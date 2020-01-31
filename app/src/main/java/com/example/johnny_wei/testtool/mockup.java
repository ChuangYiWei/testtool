package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.johnny_wei.testtool.BLEutils.LiteBle;

public class mockup extends AppCompatActivity {
    //BLE
    private LiteBle liteBluetooth;
    private Activity mActivity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mockup);

        liteBluetooth = new LiteBle(mActivity);
        startNextActivity();
    }

    private void startNextActivity() {
        //go to scan page
        Intent intent;
        intent = new Intent(mActivity, test_ble.class);
        startActivity(intent);
    }
}
