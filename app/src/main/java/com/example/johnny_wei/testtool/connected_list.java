package com.example.johnny_wei.testtool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.johnny_wei.testtool.LiteBle.StaticLiteble;
import static com.example.johnny_wei.testtool.config.globalConfig.EXTRAS_DEVICE_ADDRESS;

public class connected_list extends AppCompatActivity {
    private String mDeviceAddress = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected_list);
        setupView();
        StaticLiteble.connect(mDeviceAddress);
        StaticLiteble.printServices(StaticLiteble.getBluetoothGatt());
    }

    private void setupView() {
        Intent intent = getIntent();
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
    }
}
