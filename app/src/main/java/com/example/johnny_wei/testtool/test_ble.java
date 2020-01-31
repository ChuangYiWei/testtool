package com.example.johnny_wei.testtool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.BLEutils.LiteBle;
import com.example.johnny_wei.testtool.BLEutils.callback.LiteBLECallback;
import com.example.johnny_wei.testtool.config.globalConfig;
import com.example.johnny_wei.testtool.utils.DevUtil;
import com.example.johnny_wei.testtool.utils.Permission;


import java.util.List;

import static android.bluetooth.BluetoothDevice.PHY_LE_2M;
import static android.bluetooth.BluetoothDevice.PHY_OPTION_NO_PREFERRED;
import static com.example.johnny_wei.testtool.BLEutils.LiteBle.StaticLiteble;
import static com.example.johnny_wei.testtool.config.globalConfig.BLE5_API_LEVEL;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_COARSE_LOCATION;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;


public class test_ble extends AppCompatActivity  {
    String TAG = getClass().getSimpleName();
    Activity thisActivity = this;
    private LiteBle liteBluetooth;
    private LiteBle liteBluetooth_DUT;
    private Handler m_userHandler;

    //view
    static TextView tv_status;
    EditText ed_mac;
    EditText dut_ed_mac;
    GattCB gatt_cb;
    DUT_GattCB dut_gatt_cb;
    private PopupWindow mPopupWindow;

    @RequiresApi(21)
    private ScanCallback mScanCallback;

