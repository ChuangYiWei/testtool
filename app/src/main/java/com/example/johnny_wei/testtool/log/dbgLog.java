package com.example.johnny_wei.testtool.log;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dbgLog {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    private static final String className = "dbgLog";

    private static boolean DEBUG_ENABLED = false;
    private static String PATH = "D_DEBUG/debug_0";
    private static String CURRENT_PATH = PATH;
    private static String DEBUG_FOLDER = "D_DEBUG/";
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
                circularlogToFile(tag, message);
                //logToFile(tag, message);

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

    //ex: dbgLog.setPath("123/456/789");
    // folder will be 123/456, file name is 789
    private static void logToFile(String tag, String message) {
        try {
            File logFile = new File(Environment.getExternalStorageDirectory(), PATH);
            Log.d(className, "log file path is:" + PATH);
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

    private static void circularlogToFile(String tag, String message) {
        try {
            File logFile = new File(Environment.getExternalStorageDirectory(), CURRENT_PATH);
            int maxFileNum = findLargetExtNum(DEBUG_FOLDER);
            if (maxFileNum == -1) {//if no debug_x file exist
                logFile.getParentFile().mkdirs();
                logFile.createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
                writer.write(String.format("%1s [%2s]:%3s\r\n", getDateTimeStamp(), tag, message));
                writer.close();
            } else {
                //if file larger than 2M, create new file debug_(x+1)
                if (logFile.length() > 2097152) { // 2 MB

                    String newFileName = GetFilebyAddsuffix("debug_" + maxFileNum);
                    CURRENT_PATH = DEBUG_FOLDER + newFileName;
                    File newlogFile = new File(Environment.getExternalStorageDirectory(), CURRENT_PATH);
                    newlogFile.createNewFile();
                    Log.w(className, "create file" + newlogFile.getPath());
                    BufferedWriter writer = new BufferedWriter(new FileWriter(newlogFile, true));
                    writer.write(String.format("%1s [%2s]:%3s\r\n", getDateTimeStamp(), tag, message));
                    writer.close();
                } else//keep writing current file debug_x
                {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true));
                    writer.write(String.format("%1s [%2s]:%3s\r\n", getDateTimeStamp(), tag, message));
                    writer.close();
                }
            }

        } catch (Exception e) {
            android.util.Log.e(className, "Unable to log exception to file.", e);
        }
    }

    private static String GetFilebyAddsuffix(String filename) {
        //debug_01 => debug_02
        int indexof_ = filename.indexOf("_");
        int num = Integer.parseInt(filename.substring(indexof_ + 1));
        num = num + 1;
        String new_filename = filename.substring(0, indexof_ + 1) + num;
        Log.d(className, "new_filename:" + new_filename);
        return new_filename;
    }

    //find largest number after "debug_x"
    private static int findLargetExtNum(String folderName) {
        int MaxNum = -1;
        int current = MaxNum;
        try {
            File directory = new File(Environment.getExternalStorageDirectory(), folderName);
            //get all the files from a directory
            File[] fList = directory.listFiles();

            for (File file : fList) {
                System.out.println(file.getName());
                //find largest file num extension
                current = Integer.parseInt(file.getName().substring(file.getName().indexOf("_") + 1));
                if (current > MaxNum) {
                    MaxNum = current;
                }
            }
        } catch (Exception e) {
            Log.e(className, e.toString());
            return -1;
        }
        return current;
    }

    /*create folder under sd path*/
    public static  boolean createFoler(String folderName) {
        if (!checkExternalMedia()) {
            return false;
        }

        String absPath = SD_PATH + File.separator + folderName;
        Log.d(className, "create Folder path is " + absPath);
        File folder = new File(absPath);

        if (!folder.exists()) {
            boolean success = folder.mkdirs();
            if (success) {
                Log.d(className, "create folder " + absPath + " success");
                return true;
            } else {
                Log.d(className, "create folder " + absPath + "fail");
                return false;
            }
        } else {
            Log.d(className, "folder exist");
        }

        return true;
    }

    /*check external storage permission*/
    private static boolean checkExternalMedia() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //Log.d(TAG, "sd card mounted with read/write access");
            return true;
        } else {
            Log.d(className, "mount fail state : " + state);
        }
        return false;
    }


    //delete--------------------------------------------------------------
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
