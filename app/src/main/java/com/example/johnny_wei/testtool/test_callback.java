package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class test_callback extends AppCompatActivity implements ICallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_callback);

        caller a = new caller();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    @Override
    public void callback(MyObj o) {

    }
}
