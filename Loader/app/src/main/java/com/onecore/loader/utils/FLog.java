package com.onecore.loader.utils;

import android.content.Context;
import android.util.Log;

import com.onecore.loader.BuildConfig;
import com.onecore.loader.BoxApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FLog {
    public static final String TAG = FLog.class.getSimpleName();
    private static final Object FILE_LOCK = new Object();
    private static final SimpleDateFormat DATE_FILE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    private static final SimpleDateFormat DATE_LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    private static void writeToFile(String level, String msg) {
        try {
            Context context = BoxApplication.get();
            if (context == null) {
                return;
            }

            File externalDir = context.getExternalFilesDir(null);
            if (externalDir == null) {
                return;
            }
            File logsDir = new File(externalDir, "logs");
            if (!logsDir.exists() && !logsDir.mkdirs()) {
                return;
            }

            String date = DATE_FILE_FORMAT.format(new Date());
            File logFile = new File(logsDir, "loader-" + date + ".log");
            String line = DATE_LOG_FORMAT.format(new Date()) + " [" + level + "] " + msg;

            synchronized (FILE_LOCK) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException ignored) {
        }
    }

    private static void write(String level, String msg) {
        String safeMsg = msg == null ? "null" : msg;
        writeToFile(level, safeMsg);
    }

    public static void debug(String msg) {
        write("DEBUG", msg);
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.d(TAG, msg);
    }

    public static void info(String msg) {
        write("INFO", msg);
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.i(TAG, msg);
    }

    public static void warning(String msg) {
        write("WARN", msg);
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.w(TAG, msg);
    }

    public static void error(String msg) {
        write("ERROR", msg);
        if (!BuildConfig.DEBUG) {
            return;
        }
        Log.e(TAG, msg);
    }
}
