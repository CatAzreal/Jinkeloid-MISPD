package com.jinkeloid.mispd.utils;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.jinkeloid.mispd.Dungeon;
import com.jinkeloid.mispd.actors.Actor;
import com.jinkeloid.mispd.ui.Banner;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;

public abstract class Logger extends Gizmo {
    private LinkedList<Map<String, String>> entries;
    private int maxEntries;
    private File logFile;
    private float time;

    private static float logExportInterval = 10;
    private static final String TAG = "Logger";
    private final String[] entryHeader;
    private boolean headerWritten = false;

    public Logger(int maxEntries, String filename, String[] entryHeader) {
        DeviceCompat.log(TAG, "Logger Created");
        this.entries = new LinkedList<>();
        this.maxEntries = maxEntries;
        this.time = logExportInterval;
        this.entryHeader = entryHeader;

        try {
            this.logFile = FileUtils.getFileHandle(filename).file();
            if (!this.logFile.exists()) {
                this.logFile.createNewFile();
            }
        } catch (Exception e) {
            DeviceCompat.log(TAG, "Failed to create file handle: " + e.getMessage());
        }
    }

    public static String getHMSM() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        return String.format(Locale.CHINA,"%02d:%02d:%02d:%03d", hour, minute, second, millisecond);
    }

    public synchronized void addEntry(Map<String, String> entryData) {
        entryData.put("timestamp", getHMSM());
        entryData.put("turn", String.valueOf((int) Actor.now()));
        entryData.put("depth", String.valueOf(Dungeon.depth));
        if (entryData.size() != entryHeader.length) {
            throw new IllegalArgumentException("Entry data does not match template length.");
        }
        entries.addLast(entryData);
        if (entries.size() > maxEntries) {
            entries.removeFirst();
        }
        logToFile();
    }

    public synchronized void clearEntries() {
        entries.clear();
    }

    public synchronized void logToFile() {
        if (entries.isEmpty()) {
            return;
        }
        FileHandle fileHandle = FileUtils.getFileHandle(this.logFile.getName());
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileHandle.write(true)))) {
            if (!headerWritten) {
                writer.write(formatHeader() + "\n");
                headerWritten = true;
            }
            while (!entries.isEmpty()) {
                Map<String, String> entry = entries.poll();
                writer.write(formatEntry(entry) + "\n");
            }
        } catch (IOException e) {
            DeviceCompat.log(TAG, "Error writing to file: " + e.getMessage());
        }
    }

    private String formatHeader() {
        StringBuilder sb = new StringBuilder();
        for (String key : entryHeader) {
            sb.append(key).append(",");
        }
        sb.setLength(sb.length() - 1); // Remove trailing comma
        return sb.toString();
    }

    private String formatEntry(Map<String, String> entry) {
        StringBuilder sb = new StringBuilder();
        for (String key : entryHeader) {
            if (entry.containsKey(key)) sb.append(entry.get(key));
            sb.append(",");
        }
        sb.setLength(sb.length() - 1); // Remove trailing comma
        return sb.toString();
    }

    public void exportLog(String filename, String method) {
        if ("clear".equals(method)) {
            clearEntries();
            Game.platform.clearText(filename);
        } else if ("share".equals(method)) {
            Game.platform.shareText(filename);
        }
    }

    @Override
    public void update() {
        super.update();
        time -= Game.elapsed;
        if (time <= 0) {
            DeviceCompat.log(TAG, "Exporting log to file");
            logToFile();
            time = logExportInterval;
        }
    }
}