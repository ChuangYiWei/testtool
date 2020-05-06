package com.BLE.johnny_wei.testtool.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import static com.BLE.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_COARSE_LOCATION;
import static com.BLE.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;


public class permissionUtil implements  ActivityCompat.OnRequestPermissionsResultCallback {
    Activity mActivity;
    String className = getClass().getSimpleName();
    public permissionUtil(Activity activity)
    {
        mActivity = activity;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("Permission", "onRequestPermissionsResult");
        if (grantResults.length == 0) {
            Log.e(className, "grantResults size is 0");
            return;
        }
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(className, "coarse location permission granted");
                } else {
                    AlertDialogUtil.simpleDialog(mActivity,
                            "Functionality limited",
                            "Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                }
                break;
            }
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d(className, "storage location permission granted");
                } else {
                    AlertDialogUtil.simpleDialog(mActivity,
                            "Functionality limited",
                            "storage access has not been granted, debug log disable");
                }
            }
            break;
        }
    }
}
