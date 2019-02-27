package com.example.johnny_wei.testtool.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;

public class Permission {
    private static Activity mActivity;

    public static void setActivity(Activity activity)
    {
        mActivity = activity;
    }

    public static boolean shouldAskPermissions() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(23)
    public static void WritePermissionsReq() {
        int permission = mActivity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //未取得權限，向使用者要求允許權限
        if (permission != PackageManager.PERMISSION_GRANTED) {
            mActivity.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }
}
