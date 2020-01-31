package com.example.johnny_wei.testtool;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ble extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    private Handler m_userHandler;
    private BluetoothAdapter mBluetoothAdapter;
    static final int PERMISSION_REQUEST_COARSE_LOCATION = 101;
    private static final long SCAN_PERIOD = 5000;
    private static final int REQUEST_ENABLE_BT = 1;

    //service
    private LoaclServiceConnection mLoaclServiceConnection;
    static bleService mbleService;

    EditText ed_mac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        enableBluetooth();
        init();
        openBleService();
//        scanLeDevice(true, SCAN_PERIOD);

        setupview();
        long unixTime = System.currentTimeMillis();
        Log.d("jjj",Long.toString(unixTime));
        SystemClock.sleep(50);
        long unixTime2 = System.currentTimeMillis();
        Log.d("jjj",Long.toString(unixTime2));
    }

    private void setupview() {
        ed_mac = findViewById(R.id.mb_ed_mac);
    }


    private void init() {
        HandlerThread ht_thread = new HandlerThread("name");
        ht_thread.start();
        m_userHandler = new Handler(ht_thread.getLooper());
    }

    private void enableBluetooth() {

        //if API level > 23, we need to ask permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permission = this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            //未取得權限，向使用者要求允許權限
            if (permission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

//      Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to// BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) this.getSystemService(this.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK){
            Log.d(TAG,"open bluetooth OK");
        }else if(requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED){
            Toast.makeText(ble.this,"please open bluetooth !! ",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == 0) {
            Log.e(TAG, "grantResults size is 0");
            return;
        }
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                break;
            }
        }
    }

    private boolean mScanning;
    private void scanLeDevice(final boolean enable,final long scanTime) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            m_userHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, scanTime);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }


    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    String name = device.getName();
                    if(null != name) {
                        Log.i(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + Integer.toString(rssi));
                    }
                }
            };

    public void clk_scan_start(View view) {
        scanLeDevice(true,SCAN_PERIOD);
    }

    public void clk_scan_stop(View view) {
        scanLeDevice(false,SCAN_PERIOD);
    }

    public void clk_connect(View view) {
        mbleService.connect(ed_mac.getText().toString());
    }

    public void clk_disconnect(View view) {
        mbleService.disconnect();
    }

    private class LoaclServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mbleService = ((bleService.MyBinder)service).getService();
            if (!mbleService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            else{ Log.d(TAG, "mbleService.initialize() success");}
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //service 物件設為null
            mbleService = null;
        }
    }

    private void openBleService() {
        mLoaclServiceConnection = new LoaclServiceConnection();
        Intent gattServiceIntent = new Intent(this, bleService.class);
        bindService(gattServiceIntent, mLoaclServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        unbindService(mLoaclServiceConnection);
        super.onDestroy();
    }
}
