package com.example.johnny_wei.testtool.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import static android.content.pm.PackageManager.FEATURE_BLUETOOTH;
import static android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE;

public class DevUtil {
    private static final String TAG = "DevUtil";

    public static String GetDeviceModel(){
        return Build.MODEL;
    }

    public static String GetSystemVersion(){
        return Build.VERSION.RELEASE;
    }

    public static String GetBrand(){
        return Build.BRAND;
    }

    public static String Get_MANUFACTURER(){
        return Build.MANUFACTURER;
    }

    public static int GetVersionSDK(){
        return Build.VERSION.SDK_INT;
    }

    public static void printDeviceInfo() {
        Log.d(TAG, "Brand:" + DevUtil.GetBrand());
        Log.d(TAG, "Dev model:" + DevUtil.GetDeviceModel());
        Log.d(TAG, "    System ver:" + DevUtil.GetSystemVersion());
        Log.d(TAG, "    SDK ver:" + DevUtil.GetVersionSDK());
        Log.d(TAG, "    MANUFACTURER" + DevUtil.Get_MANUFACTURER());
    }

    public static boolean hasBLUETOOTH_Feature(Context argContext) {
        return (argContext == null) ? false : argContext.getPackageManager().hasSystemFeature(FEATURE_BLUETOOTH);
    }

    public static boolean hasBLUETOOTH_LE_Feature(Context argContext) {
        return (argContext == null) ? false : argContext.getPackageManager().hasSystemFeature(FEATURE_BLUETOOTH_LE);
    }

    public static boolean if_BLE5_API_support()
    {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean if_LeScanner_API_support()
    {
        return Build.VERSION.SDK_INT >= 21;
    }

}
