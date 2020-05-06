package com.BLE.johnny_wei.testtool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.BLE.johnny_wei.testtool.BLEutils.LiteBle;
import com.BLE.johnny_wei.testtool.adapter.ListItem;
import com.BLE.johnny_wei.testtool.adapter.RowItem;
import com.BLE.johnny_wei.testtool.adapter.ScanListAdapter;
import com.BLE.johnny_wei.testtool.utils.DevUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.BLE.johnny_wei.testtool.config.globalConfig.EXTRAS_DEVICE_ADDRESS;
import static com.BLE.johnny_wei.testtool.config.globalConfig.EXTRAS_DEVICE_NAME;

public class scan_list extends AppCompatActivity {

    String TAG = getClass().getSimpleName();
    ListView listView;
    List<ListItem> ListItems;
    List<RowItem> rowItems;
    ScanListAdapter mAdapter;
    private Context mContext;
    private ConstraintLayout mConstraintLayout;
    private PopupWindow mPopupWindow;
    private Button mButton;

    private String NEW_LINE = "\n";
    private  HashMap<String, Integer> mdevMap;
    Activity thisActivity = this;

    private LiteBle liteBluetooth;

    @RequiresApi(21)
    private ScanCallback mScanCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_list);
        mContext = this;
        liteBluetooth = new LiteBle(thisActivity);
        liteBluetooth.enableBluetoothIfDisabled(thisActivity, 1);
        mdevMap = new HashMap<String, Integer>();
        setupView();
//        dev_scan();
        showDevInfo();
        setupPopup();
    }

    @Override
    protected void onPause() {
        mdevMap.clear();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        dev_scan_stop();
        super.onDestroy();
    }

    void setupPopup()
    {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.devinfo,null);
                // Initialize a new instance of popup window
                if(mPopupWindow == null) {
                    mPopupWindow = new PopupWindow(
                            customView,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                }

                if(null == mPopupWindow)
                {
                    Log.e("jjj", "mPopupWindow is null");
                }
                try {
                    TextView tv = (TextView) customView.findViewById(R.id.tv_dev);
                    String str = GetDevString();
                    str += "        [--->click to cancel<---]\n";
                    tv.setBackgroundColor(0xff000000);
                    tv.setText(str);
                    tv.setTextColor(0xffaabb00);
                    tv.setTextSize(20);

                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            Log.e("jjj", "dismiss mPopupWindow ");
                            mPopupWindow.dismiss();
                        }
                    });

                    // Finally, show the popup window at the center location of root relative layout

                    mPopupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER, 0, 0);
                }catch (Exception e)
                {
                    Log.e("jjj", e.toString());
                }
            }
        });
    }

    private String GetDevString()
    {
        String str = "";
        str += "Brand----------:" + DevUtil.GetBrand() + NEW_LINE;
        str += "Device Name----:" + DevUtil.GetDeviceModel() + NEW_LINE;
        str += "Android version:" + DevUtil.GetSystemVersion()+ NEW_LINE;
        str += "MANUFACTURER---:" + Build.MANUFACTURER+ NEW_LINE;
        str += "SDK version----:" + DevUtil.GetVersionSDK()+ NEW_LINE;
        str += "Bluetooth Supported : "+ DevUtil.hasBLUETOOTH_Feature(this)+ NEW_LINE;
        str += "Bluetooth Low Energy Supported : " + DevUtil.hasBLUETOOTH_LE_Feature(this)+ NEW_LINE;
        if(DevUtil.if_BLE5_API_support()) {
            if(liteBluetooth.getBluetoothAdapter() != null) {
                str += "High speed(PHY 2M) Supported : " + liteBluetooth.getBluetoothAdapter().isLe2MPhySupported()+ NEW_LINE;
                str += "Long Range(PHY Coded) Supported : " + liteBluetooth.getBluetoothAdapter().isLeCodedPhySupported() + NEW_LINE;
                str += "Extended adv Supported : " + liteBluetooth.getBluetoothAdapter().isLeExtendedAdvertisingSupported() + NEW_LINE;
                str += "Periodic adv Supported : " + liteBluetooth.getBluetoothAdapter().isLePeriodicAdvertisingSupported() + NEW_LINE;
                str += "Max adv Data Length : " + liteBluetooth.getBluetoothAdapter().getLeMaximumAdvertisingDataLength() + NEW_LINE;
            }
        }
        return str;
    }

    void showDevInfo()
    {
        Log.d(TAG, "Brand:" + DevUtil.GetBrand());
        Log.d(TAG, "Device Name:" + DevUtil.GetDeviceModel());
        Log.d(TAG, "Android version:" + DevUtil.GetSystemVersion());
        Log.d(TAG, "MANUFACTURER:" + Build.MANUFACTURER);
        Log.d(TAG, "SDK version:" + DevUtil.GetVersionSDK());

        Log.d(TAG,"Bluetooth Supported : "+ DevUtil.hasBLUETOOTH_Feature(this));
        Log.d(TAG,"Bluetooth Low Energy Supported : " + DevUtil.hasBLUETOOTH_LE_Feature(this));
    }

    void dev_scan() {

        if (DevUtil.if_LeScanner_API_support()) {
            mScanCallback = new LeScannerAPI21();
            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build();
            liteBluetooth.startAPI21_LeScan(mScanCallback, null, settings, 10000);
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
        mButton = (Button) findViewById(R.id.btn_dev);
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

                startNextActivity(position);
            }
        });
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.scan_list_parent);
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

    private void startNextActivity(int position) {

        dev_scan_stop();

        //go to different activity
        Intent intent;
        intent = new Intent(thisActivity, connected_list.class);

        intent.putExtra(EXTRAS_DEVICE_NAME, rowItems.get(position).getstrL1());
        intent.putExtra(EXTRAS_DEVICE_ADDRESS, rowItems.get(position).getstrL2());
        startActivity(intent);
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
                                    (mAdapter).notifyDataSetChanged();
                                    mdevMap.put(devAddr, rssi);
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

            int listsize = rowItems.size();
            listsize = listsize -1;//index start from 0
            while(listsize >= 0)
            {
                final int finalListsize = listsize;
                if(rowItems.get(finalListsize).getstrL2().equals(devAddr)) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.w(TAG, "update rssi:" + rssi);
                            rowItems.get(finalListsize).setstrL3(Integer.toString(rssi));
                            (mAdapter).notifyDataSetChanged();
                            mdevMap.put(devAddr,rssi);
                        }
                    });
                }
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
                            (mAdapter).notifyDataSetChanged();
                            mdevMap.put(devAddr, rssi);
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
