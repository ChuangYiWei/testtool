package com.example.johnny_wei.testtool;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.provider.Settings;
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

import com.example.johnny_wei.testtool.BLEutils.BLE_ACTIVITY.ble_dev_select;
import com.example.johnny_wei.testtool.BLEutils.LiteBle;
import com.example.johnny_wei.testtool.BLEutils.callback.LiteBLECallback;
import com.example.johnny_wei.testtool.config.globalConfig;
import com.example.johnny_wei.testtool.utils.DevUtil;
import com.example.johnny_wei.testtool.utils.Permission;


import java.util.List;

import static android.bluetooth.BluetoothDevice.PHY_LE_1M;
import static android.bluetooth.BluetoothDevice.PHY_LE_2M;
import static android.bluetooth.BluetoothDevice.PHY_LE_CODED;
import static android.bluetooth.BluetoothDevice.PHY_LE_CODED_MASK;
import static android.bluetooth.BluetoothDevice.PHY_OPTION_NO_PREFERRED;
import static android.bluetooth.BluetoothDevice.PHY_OPTION_S2;
import static android.bluetooth.BluetoothDevice.PHY_OPTION_S8;
import static com.example.johnny_wei.testtool.config.globalConfig.BLE5_API_LEVEL;
import static com.example.johnny_wei.testtool.config.globalConfig.DUT;
import static com.example.johnny_wei.testtool.config.globalConfig.MB;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_BACKGROUND_LOCATION;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_COARSE_LOCATION;
import static com.example.johnny_wei.testtool.config.globalConfig.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;
import static com.example.johnny_wei.testtool.config.globalConfig.REQUEST_CODE_LOCATION_SETTINGS;
import static com.example.johnny_wei.testtool.config.globalConfig.REQ_CODE_BLE_DEV_ACT;
import static com.example.johnny_wei.testtool.config.globalConfig.UUID_NOTIFY_CHARA;
import static com.example.johnny_wei.testtool.config.globalConfig.UUID_SERVICE;
import static com.example.johnny_wei.testtool.config.globalConfig.UUID_WRITE_CHARA;
import static com.example.johnny_wei.testtool.config.globalConfig.UUID_WRITE_DESCRIPTOR;
import static com.example.johnny_wei.testtool.utils.commonUtil.intToByteArray;
import static com.example.johnny_wei.testtool.utils.strUtil.bytes2String;


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

    int mb_tx_pkt_count = 0;
    int mb_rx_pkt_count = 0;
    int mb_max_pkt_count = 0x7FFFFFFF;
    int dut_tx_pkt_count = 0;
    int dut_rx_pkt_count = 0;
    int dut_max_pkt_count = 0x7FFFFFFF;

    int max_mtu_length = 23;
    byte[] tx_byte = new byte[255] ;
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

        //DUT
        liteBluetooth_DUT = new LiteBle(thisActivity, DUT);//0:DUT
        liteBluetooth_DUT.enableBluetoothIfDisabled(thisActivity, 1);

        //MB
        liteBluetooth = new LiteBle(MB);//1:MB

        gatt_cb = new GattCB();
        dut_gatt_cb = new DUT_GattCB();
        setupview();


        if( Build.VERSION.SDK_INT >= 29) {
//            Log.d(TAG,"Req_Access_Access_Background_Location");
//            int permission = thisActivity.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//            //未取得權限，向使用者要求允許權限
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                thisActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_BACKGROUND_LOCATION);
//            }

//            int permission = thisActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//            //未取得權限，向使用者要求允許權限
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                thisActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
//            }
        }
        else
        {
//            Permission.Req_Access_Coarse_Permissions(this);
        }


        if(Build.VERSION.SDK_INT >= 23) {
            int permission = thisActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            //未取得權限，向使用者要求允許權限
            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.w(TAG, "requset ACCESS_FINE_LOCATION");
                thisActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
            }
        }

        if(DevUtil.if_LeScanner_API_support())
        {
            mScanCallback = new LeScannerAPI21();
        }

        /**go to select ble device*/
