package com.example.johnny_wei.testtool.BLEutils;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import static android.content.pm.PackageManager.FEATURE_BLUETOOTH;
import static android.content.pm.PackageManager.FEATURE_BLUETOOTH_LE;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_COARSE_LOCATION;

public class BleUtil {
    public static void enableBluetooth(Activity activity, int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    public static String GetDevMAC(Activity activity) {
        return android.provider.Settings.Secure.getString(activity.getContentResolver(), "bluetooth_address");
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
}