    private ConstraintLayout mConstraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_test_ble);

        HandlerThread ht_thread = new HandlerThread("name");
        ht_thread.start();
        m_userHandler = new Handler(ht_thread.getLooper());
        liteBluetooth_DUT = StaticLiteble;
        liteBluetooth = new LiteBle(thisActivity,1);
        liteBluetooth.enableBluetoothIfDisabled(thisActivity, 1);
        gatt_cb = new GattCB();
        dut_gatt_cb = new DUT_GattCB();

        Permission.Req_Access_Coarse_Permissions(this);

        setupview();

        if(DevUtil.if_LeScanner_API_support())
        {
            mScanCallback = new LeScannerAPI21();
        }

    }
    private void setupview() {

        //ed_mac = findViewById(R.id.ed_mac);
        tv_status = findViewById(R.id.tv_status);
        ed_mac = findViewById(R.id.mb_ed_mac);
        dut_ed_mac= findViewById(R.id.dut_edt_mac);
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.test_ble_parent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"onOptionsItemSelected");
        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.action_settings) {
            // 按下「設定」要做的事
            Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
            showPopupWindow();
            return true;
        }
        else if (id == R.id.action_help) {
            // 按下「使用說明」要做的事
            Toast.makeText(this, "使用說明", Toast.LENGTH_SHORT).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    void showPopupWindow()
    {
        LayoutInflater inflater = (LayoutInflater) test_ble.this.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.custom_layout,null);
        // Initialize a new instance of popup window
        if(mPopupWindow == null) {
            Log.e(TAG, "create new  mPopupWindow");
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
            TextView tv = (TextView) customView.findViewById(R.id.tv);
            String str = "THIS　is Friday Night !!!!";
            tv.setText(str);
            tv.setTextColor(0xffaabb00);
            tv.setTextSize(20);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Dismiss the popup window
                    Log.e(TAG, "dismiss mPopupWindow ");
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

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi,
                                     byte[] scanRecord) {
                    String name = device.getName();
                    if (null != name) {
                        Log.i(TAG, "device name:" + device.getName() + ", device address:" + device.getAddress() + Integer.toString(rssi));
                    }

    }};

    public void clk_dut_conn(View view) {
        liteBluetooth_DUT.connect(dut_ed_mac.getText().toString());
    }

    public void clk_dut_disconn(View view) {
        liteBluetooth_DUT.disconnect();
    }

    public void clk_dut_state(View view) {
        liteBluetooth_DUT.isMainThread();
        int state = liteBluetooth_DUT.getConnectionState();
        tv_status.setText(String.valueOf(state));
        liteBluetooth_DUT.printServices(liteBluetooth_DUT.getBluetoothGatt());
    }


    @RequiresApi(21)
    public class LeScannerAPI21 extends ScanCallback
    {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {

//            if( result == null
//                    || result.getDevice() == null
//                    || TextUtils.isEmpty(result.getDevice().getName()) )
//                return;
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
            //StringBuilder builder = new StringBuilder( result.getDevice().getName() );

            //builder.append("\n").append(new String(result.getScanRecord().getServiceData(result.getScanRecord().getServiceUuids().get(0)), Charset.forName("UTF-8")));
            //mText.setText(builder.toString());
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


    //user implement this to check
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1  && resultCode == RESULT_OK){
            Log.d(TAG,"open bluetooth OK");
        }else if(requestCode == 1 && resultCode == RESULT_CANCELED){
            Toast.makeText(test_ble.this,"please open bluetooth !! ",Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public void clk_scan(View view) {
        if (DevUtil.if_LeScanner_API_support()) {
            liteBluetooth.startAPI21_LeScan(mScanCallback, 5000);
        } else {
            liteBluetooth.startLeScan(mLeScanCallback, 5000);
        }
    }

    public void clk_stopscan(View view) {
        liteBluetooth.stopLeScan(mLeScanCallback);
    }

    public void clk_getState(View view) {
        liteBluetooth.isMainThread();
        int state = liteBluetooth.getConnectionState();
        tv_status.setText(String.valueOf(state));
        liteBluetooth.printServices(liteBluetooth.getBluetoothGatt());
    }

    public void clk_MB_connect(View view) {
        liteBluetooth.connect(ed_mac.getText().toString());
    }

    public void clk_MB_disconn(View view) {
        liteBluetooth.disconnect();
    }

    public void clk_reboot_BT(View view) {
        m_userHandler.post(new Runnable() {
           @Override
           public void run() {
               liteBluetooth.rebootBluetooth();
           }
        });
    }

    public void clk_enable_notify(View view) {
        liteBluetooth.enableCharacteristicNotify(
                globalConfig.UUID_SERVICE,
                globalConfig.UUID_NOTIFY_CHARA,
                globalConfig.UUID_WRITE_DESCRIPTOR);
    }

    public void clk_write_data(View view) {
        byte bytes[] = {0x0, 0x9, 0x9};
        liteBluetooth.writeDataToCharacteristic(
                globalConfig.UUID_SERVICE,
                globalConfig.UUID_WRITE_CHARA,
                bytes
        );
    }

    public void clk_readData(View view) {
        liteBluetooth.readCharacteristic(globalConfig.UUID_BATTERY_SERVICE, globalConfig.UUID_BATTERY_LEVEL_CHARA);
    }

    public void clk_dut_enable_notify(View view) {
        liteBluetooth_DUT.enableCharacteristicNotify(
                globalConfig.UUID_SERVICE,
                globalConfig.UUID_NOTIFY_CHARA,
                globalConfig.UUID_WRITE_DESCRIPTOR);
    }

    public void clk_dut_write_data(View view) {
        byte bytes[] = {0x1, 0x2, 0x3};
        liteBluetooth_DUT.writeDataToCharacteristic(
                globalConfig.UUID_SERVICE,
                globalConfig.UUID_WRITE_CHARA,
                bytes
        );
    }

    public void clk_dut_readData(View view) {
        liteBluetooth_DUT.readCharacteristic(globalConfig.UUID_BATTERY_SERVICE, globalConfig.UUID_BATTERY_LEVEL_CHARA);
    }

    @TargetApi(BLE5_API_LEVEL)
    public void clk_readphy(View view) {
        if(DevUtil.if_BLE5_API_support()) {
            m_userHandler.post(readphy_run);
            m_userHandler.post(setPreferredPhy_run);
        }
        else
        {
            tv_status.setText("not support, API level < 26");
        }
    }

    @TargetApi(BLE5_API_LEVEL)
    Runnable readphy_run = new Runnable() {
        @Override public void run() {
            Log.d(TAG,"gatt readPhy");
            liteBluetooth.getBluetoothGatt().readPhy();
        }
    };

    @TargetApi(BLE5_API_LEVEL)
    Runnable setPreferredPhy_run= new Runnable() {
        @Override public void run() {
            Log.d(TAG,"set prefer phy");
            liteBluetooth.getBluetoothGatt().setPreferredPhy(PHY_LE_2M,PHY_LE_2M,PHY_OPTION_NO_PREFERRED);
        }
    };

    @TargetApi(BLE5_API_LEVEL)
    Runnable BLE5_support_run= new Runnable() {
        @Override public void run() {
            if(liteBluetooth.getBluetoothAdapter().isLe2MPhySupported()) {
                Log.d(TAG, "This device support 2M");
            }
            if(liteBluetooth.getBluetoothAdapter().isLeCodedPhySupported()) {
                Log.d(TAG, "LeCodedPhySupported");
            }
            if(liteBluetooth.getBluetoothAdapter().isLeExtendedAdvertisingSupported()) {
                Log.d(TAG, "LeExtendedAdvertisingSupported");
            }
            if(liteBluetooth.getBluetoothAdapter().isLePeriodicAdvertisingSupported()) {
                Log.d(TAG, "LePeriodicAdvertisingSupported");
            }
            int maxAdvLength = liteBluetooth.getBluetoothAdapter().getLeMaximumAdvertisingDataLength();
                Log.d(TAG, "maxAdvLength is : " + maxAdvLength);
        }
    };

    @TargetApi(BLE5_API_LEVEL)
    public void clk_support(View view) {
        if(DevUtil.if_BLE5_API_support()) {
            m_userHandler.post(BLE5_support_run);
        }
        else
        {
            tv_status.setText("not support, API level < 26");
        }
    }

    private class GattCB extends LiteBLECallback {
        GattCB() {
            //this reference(pointer) is GattCB
            liteBluetooth.listenBLECallback(this,TAG);
        }
        //implement callback
        @Override
        public void ConnectedCB() {
            Log.w(TAG, "MB OnConnect callback !!!");
        }

        @Override
        public void DisConnectCB() {
            Log.w(TAG, "MB disconnected callback !!!");
        }

        @Override
        public void ConnectFailCB(String reason) {
            Log.w(TAG, "MB ConnectFailCB callback !!! " + "reason:" + reason);
        }

        @Override
        public void readCharacteristicSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "MB read uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void readCharacteristicFailCB(String UUID, int status) {
            Log.e(TAG, "MB readCharacteristicFailCB uuid:" + UUID);
        }


        @Override
        public void writeCharacteristicSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "MB write uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void writeCharacteristicFailCB(String UUID, int status) {
            Log.e(TAG, "MB write uuid fail:" + UUID + "status:" + status);
        }

        @Override
        public void CharaValueChangedSuccessCB(String UUID, byte[] CBData) {
            Log.e(TAG, "MB CharaValueChanged uuid:" + UUID);
            printbytes(CBData);
        }


        @Override
        public void readDescSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "MB read Desc uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void readDescFailCB(String UUID, int status) {
            Log.e(TAG, "MB read Desc fail uuid " + UUID);
        }


        @Override
        public void writeDescSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "MB write desc uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void writeDescFailCB(String UUID, int status) {
            Log.e(TAG, "MB write desc fail uuid:" + UUID);
        }

    }

    private class DUT_GattCB extends LiteBLECallback {
        DUT_GattCB() {
            //this reference(pointer) is GattCB
            liteBluetooth_DUT.listenBLECallback(this,TAG);
        }
        //implement callback
        @Override
        public void ConnectedCB() {
            Log.w(TAG, "DUT_GattCB OnConnect callback !!!");
        }

        @Override
        public void DisConnectCB() {
            Log.w(TAG, "DUT_GattCB disconnected callback !!!");
        }

        @Override
        public void ConnectFailCB(String reason) {
            Log.w(TAG, "DUT_GattCB ConnectFailCB callback !!! " + "reason:" + reason);
        }

        @Override
        public void readCharacteristicSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "DUT read uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void readCharacteristicFailCB(String UUID, int status) {
            Log.e(TAG, "DUT readCharacteristicFailCB uuid:" + UUID);
        }


        @Override
        public void writeCharacteristicSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "DUT write uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void writeCharacteristicFailCB(String UUID, int status) {
            Log.e(TAG, "DUT write uuid fail:" + UUID + "status:" + status);
        }

        @Override
        public void CharaValueChangedSuccessCB(String UUID, byte[] CBData) {
            Log.e(TAG, "DUT CharaValueChanged uuid:" + UUID);
            printbytes(CBData);
        }


        @Override
        public void readDescSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "DUT read Desc uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void readDescFailCB(String UUID, int status) {
            Log.e(TAG, "DUT read Desc fail uuid " + UUID);
        }


        @Override
        public void writeDescSuccessCB(String UUID, byte[] CBData) {
            Log.w(TAG, "DUT write desc uuid " + UUID);
            printbytes(CBData);
        }

        @Override
        public void writeDescFailCB(String UUID, int status) {
            Log.e(TAG, "DUT write desc fail uuid:" + UUID);
        }

    }


    public void printbytes(byte bytes[]){
        String printStr = "";
        for (byte data : bytes) {
            printStr = printStr + String.format("%02x", data);
        }
        Log.d(TAG, "printbytes:" + printStr);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.d("Permission", "onRequestPermissionsResult");
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
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "storage location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("storage access has not been granted, debug log disable");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
            }
            break;
        }

    }
}
