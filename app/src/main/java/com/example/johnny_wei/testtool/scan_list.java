package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.adapter.ListItem;
import com.example.johnny_wei.testtool.adapter.RowItem;
import com.example.johnny_wei.testtool.adapter.ScanListAdapter;
import com.example.johnny_wei.testtool.utils.DevUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class scan_list extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    ListView listView;
    List<ListItem> ListItems;
    List<RowItem> rowItems;
    ScanListAdapter mAdapter;

    private  HashMap<String, Integer> mdevMap;
    Activity thisActivity = this;

    private LiteBle liteBluetooth;

    @RequiresApi(21)
    private ScanCallback mScanCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_list);

        liteBluetooth = new LiteBle(thisActivity);
        liteBluetooth.enableBluetoothIfDisabled(thisActivity, 1);
        mdevMap = new HashMap<String, Integer>();
        setupView();
        dev_scan();
    }

    @Override
    protected void onPause() {
        mdevMap.clear();
        super.onPause();
    }

    void dev_scan() {

        if (DevUtil.if_LeScanner_API_support()) {
            mScanCallback = new LeScannerAPI21();
            liteBluetooth.startAPI21_LeScan(mScanCallback, 10000);
        } else {
            liteBluetooth.startLeScan(mLeScanCallback, 10000);
        }
    }
    void dev_scan_stop() {

        if (DevUtil.if_LeScanner_API_support()) {
            liteBluetooth.stopAPI21_LeScan(mScanCallback);
        } else {
            liteBluetooth.stopLeScan(mLeScanCallback);
        }
    }


    private void setupView() {
        listView = (ListView) findViewById(R.id.ble_list);
//        ListItems = new ArrayList<>();
        rowItems = new ArrayList<>();
        mAdapter = new ScanListAdapter(this, rowItems);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Item " + (position + 1) + ": " + rowItems.get(position),
                        Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected");
        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.menu_scan) {
            dev_scan();
            Toast.makeText(this, "scan start", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.menu_stop) {
            dev_scan_stop();
            Toast.makeText(this, "scan stop", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    //user implement this
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1  && resultCode == RESULT_OK){
            Log.d(TAG,"open bluetooth OK");
        }else if(requestCode == 1 && resultCode == RESULT_CANCELED){
            Toast.makeText(thisActivity,"please open bluetooth !! ",Toast.LENGTH_LONG).show();
            finish();
        }
    }


    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {

                    final String devName = device.getName();
                    final String devAddr = device.getAddress();

                    Log.i(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + Integer.toString(rssi));
                    if (null != devAddr) {
                        //if we don't have devAddr yet, add devaddr/rssi as key/value
                        if ((!mdevMap.containsKey(devAddr) ) ) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    RowItem rowItem = new RowItem(devName, devAddr, Integer.toString(rssi));
                                    rowItems.add(rowItem);
                                    Log.w(TAG,"add device");
                                    mdevMap.put(devAddr, rssi);
                                    (mAdapter).notifyDataSetChanged();
                                }
                            });
                        }
                        else if(mdevMap.containsKey(devAddr))
                        {
                            //update rssi if different
                            updateRssi(devAddr, rssi);
                        }
                    }
                }


    };

    void updateRssi(final String devAddr, final int rssi)
    {
        if(mdevMap.get(devAddr) != rssi)
        {
            mdevMap.put(devAddr,rssi);
            int listsize = rowItems.size();
            listsize = listsize -1;//index start from 0
            while(listsize >= 0)
            {
//                           runOnMainThread(updateAdapter_run);
                final int finalListsize = listsize;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(rowItems.get(finalListsize).getstrL2().equals(devAddr))
                        {
                            Log.w(TAG, "update rssi:" + rssi);
                            rowItems.get(finalListsize).setstrL3(Integer.toString(rssi));
                        }
                        (mAdapter).notifyDataSetChanged();
                    }
                });
                listsize--;
            }
        }
    }

    @RequiresApi(21)
    public class LeScannerAPI21 extends ScanCallback
    {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i(TAG, "Dev name:" + result.getDevice().getName() +
                    ", Dev address:" + result.getDevice().getAddress() +
                    ", Dev rssi:" + result.getRssi());

            if (DevUtil.if_BLE5_API_support()) {
                Log.d(TAG, "AdvertisingSid:" + result.getAdvertisingSid());
                Log.d(TAG, "getDataStatus:" + result.getDataStatus());
                Log.d(TAG, "getPeriodicAdvInterval:" + result.getPeriodicAdvertisingInterval());
                Log.d(TAG, "getPrimaryPhy:" + result.getPrimaryPhy());
                Log.d(TAG, "getSecondaryPhy:" + result.getSecondaryPhy());
                Log.d(TAG, "getTxPower:" + result.getTxPower());
                Log.d(TAG, "isConnectable:" + result.isConnectable());
                Log.d(TAG, "isLegacy:" + result.isLegacy());
            }
            final String devName = result.getDevice().getName();
            final String devAddr = result.getDevice().getAddress();
            final int rssi = result.getRssi();

            if (null != devAddr) {
                //if we don't have devAddr yet, add devaddr/rssi as key/value
                if ((!mdevMap.containsKey(devAddr) ) ) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RowItem rowItem = new RowItem(devName, devAddr, Integer.toString(rssi));
                            rowItems.add(rowItem);
                            Log.w(TAG,"add device");
                            mdevMap.put(devAddr, rssi);
                            (mAdapter).notifyDataSetChanged();
                        }
                    });
                }
                else if(mdevMap.containsKey(devAddr))
                {
                    //update rssi if different
                    updateRssi(devAddr, rssi);
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
