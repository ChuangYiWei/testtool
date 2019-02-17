package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class test_callback extends AppCompatActivity {
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
}
