package com.BLE.johnny_wei.testtool.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;

public class commonUtil {

    public static byte[] intToByteArray(int a)
    {
        byte[] ret = new byte[4];
        ret[3] = (byte) (a & 0xFF);
        ret[2] = (byte) ((a >> 8) & 0xFF);
        ret[1] = (byte) ((a >> 16) & 0xFF);
        ret[0] = (byte) ((a >> 24) & 0xFF);
        return ret;
    }

    public static int byteArrayToInt(byte[] b)
    {
        return (b[3] & 0xFF) + ((b[2] & 0xFF) << 8) + ((b[1] & 0xFF) << 16) + ((b[0] & 0xFF) << 24);
    }

    public static int byteArrayToInt(byte[] b, int index){
        return   b[index+3] & 0xFF |
                (b[index+2] & 0xFF) << 8 |
                (b[index+1] & 0xFF) << 16 |
                (b[index+0] & 0xFF) << 24;
    }

    //ms convert to hours + ":" + minutes + ":" + seconds
    public static String getElapsedTimeMinutesSecondsString(int miliseconds) {
        int elapsedTime = miliseconds;
        String format = String.format("%%0%dd", 2);
        elapsedTime = elapsedTime / 1000;
        int mins = elapsedTime / 60;
        int hrs = elapsedTime / 3600;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, mins % 60);
        String hours = String.format(format, hrs);
        return hours + ":" + minutes + ":" + seconds;
    }

    //The file root folder in the app, path:sd/Android/data/com.xxx.xxx/files/Folder X
    public static boolean is_File_Exist(String absfileName, Context context) {
        File file = new File(context.getExternalFilesDir(null),absfileName);
        if (!file.exists()) {
            Log.w("commonUtil", "fileName: " + absfileName + " not exist");
            return false;
        }
        Log.d("commonUtil", "fileName: " + absfileName + " exist");
        return true;
    }
}
