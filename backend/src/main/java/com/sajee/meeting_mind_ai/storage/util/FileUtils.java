package com.sajee.meeting_mind_ai.storage.util;

public final class FileUtils {

    private FileUtils() {
    }

    public static String getExtension(String filename) {

        if (filename == null || !filename.contains(".")) {
            return "";
        }

        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}