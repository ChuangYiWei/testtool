package com.BLE.johnny_wei.testtool;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class advdata extends AppCompatActivity {
    final static String TAG = "advdata";
    //com.example.johnny_wei.testtool.ble

    private Handler mHandler;
    private BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 5000;
    private boolean mScanning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advdata);

        byte[] adv = {
                0x02,0x01,0x06,
                0x1a,(byte)0xff,0x4c,0x00,0x02,0x15,(byte)0xfd,(byte)0xa5,0x06,
                (byte)0x93,(byte)0xa4,(byte)0xe2,0x4f,(byte)0xb1,(byte)0xaf,(byte)0xcf,(byte)0xc6,
                (byte)0xeb,0x07,0x64,0x78,0x25,0x27,0x25,0x7f,0x16,(byte)0xc5,
                0x0d,0x09,0x32,0x30,0x31,0x38,0x5f,0x31,0x30,0x33,0x31,0x5f,0x31,0x37,
                0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        Log.d(TAG, "adv length:" + adv.length);
        Log.d(TAG, "scan record:" + byteArrayToHex(adv));

        parseAdv(adv);
        parseScanRecordAsSparseArray(adv);
//        init();
//        scanLeDevice(true);
    }

    public static void parseScanRecordAsSparseArray(final byte[] scanRecord) {
        StringBuilder sb = new StringBuilder();


        int index = 0;
        while (index < scanRecord.length) {
            final int length = scanRecord[index++];
            //Done once we run out of records
            if (length == 0) break;

            final int type = scanRecord[index] & 0xFF;

            //Done if our record isn't a valid type
            if (type == 0) break;

            final byte[] data = Arrays.copyOfRange(scanRecord, index + 1, index + length);


            Log.d(TAG,"length:" + String.format("%02x", length));
            Log.d(TAG,"type:" + String.format("%02x", type));
            Log.d(TAG,"data:" +  byteArrayToHex(data));

            sb.append("length:" + String.format("0x%02x", length) + "\n");
            sb.append("type:" + String.format("0x%02x", type)+"\n");
            sb.append("data:" + "0x" +byteArrayToHex(data)+"\n");

            Log.w("jjj",sb.toString());
            //Advance
            index += length;
        }

        return;
    }

    private void parseAdv(byte[] adv_data) {
        if (adv_data.length == 0) {
            Log.e(TAG, "adv_data len is 0");
            return;
        }
        int ptr = 0;
        int len = adv_data[ptr];
        while (len > 0) {
            byte type = adv_data[ptr + 1];
            Log.d(TAG, "ad len: " + Integer.toString(len) + ",ad type:" + String.format("0x%02x", type));
            switch (type) {
                case 0x01: // Flags
                    break;
                case (byte) 0xFF: // “Manufacturer Specific Data”
                    break;
                default: // skip
                    break;
            }
            int dataLen = len - 1;// minus type(1 byte) length
            //allocate byte array
            byte[] data = new byte[dataLen];
            //copy data , skip len(1 byte) and type(1 byte),so ptr + 2
            System.arraycopy(adv_data, ptr + 2, data, 0, dataLen);
            Log.d(TAG, "ad data :" + byteArrayToHex(data));

            int nextAD = ptr + len + 1;
            ptr = nextAD;

            //next AD length
            len = adv_data[ptr];
        }
    }
    private static final int REQUEST_ENABLE_BT = 1;
    private void init() {
        mHandler = new Handler();

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
//                    checkAdvAvgIntv(100);
//                    getAvgRssi();
//                    rssiList.clear();
                }
            }, SCAN_PERIOD);

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

                    if (name != null) {
                        if (device.getAddress().equals("20:18:10:31:17:56")) {
                            //AddAdvIntvTime();
                            //parseAdv(scanRecord);
                            //Log.d(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + Integer.toString(rssi));
                            //Log.d(TAG, "rssi:" + Integer.toString(rssi));
                            AddRssi(rssi);
//                            Log.d(TAG, "scan record length:" + scanRecord.length);
                            Log.d(TAG, "scan record:" + byteArrayToHex(scanRecord));
                        }
                    }
                }
            };

    //TODO:reset to default
    long previous_time = 0;
    long current_time = 0;

    List<Integer> advIntvList = new ArrayList<Integer>();
    List<Integer> rssiList = new ArrayList<Integer>();

    private void AddRssi(int rssi) {
        rssiList.add(rssi);
    }

    private int getAvgRssi() {
        if(rssiList.size() == 0) {
            Log.d(TAG, "list size is 0");
            return -1;
        }
        int sum = 0;
        for(int rssi : rssiList) {
            Log.d(TAG, Integer.toString(rssi));
            sum = sum + rssi;
        }
        int avgRssi = sum / (rssiList.size());
        Log.d(TAG, "avgRssi:" + Integer.toString(avgRssi));
        return  avgRssi;
    }

    private void AddAdvIntvTime() {
        if (current_time == 0) {
            current_time = System.currentTimeMillis();
            previous_time = current_time;
        } else {
            current_time = System.currentTimeMillis();
            long diff_time = current_time - previous_time;
            advIntvList.add((int)diff_time);
            Log.d(TAG, "diff time:" + Long.toString(diff_time));
            previous_time = current_time;
        }
    }

    //if avgAdvIntv bigger than 2*msec return false
    private boolean checkAdvAvgIntv(int msec) {
        if(advIntvList.size() == 0) {
            Log.d(TAG, "list size is 0");
            return false;
        }
        int sum = 0;
        for(int x : advIntvList) {
            Log.d(TAG, Integer.toString(x));
            sum = sum + x;
        }
        int avgAdvIntv = sum / (advIntvList.size());
        Log.d(TAG, "average adv Interval:" + Integer.toString(avgAdvIntv));
        if(avgAdvIntv >(2*msec)) {
            Log.d(TAG, "[err] average adv Interval is not correct)");
            return false;
        }

        return true;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public void clk_start(View view) {
        scanLeDevice(true);
    }

    public void clk_stop(View view) {
        scanLeDevice(false);
    }
}
