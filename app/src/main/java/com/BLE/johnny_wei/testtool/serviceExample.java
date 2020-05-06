package com.BLE.johnny_wei.testtool;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class serviceExample extends AppCompatActivity {
    String TAG = "serviceExample";
    //service
    private LoaclServiceConnection mLoaclServiceConnection;
    static public MyService mMyService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_example);
        openBleService();

    }
    private void openBleService() {
        mLoaclServiceConnection = new LoaclServiceConnection();
        Intent gattServiceIntent = new Intent(this, MyService.class);
        bindService(gattServiceIntent, mLoaclServiceConnection, Service.BIND_AUTO_CREATE);
    }

    private class LoaclServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //透過Binder調用Service內的方法

            mMyService = ((MyService.MyBinder)service).getService();
            if (!MyService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            MyService.connect();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //service 物件設為null
            mMyService = null;
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mLoaclServiceConnection);
        super.onDestroy();
    }
}
