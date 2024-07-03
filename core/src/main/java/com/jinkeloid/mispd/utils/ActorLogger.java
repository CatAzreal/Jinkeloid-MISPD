package com.jinkeloid.mispd.utils;

import java.util.HashMap;
import java.util.Map;

public class ActorLogger extends Logger {

    private static final String[] ENTRY_HEADER = {
            "timestamp", "turn", "depth", "actorClass", "method", "param1", "param2"
    };

    public ActorLogger(int maxEntries, String filename) {
        super(maxEntries, filename, ENTRY_HEADER);
    }

    // Since java does not support conditional formatting, gotta create multiple methods to handle different cases
    public void logActorEntry(Class<?> actorClass) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("actorClass", actorClass.toString());
        entryData.put("method", "");
        entryData.put("param1", "");
        entryData.put("param2", "");
        addEntry(entryData);
    }
    public void logActorEntry(Class<?> actorClass, String method) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("actorClass", actorClass.toString());
        entryData.put("method", method);
        entryData.put("param1", "");
        entryData.put("param2", "");
        addEntry(entryData);
    }
    public void logActorEntry(Class<?> actorClass, String method, String param1) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("actorClass", actorClass.toString());
        entryData.put("method", method);
        entryData.put("param1", param1);
        entryData.put("param2", "");
        addEntry(entryData);
    }

    public void logActorEntry(Class<?> actorClass, String method, String param1, String param2) {
        Map<String, String> entryData = new HashMap<>();
        entryData.put("actorClass", actorClass.toString());
        entryData.put("method", method);
        entryData.put("param1", param1);
        entryData.put("param2", param2);
        addEntry(entryData);
    }
}