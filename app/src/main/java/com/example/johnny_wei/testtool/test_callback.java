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

/*
public class test_callback extends AppCompatActivity {
    TextView cb;
    Activity thisActivity = this;
    private class caller extends absClass {
        Worker m_worker;
        caller()
        {
            m_worker = new Worker(this);
            m_worker.startWork();
        }
        @Override
        public void callback(final MyObj obj) {
            Log.d("caller","[test_callback] callback been called , data is :" + obj.data);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cb.setText("data is :" + obj.data);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_callback);
        cb=findViewById(R.id.tv_cb);
        caller a = new caller();
    }

 */
