package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.BLEutils.BleUtil;
import com.example.johnny_wei.testtool.BLEutils.LiteBle;
import com.example.johnny_wei.testtool.adapter.RowItem;
import com.example.johnny_wei.testtool.adapter.customAdapter;
import com.example.johnny_wei.testtool.utils.DevUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.johnny_wei.testtool.BLEutils.LiteBle.StaticLitebleArray;
import static com.example.johnny_wei.testtool.config.globalConfig.DUT;
import static com.example.johnny_wei.testtool.config.globalConfig.MB;

//mock up , create ble instance
public class mockup extends AppCompatActivity {
    String TAG = getClass().getSimpleName();

    //adapter
    ListView listView;
    List<RowItem> rowItems;
    customAdapter mAdapter;

    //save device addr key
    private HashMap<String, Integer> mdevMap;
    final int SCAN_TIME = 5000;

    private Activity mActivity = this;

    @RequiresApi(21)
    private ScanCallback mScanCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mockup);
        init();
        setupAdapter();
        dev_scan();
    }

    @Override
    protected void onDestroy() {
        uninit();
        super.onDestroy();
    }

    void init() {
        mdevMap = new HashMap<String, Integer>();
    }

    void uninit() {
        mdevMap.clear();
    }

    /**go back to previous activity*/
    public void finish(String addr) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("EXTRAS_DEVICE_ADDRESS", addr);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }

    void dev_scan() {
        if (BleUtil.if_LeScanner_API_support()) {
            mScanCallback = new LeScannerAPI21();
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            StaticLitebleArray[DUT].startAPI21_LeScan(mScanCallback, null, settings, SCAN_TIME);
        } else {
            StaticLitebleArray[DUT].startLeScan(mLegacyScanCallback, SCAN_TIME);
        }
    }

    void setupAdapter() {
        listView = (ListView) findViewById(R.id.scan_only_list);
        rowItems = new ArrayList<>();
        mAdapter = new customAdapter(this, rowItems);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Item " + (position + 1) + ": " + rowItems.get(position).getstrL2(),
                        Toast.LENGTH_SHORT);
                toast.show();
                dev_scan_stop();
                //finish and back to calling activity
                finish(rowItems.get(position).getstrL2());
            }
        });
    }


    void dev_scan_stop()
    {
        if (BleUtil.if_LeScanner_API_support()) {
            StaticLitebleArray[DUT].stopAPI21_LeScan(mScanCallback);
        } else {
            StaticLitebleArray[DUT].stopLeScan(mLegacyScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLegacyScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    final String devName = device.getName();
                    final String devAddr = device.getAddress();

                    Log.i(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + "rssi:" + (rssi));
                    if (null != devAddr) {
                        if ((!mdevMap.containsKey(devAddr))) {
                            mdevMap.put(devAddr, rssi);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RowItem rowItem = new RowItem(devName, devAddr, Integer.toString(rssi));
                                    rowItems.add(rowItem);
                                    (mAdapter).notifyDataSetChanged();
                                }
                            });
                        }
                    }
                }
            };

    @RequiresApi(21)
    public class LeScannerAPI21 extends ScanCallback
    {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i(TAG, "Dev name:" + result.getDevice().getName() +
                    ", Dev address:" + result.getDevice().getAddress() +
                    ", Dev rssi:" + result.getRssi());

            final String devAddr = result.getDevice().getAddress();
            final String devName = result.getDevice().getName();
            final int rssi = result.getRssi();
            if (null != devAddr) {
                //if we don't have devAddr yet, add devaddr/rssi as key/value
                if ((!mdevMap.containsKey(devAddr) ) ) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RowItem rowItem = new RowItem(devName, devAddr, Integer.toString(rssi));
                            rowItems.add(rowItem);
                            (mAdapter).notifyDataSetChanged();
                        }
                    });
                }
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            Log.d(TAG, "onBatchScanResults");
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e(TAG, "Discovery onScanFailed: " + errorCode);
            super.onScanFailed(errorCode);
        }
    }

}
