package com.jinkeloid.mispd.utils;

import java.util.HashMap;
import java.util.Map;

public class ItemLogger extends Logger {

    private static final String[] ENTRY_HEADER = {
            "timestamp", "turn", "depth", "itemClass", "method", "act", "param"
    };

    public ItemLogger(int maxEntries, String filename) {
        super(maxEntries, filename, ENTRY_HEADER);
    }

    // Since java does not support conditional formatting, gotta create multiple methods to handle different cases
    public void logItemEntry(Class<?> itemClass) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("itemClass", itemClass.toString());
        entryData.put("method", "");
        entryData.put("act", "");
        entryData.put("param", "");
        addEntry(entryData);
    }
    public void logItemEntry(Class<?> itemClass, String method) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("itemClass", itemClass.toString());
        entryData.put("method", method);
        entryData.put("act", "");
        entryData.put("param", "");
        addEntry(entryData);
    }
    public void logItemEntry(Class<?> itemClass, String method, String act) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("itemClass", itemClass.toString());
        entryData.put("method", method);
        entryData.put("act", act);
        entryData.put("param", "");
        addEntry(entryData);
    }

    public void logItemEntry(Class<?> itemClass, String method, String act, String param) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("itemClass", itemClass.toString());
        entryData.put("method", method);
        entryData.put("act", act);
        entryData.put("param", param);
        addEntry(entryData);
    }
}