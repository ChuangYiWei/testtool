package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnny_wei.testtool.config.BLECbData;
import com.example.johnny_wei.testtool.config.IBLECallback;
import com.example.johnny_wei.testtool.config.globalConfig;

public class test_ble extends AppCompatActivity implements IBLECallback {
    String TAG = getClass().getSimpleName();
    Activity thisActivity = this;
    private LiteBle liteBluetooth;
    private Handler m_userHandler;

    //view
    static TextView tv_status;
    EditText ed_mac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ble);

        HandlerThread ht_thread = new HandlerThread("name");
        ht_thread.start();
        m_userHandler = new Handler(ht_thread.getLooper());

        liteBluetooth = new LiteBle(thisActivity);
        liteBluetooth.enableBluetoothIfDisabled(thisActivity, 1);
        liteBluetooth.setCallback(this);

        setupview();
    }
    private void setupview() {

        //ed_mac = findViewById(R.id.ed_mac);
        tv_status = findViewById(R.id.tv_status);
        ed_mac = findViewById(R.id.ed_mac);
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
        liteBluetooth.startLeScan(mLeScanCallback, 5000);
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

    public void clk_connect(View view) {
        liteBluetooth.connect(ed_mac.getText().toString());
    }

    public void clk_disconn(View view) {
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

    public void clk_notify(View view) {
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

    //implement callback
    @Override
    public void ConnectedCB() {
        Log.w(TAG,"OnConnect callback !!!");
    }

    @Override
    public void DisConnectCB() {
        Log.w(TAG,"disconnected callback !!!");
    }

    @Override
    public void ConnectFailCB(String reason)
    {
        Log.w(TAG,"ConnectFailCB callback !!! " + "reason:" + reason);
    }

    @Override
    public void readCharacteristicSuccessCB(String UUID, byte[] CBData)
    {
        Log.w(TAG,"read uuid " + UUID);
        printbytes(CBData);
    }

    @Override
    public void readCharacteristicFailCB(String UUID, int status)
    {
        Log.e(TAG,"readCharacteristicFailCB uuid:" + UUID);
    }


    @Override
    public void writeCharacteristicSuccessCB(String UUID, byte[] CBData)
    {
        Log.w(TAG,"write uuid " + UUID);
        printbytes(CBData);
    }

    @Override
    public void writeCharacteristicFailCB(String UUID, int status)
    {
        Log.e(TAG,"write uuid fail:" + UUID + "status:" + status);
    }

    @Override
    public void CharaValueChangedSuccessCB(String UUID, byte[] CBData)
    {
        Log.e(TAG,"CharaValueChanged uuid:" + UUID);
        printbytes(CBData);
    }


    @Override
    public void readDescSuccessCB(String UUID, byte[] CBData)
    {
        Log.w(TAG,"read Desc uuid " + UUID);
        printbytes(CBData);
    }

    @Override
    public void readDescFailCB(String UUID, int status)
    {
        Log.e(TAG,"read Desc fail uuid " + UUID);
    }


    @Override
    public void writeDescSuccessCB(String UUID, byte[] CBData)
    {
        Log.w(TAG,"write desc uuid " + UUID);
        printbytes(CBData);
    }

    @Override
    public void writeDescFailCB(String UUID, int status)
    {
        Log.e(TAG,"write desc fail uuid:" + UUID);
    }

    public void printbytes(byte bytes[]){
        String printStr = "";
        for (byte data : bytes) {
            printStr = printStr + String.format("%02x", data);
        }
        Log.d(TAG, "printbytes:" + printStr);
    }
}
