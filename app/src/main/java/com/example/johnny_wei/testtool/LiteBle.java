package com.example.johnny_wei.testtool;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import static com.example.johnny_wei.testtool.permision_test.PERMISSION_REQUEST_COARSE_LOCATION;

public class LiteBle {
    private Context context;
    private BluetoothManager mbluetoothManager;
    private BluetoothAdapter mbluetoothAdapter;
    private final int REQUEST_ENABLE_BT = 1;
    private Handler m_userHandler;
    String TAG = getClass().getSimpleName();

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_SCANNING = 1;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_SERVICES_DISCOVERED = 4;

    private int connectionState = STATE_DISCONNECTED;

    public LiteBle(Context context) {
        this.context = context = context.getApplicationContext();
        mbluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mbluetoothAdapter = mbluetoothManager.getAdapter();
        HandlerThread ht_thread = new HandlerThread("name");
        ht_thread.start();
        m_userHandler = new Handler(ht_thread.getLooper());
    }

    public BluetoothManager getBluetoothManager() {
        return mbluetoothManager;
    }

    public BluetoothAdapter getBluetoothAdapter() {
        return mbluetoothAdapter;
    }

    public void enableBluetoothIfDisabled(Activity activity, int requestCode) {

        if (!mbluetoothAdapter.isEnabled()) {
            //if API level > 23, we need to ask permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int permission = activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                //未取得權限，向使用者要求允許權限
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            }

            // Ensures Bluetooth is available on the device and it is enabled. If not,
            // displays a dialog requesting user permission to enable Bluetooth.
            if (!mbluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    private boolean mScanning;

    public void startLeScan(final BluetoothAdapter.LeScanCallback scanCallback, final long scanTime) {
        // Stops scanning after a pre-defined scan period.
        m_userHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopLeScan(scanCallback);
            }
        }, scanTime);

        connectionState = STATE_SCANNING;
        mbluetoothAdapter.startLeScan(scanCallback);
    }

    public void stopLeScan(BluetoothAdapter.LeScanCallback scanCallback) {
        mbluetoothAdapter.stopLeScan(scanCallback);
        if (connectionState == STATE_SCANNING) {
            connectionState = STATE_DISCONNECTED;
        }
    }

    /**
     * return
     * {@link #STATE_DISCONNECTED}
     * {@link #STATE_SCANNING}
     * {@link #STATE_CONNECTING}
     * {@link #STATE_CONNECTED}
     * {@link #STATE_SERVICES_DISCOVERED}
     */
    public int getConnectionState() {
        return connectionState;
    }
}