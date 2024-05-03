package com.jinkeloid.mispd.utils;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.jinkeloid.mispd.ui.Banner;
import com.watabou.noosa.Game;
import com.watabou.noosa.Gizmo;
import com.watabou.utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;

public class Logger extends Gizmo {
    private LinkedList<String> entries;
    private int maxEntries;
    private File logFile;
    private float time;

    private static float logExportInterval = 10;

    public Logger(int maxEntries, String filename) {
        GLog.i("Logger created");
        this.entries = new LinkedList<>();
        this.maxEntries = maxEntries;
        this.time = logExportInterval;

        try {
            this.logFile = FileUtils.getFileHandle(filename).file();
        } catch (Exception e) {
            GLog.i("Failed to create file handle: " + e.getMessage());
        }
    }

    public void addEntry(String entry) {
        if (entries.size() >= maxEntries) {
            entries.removeFirst();  // Remove the oldest entry
        }
        entries.addLast(entry);  // Add new entry
    }

    public void clearEntries() {
        entries.clear();
    }

    public void logToFile() {
        FileHandle fileHandle = FileUtils.getFileHandle(this.logFile.getName());
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileHandle.write(true)));
            for (String entry : entries) {
                writer.write(entry + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public void exportLog(String filename, String method) {
        if ("clipboard".equals(method)) {
            Game.platform.copyToClipboard(filename);
        } else if ("share".equals(method)) {
            Game.platform.shareText(filename);
        }
    }

    @Override
    public void update() {
        super.update();

        time -= Game.elapsed;
        if (time <= 0) {
            GLog.i("Exporting log to file");
            logToFile();
            time = logExportInterval;
        }
    }
}