//        Intent go2intent = new Intent(this, ble_dev_select.class);
//        this.startActivityForResult(go2intent, REQ_CODE_BLE_DEV_ACT);


//        if (!isLocationEnable(thisActivity)) {
//            setLocationService();
//        }

    }

    /**
     * Location service if enable
     *
     * @param context
     * @return location is enable if return true, otherwise disable.
     */
    public static final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }

    private void setLocationService() {
        Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
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
        //liteBluetooth_DUT.connect(dut_ed_mac.getText().toString());
        liteBluetooth_DUT.connectRetry(dut_ed_mac.getText().toString());
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

    public void clk_mb_loop(View view) {
        //先reset再傳
        mb_reset2Default();
        byte tx[] = intToByteArray(mb_tx_pkt_count);
        mb_send_data(UUID_SERVICE,UUID_WRITE_CHARA,tx);
    }

    public void clk_dut_loop(View view) {
        //先reset再傳
        dut_reset2Default();
        byte tx[] = intToByteArray(dut_tx_pkt_count);
        dut_send_data(UUID_SERVICE,UUID_WRITE_CHARA,tx);
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
        Log.d(TAG,"requestCode:"+requestCode);
        //request code is back from the class we call
        if(requestCode == 1  && resultCode == RESULT_OK){
            Log.d(TAG,"open bluetooth OK");
        }else if(requestCode == REQ_CODE_BLE_DEV_ACT && resultCode == RESULT_OK){
            Toast.makeText(test_ble.this,data.getExtras().getString("EXTRAS_DEVICE_ADDRESS"),Toast.LENGTH_LONG).show();
            Log.d(TAG,"get device name:"+data.getExtras().getString("EXTRAS_DEVICE_ADDRESS"));
        }else if(requestCode == REQUEST_CODE_LOCATION_SETTINGS){
            if (isLocationEnable(this)) {
                Log.d(TAG,"gps enanle");
            } else {
                Toast.makeText(test_ble.this,"please enable location service",Toast.LENGTH_LONG).show();
                Log.w(TAG,"gps not enanle");
            }
        }
        else if(requestCode == 1 && resultCode == RESULT_CANCELED){
            Toast.makeText(test_ble.this,"please open bluetooth !! ",Toast.LENGTH_LONG).show();
            finish();
        }
    }


    public void clk_scan(View view) {
        // Since Android 6.0 we need to obtain either Manifest.permission.ACCESS_COARSE_LOCATION or Manifest.permission.ACCESS_FINE_LOCATION to be able to scan for
        // Bluetooth LE devices. This is related to beacons as proximity devices.
        // On API older than Marshmallow the following code does nothing.
//        if(Build.VERSION.SDK_INT >= 23) {
//
//            int permission = thisActivity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                Log.w(TAG, "requset ACCESS_COARSE_LOCATION");
//                thisActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
//            }
//        }

//        if( Build.VERSION.SDK_INT >= 29) {
//            int permission = thisActivity.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION);
//            //未取得權限，向使用者要求允許權限
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG,"Req_Access_Access_Background_Location");
//                thisActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_BACKGROUND_LOCATION);
//            }
//        }
//        if(Build.VERSION.SDK_INT >= 23) {
//            int permission = thisActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//            //未取得權限，向使用者要求允許權限
//            if (permission != PackageManager.PERMISSION_GRANTED) {
//                Log.w(TAG, "requset ACCESS_FINE_LOCATION");
//                thisActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
//            }
//        }

        if (!isLocationEnable(thisActivity)) {
            setLocationService();
        }
        else {
            if (DevUtil.if_LeScanner_API_support()) {
                ScanSettings settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                liteBluetooth.startAPI21_LeScan(mScanCallback, null, settings, 5000);
            } else {
                liteBluetooth.startLeScan(mLeScanCallback, 5000);
            }
        }
    }

    public void clk_stopscan(View view) {
        if (DevUtil.if_LeScanner_API_support()) {
            liteBluetooth.stopAPI21_LeScan(mScanCallback);
        } else {
            liteBluetooth.stopLeScan(mLeScanCallback);
        }

    }

    public void clk_getState(View view) {
        liteBluetooth.isMainThread();
        int state = liteBluetooth.getConnectionState();
        tv_status.setText(String.valueOf(state));
        liteBluetooth.printServices(liteBluetooth.getBluetoothGatt());

    }


    public void clk_MB_connect(View view) {
        liteBluetooth.connectRetry(ed_mac.getText().toString());
//        liteBluetooth.connect(ed_mac.getText().toString());
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
                UUID_SERVICE,
                UUID_NOTIFY_CHARA,
                UUID_WRITE_DESCRIPTOR);
    }

    int idx=0;
    public void clk_write_data(View view) {
//        byte bytes[] = {0x0, 0x9, 0x9};
//        liteBluetooth.writeDataToCharacteristic(
//                UUID_SERVICE,
//                UUID_WRITE_CHARA,
//                bytes
//        );

        Log.d(TAG,"max_mtu_length:" + max_mtu_length);
        set_same_byte(tx_byte,max_mtu_length-3,idx);
        Log.d(TAG,"set same byte result:" + bytes2String(tx_byte));
        liteBluetooth.ble_write_cmd(MB, UUID_SERVICE, UUID_WRITE_CHARA, max_mtu_length, tx_byte);
        idx++;
    }

    public void clk_readData(View view) {
        liteBluetooth.readCharacteristic(globalConfig.UUID_BATTERY_SERVICE, globalConfig.UUID_BATTERY_LEVEL_CHARA);
    }

    public void clk_dut_enable_notify(View view) {
        liteBluetooth_DUT.enableCharacteristicNotify(
                UUID_SERVICE,
                UUID_NOTIFY_CHARA,
                UUID_WRITE_DESCRIPTOR);
    }

    public void clk_dut_write_data(View view) {
        byte bytes[] = {0x1, 0x2, 0x3};
        liteBluetooth_DUT.writeDataToCharacteristic(
                UUID_SERVICE,
                UUID_WRITE_CHARA,
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

    @TargetApi(21)
    Runnable reqMTU_run = new Runnable() {
        @Override public void run() {
            Log.w(TAG,"reqMTU_run");
            int n = 200;
            liteBluetooth.getBluetoothGatt().requestMtu(n);
        }
    };

    @TargetApi(BLE5_API_LEVEL)
    Runnable setPreferredPhy_run= new Runnable() {
        @Override public void run() {
            Log.d(TAG,"set prefer phy");
//            liteBluetooth.getBluetoothGatt().setPreferredPhy(PHY_LE_2M,PHY_LE_2M,PHY_OPTION_NO_PREFERRED);
            //liteBluetooth.getBluetoothGatt().setPreferredPhy(PHY_LE_1M,PHY_LE_1M,PHY_OPTION_NO_PREFERRED);
//            liteBluetooth.getBluetoothGatt().setPreferredPhy(PHY_LE_CODED_MASK,PHY_LE_CODED_MASK,PHY_OPTION_S2);
            liteBluetooth.getBluetoothGatt().setPreferredPhy(PHY_LE_CODED_MASK,PHY_LE_CODED_MASK,PHY_OPTION_S8);
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

    @TargetApi(21)
    public void clk_support(View view) {
        m_userHandler.post(reqMTU_run);
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
        public void SrvDiscoverSuccessCB() {
            Log.w(TAG, "SrvDiscoverSuccessCB callback !!!");
            liteBluetooth.enableCharacteristicNotify(UUID_SERVICE,UUID_NOTIFY_CHARA,UUID_WRITE_DESCRIPTOR);
            super.SrvDiscoverSuccessCB();
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
            //Log.w(TAG, "MB write uuid " + UUID);
            //printbytes(CBData);
        }

        @Override
        public void writeCharacteristicFailCB(String UUID, int status) {
            Log.e(TAG, "MB write uuid fail:" + UUID + "status:" + status);
        }

        @Override
        public void CharaValueChangedSuccessCB(String UUID, byte[] CBData) {
            Log.e(TAG, "MB CharaValueChanged uuid:" + UUID);
            printbytes(CBData);
//            byte bytes[] = {(byte)0x01, 0x02, 0x03};
//            liteBluetooth.writeDataToCharacteristic(
//                    UUID_SERVICE,
//                    UUID_WRITE_CHARA,
//                    bytes);

//            mb_tx_pkt_count++;
//            mb_rx_pkt_count++;
//            if (mb_max_pkt_count == mb_tx_pkt_count) {
//                dut_reset2Default();
//            }
//            byte tx[] = intToByteArray(mb_tx_pkt_count);
//            Log.d(TAG,"send data : 0x" + String.format("%08X", mb_tx_pkt_count));
//            mb_send_data(UUID_SERVICE, UUID_WRITE_CHARA, tx);
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

        @Override
        public void onMtuChangedSuccessCB(int mtu) {
            super.onMtuChangedSuccessCB(mtu);
            max_mtu_length = mtu;
            Log.w(TAG, "MB onMtuChangedSuccessCB:" + mtu);
        }

        @Override
        public void onMtuChangedFailCB(int mtu) {
            super.onMtuChangedFailCB(mtu);
            Log.w(TAG, "MB onMtuChangedFailCB:" + mtu);
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
        public void SrvDiscoverSuccessCB() {
            Log.w(TAG, "DUT SrvDiscoverSuccessCB callback !!!");
            liteBluetooth_DUT.enableCharacteristicNotify(UUID_SERVICE,UUID_NOTIFY_CHARA,UUID_WRITE_DESCRIPTOR);
            super.SrvDiscoverSuccessCB();
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
//            Log.w(TAG, "DUT write uuid " + UUID);
//            printbytes(CBData);
        }

        @Override
        public void writeCharacteristicFailCB(String UUID, int status) {
            Log.e(TAG, "DUT write uuid fail:" + UUID + "status:" + status);
        }

        @Override
        public void CharaValueChangedSuccessCB(String UUID, byte[] CBData) {
            Log.e(TAG, "DUT CharaValueChanged uuid:" + UUID);
            printbytes(CBData);
//            byte bytes[] = {(byte)0x04, 0x05, 0x06};
//            liteBluetooth_DUT.writeDataToCharacteristic(
//                    UUID_SERVICE,
//                    UUID_WRITE_CHARA,
//                    bytes);
            // prepare send next
//            dut_tx_pkt_count++;
//            dut_rx_pkt_count++;
//            if (dut_max_pkt_count == dut_tx_pkt_count) {
//                dut_reset2Default();
//            }
//            byte tx[] = intToByteArray(dut_tx_pkt_count);
//            Log.d(TAG,"send data : 0x" + String.format("%08X", dut_tx_pkt_count));
//            dut_send_data(UUID_SERVICE, UUID_WRITE_CHARA, tx);
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

    void mb_reset2Default()
    {
        mb_tx_pkt_count = 0;
        mb_rx_pkt_count = 0;
    }

    void dut_reset2Default()
    {
        dut_tx_pkt_count = 0;
        dut_rx_pkt_count = 0;
    }

    //write data to specific uuid
    private void mb_send_data(String service_uuid,
                           String chara_uuid,
                           final byte[] bytes)
    {
        liteBluetooth.writeDataToCharacteristic(service_uuid, chara_uuid, bytes);
    }

    private void dut_send_data(String service_uuid,
                              String chara_uuid,
                              final byte[] bytes)
    {
        liteBluetooth_DUT.writeDataToCharacteristic(service_uuid, chara_uuid, bytes);
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
            case PERMISSION_REQUEST_BACKGROUND_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "BACKGROUND_LOCATION permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since BACKGROUND_LOCATION access has not been granted, this app will not be able to discover beacons when in the background.");
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
            case PERMISSION_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "PERMISSION_REQUEST_ACCESS_FINE_LOCATION permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since PERMISSION_REQUEST_ACCESS_FINE_LOCATION access has not been granted, this app will not be able to discover beacons when in the background.");
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

    void set_same_byte(byte[] bytes,int size, int set_value)
    {

        for(int i=0;i<size;i++)
        {
            bytes[i] = (byte)set_value;
        }
    }
}
