package com.example.johnny_wei.testtool;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    static String TAG = "MyService";
    //service usage
    private final IBinder mBinder = new MyBinder();

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public static boolean initialize() {
        return true;
    }

    public static boolean connect() {
        Log.d(TAG, "connect");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
