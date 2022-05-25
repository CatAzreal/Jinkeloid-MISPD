/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.jinkeloid.mispd.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidClipboard;
import com.jinkeloid.mispd.MISPDSettings;
import com.jinkeloid.mispd.MusicImplantSPD;
import com.jinkeloid.mispd.messages.Messages;
import com.jinkeloid.mispd.services.news.News;
import com.jinkeloid.mispd.services.news.NewsImpl;
import com.jinkeloid.mispd.services.updates.UpdateImpl;
import com.jinkeloid.mispd.services.updates.Updates;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AndroidGame extends AndroidApplication {
	
	public static AndroidApplication instance;
	
	private static AndroidPlatformSupport support;

	private static int STACK_TRACE_SIZE = 1000;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup handler for uncaught exceptions.
		Thread.setDefaultUncaughtExceptionHandler (new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException (Thread thread, Throwable e) {
				handleUncaughtException (thread, e);
			}
		});

		//there are some things we only need to set up on first launch
		if (instance == null) {

			instance = this;

			try {
				Game.version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (PackageManager.NameNotFoundException e) {
				Game.version = "???";
			}
			try {
				Game.versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			} catch (PackageManager.NameNotFoundException e) {
				Game.versionCode = 0;
			}

			if (UpdateImpl.supportsUpdates()) {
				Updates.service = UpdateImpl.getUpdateService();
			}
			if (NewsImpl.supportsNews()) {
				News.service = NewsImpl.getNewsService();
			}

			FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

			// grab preferences directly using our instance first
			// so that we don't need to rely on Gdx.app, which isn't initialized yet.
			// Note that we use a different prefs name on android for legacy purposes,
			// this is the default prefs filename given to an android app (.xml is automatically added to it)
			MISPDSettings.set(instance.getPreferences("MISPD"));

		} else {
			instance = this;
		}
		
		//set desired orientation (if it exists) before initializing the app.
		if (MISPDSettings.landscape() != null) {
			instance.setRequestedOrientation( MISPDSettings.landscape() ?
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT );
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.depth = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			//use rgb888 on more modern devices for better visuals
			config.r = config.g = config.b = 8;
		} else {
			//and rgb565 (default) on older ones for better performance
		}
		
		config.useCompass = false;
		config.useAccelerometer = false;
		
		if (support == null) support = new AndroidPlatformSupport();
		else                 support.reloadGenerators();
		
		support.updateSystemUI();
		
		initialize(new MusicImplantSPD(support), config);
		
	}

	@Override
	protected void onResume() {
		//prevents weird rare cases where the app is running twice
		if (instance != this){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				finishAndRemoveTask();
			} else {
				finish();
			}
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		//do nothing, game should catch all back presses
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		support.updateSystemUI();
	}
	
	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);
		support.updateSystemUI();
	}

	public void handleUncaughtException (Thread thread, Throwable e)
	{
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.flush();
			sw.flush();
			TextView text = new TextView(this);
			runOnUiThread(new Runnable() {

				@TargetApi(Build.VERSION_CODES.Q)
				@SuppressLint({"SetTextI18n", "RtlHardcoded", "WrongConstant"})
				@Override
				public void run() {
					String remove = "\t";
					String stackTrace = sw.toString();
					stackTrace = stackTrace.replaceAll(remove, "");
					stackTrace = stackTrace.replaceAll("at ", "");
					stackTrace = stackTrace.trim();
					if (stackTrace.length() > STACK_TRACE_SIZE) {
						String disclaimer = " [stack trace too large]";
						stackTrace = stackTrace.substring(0, STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
					}
					text.setText(new StringBuilder()
							.append("MISPD崩溃啦！\n")
							.append("下面是报错记录，详细报错已复制在剪贴板上，反馈请联系qq377844252\n\n")
							.append("MISPD just crashed, what a surprise!\n")
							.append("Please dm this error log(which should be already copied to your clipboard) to me via Discord(or PD server): Jinkeloid#0864\n\n\n\n")
							.append(stackTrace).toString());
					text.setTextSize(12);
					text.setTextColor(0xFFFFFFFF);
					text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/zpix_font.ttf"));
					text.setGravity(Gravity.LEFT);
					text.setPadding(20, 15, 20, 15);
					setContentView(text);
				}
			});
			AndroidClipboard androidClipboard = new AndroidClipboard(this.getApplicationContext());
			androidClipboard.setContents(sw.toString());
			sw.close();
			pw.close();
		} catch (Exception exception) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			pw.flush();
			System.err.println(sw.toString());
		}
	}
}