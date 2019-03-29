package com.example.johnny_wei.testtool.log;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class dbgLog {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private static final String className = "dbgLog";

    private static boolean DEBUG_ENABLED = false;
    private static String PATH = "debug.txt";

    private static String SD_PATH = Environment.getExternalStorageDirectory().getPath();

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static void setDebug(boolean debug) {
        DEBUG_ENABLED = debug;
    }

    public static void setPath(String path) {
        if (path.endsWith("/")) {
            PATH = path + "debug.txt";
//        } else if (!path.endsWith(".txt")) {
//            PATH  = path + ".txt";
        } else {
            PATH = path;
        }
    }

    public static void e(String tag, String message) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.e(tag, message);
            if (logResult > 0)
                logToFile(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable error) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.e(tag, message, error);
            if (logResult > 0)
                logToFile(tag, message + "\r\n" + android.util.Log.getStackTraceString(error));
        }
    }

    public static void v(String tag, String message) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.v(tag, message);
            if (logResult > 0)
                logToFile(tag, message);
        }
    }

    public static void v(String tag, String message, Throwable error) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.v(tag, message, error);
            if (logResult > 0)
                logToFile(tag, message + "\r\n" + android.util.Log.getStackTraceString(error));
        }
    }

    public static void d(String tag, String message) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.d(tag, message);
            if (logResult > 0)
                logToFile(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable error) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.d(tag, message, error);
            if (logResult > 0)
                logToFile(tag, message + "\r\n" + android.util.Log.getStackTraceString(error));
        }
    }

    public static void i(String tag, String message) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.i(tag, message);
            if (logResult > 0)
                logToFile(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable error) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.i(tag, message, error);
            if (logResult > 0)
                logToFile(tag, message + "\r\n" + android.util.Log.getStackTraceString(error));
        }
    }

    public static void w(String tag, String message) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.w(tag, message);
            if (logResult > 0)
                logToFile(tag, message);
        }
    }

    public static void w(String tag, String message, Throwable error) {
        if (DEBUG_ENABLED) {
            int logResult = android.util.Log.w(tag, message, error);
            if (logResult > 0)
                logToFile(tag, message + "\r\n" + android.util.Log.getStackTraceString(error));
        }
    }

    public static boolean isLoggable(String string, int num) {
        return true;
    }

    private static String getDateTimeStamp() {
        String dts = sdf.format(new Date());
        return dts;

    }
    //ex: dbgLog.setPath("123/456/789");//,folder is 123/456, file is 789
    private static void logToFile(String tag, String message) {
        try {
            File logFile = new File(Environment.getExternalStorageDirectory(), PATH);
            Log.d("jjj","PATH is:" + PATH);
            if (!logFile.exists()) {
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
            }
            if (logFile.length() > 2097152) { // 2 MB
                logFile.delete();
                logFile.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
            writer.write(String.format("%1s [%2s]:%3s\r\n", getDateTimeStamp(), tag, message));
            writer.close();

        } catch (IOException e) {
            android.util.Log.e(className, "Unable to log exception to file.", e);
        }
    }


    public static void deletefile(String fileName) {
        File file = new File(Environment.getExternalStorageDirectory(), fileName);
        if(!file.exists())
        {
            logToFile("file", "file: " + file + " not exist");
            return;
        }
        Log.w(className, "delete file:" + file.getPath());
        file.delete();
    }

    public static void deletefolder(String dirName) {
        File dir = new File(Environment.getExternalStorageDirectory(), dirName);
        if(!dir.exists())
        {
            logToFile("file", "dirName: " + dirName + " not exist");
            return;
        }
        Log.w(className, "delete folder dir:" + dir.getPath());
        deleteRecursive(dir);
    }

    static void  deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

}
