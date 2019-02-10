package com.example.johnny_wei.testtool.utils;

import android.os.Build;
import android.util.Log;

public class devUtil {
    private static final String TAG = "devUtil";
    public static String GetDeviceModel(){
        return Build.MODEL;
    }

    public static String GetSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    public static String GetBrand(){
        return Build.BRAND;
    }

    public static int GetVersionSDK(){
        return Build.VERSION.SDK_INT;
    }

    public static void printDeviceInfo(){
        Log.d(TAG, "Brand:"+devUtil.GetBrand());
        Log.d(TAG, "Dev model:"+devUtil.GetDeviceModel());
        Log.d(TAG, "    System ver:"+devUtil.GetSystemVersion());
        Log.d(TAG, "    SDK ver:"+devUtil.GetVersionSDK());
    }

}
