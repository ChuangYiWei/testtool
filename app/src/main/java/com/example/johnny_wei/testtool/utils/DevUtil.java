package com.example.johnny_wei.testtool.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import static android.content.pm.PackageManager.FEATURE_BLUETOOTH;
import static android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_COARSE_LOCATION;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;

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
    public static void enableBluetooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void enableBluetoothPermisison(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            //未取得權限，向使用者要求允許權限
            if (permission != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }
    }

    public static boolean hasBLUETOOTH_Feature(Context argContext) {
        return (argContext == null) ? false : argContext.getPackageManager().hasSystemFeature(FEATURE_BLUETOOTH);
    }

    public static boolean hasBLUETOOTH_LE_Feature(Context argContext) {
        return (argContext == null) ? false : argContext.getPackageManager().hasSystemFeature(FEATURE_BLUETOOTH_LE);
    }

    public static boolean isBleEnable(Context context) {
        if (!hasBLUETOOTH_LE_Feature(context)) {
            return false;
        }
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        return manager.getAdapter().isEnabled();
    }

    public static boolean if_BLE5_API_support()
    {
        return Build.VERSION.SDK_INT >= 26;
    }

    public static boolean if_LeScanner_API_support()
    {
        return Build.VERSION.SDK_INT >= 21;
    }
    @TargetApi(23)
    public static void request_WritePermissions(Activity argActivity) {
        if(Build.VERSION.SDK_INT >= 23){
            int permission = argActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //未取得權限，向使用者要求允許權限
            if (permission != PackageManager.PERMISSION_GRANTED) {
                argActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
        else {
            // do nothing
        }
    }
}
