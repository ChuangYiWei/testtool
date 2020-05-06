package com.BLE.johnny_wei.testtool;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class handler extends AppCompatActivity {
    private TextView txt;
    private static final int msgKey1 = 1;
    Handler user_handler;
    String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        HandlerThread ht = new HandlerThread("userdefine");
        ht.start();
        user_handler = new Handler(ht.getLooper());

        txt = (TextView) findViewById(R.id.txt);

        Thread t = new Thread(runnable);//will run not in ui thread
        t.start();

        if(Looper.myLooper() == Looper.getMainLooper())
        {
            Log.d(TAG, "in onCreate: run on ui thread");
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(3000);
                    Message msg = Message.obtain();
                    msg.what = msgKey1;
                    msg.obj = "OK";
                    msg.arg1 = 99;
                    mHandler.sendMessage(msg);
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        Log.d("jjj", "sendMessagerun on ui thread");
                    } else {
                        Log.d("jjj", "sendMessage not run on ui thread");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } while (true);
        }
    };

    @SuppressLint("HandlerLeak")
    //will run on ui thread from current thread
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        Log.d(TAG, "handleMessage run on ui thread");
                    } else {
                        Log.d(TAG, "handleMessage not run on ui thread");
                    }

                    Log.d(TAG, "handleMessage got" + String.valueOf(msg.arg1));
                    String x = (String) msg.obj;
                    txt.setText(String.valueOf(x));
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler mHandler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    Log.d(TAG, "mHandler2 handleMessage got" + String.valueOf(msg.arg1));
                    String x = (String) msg.obj;
                    txt.setText(String.valueOf(x));
                    break;
                default:
                    break;
            }
        }
    };

    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // update TextView here!
                            long time = System.currentTimeMillis();
                            Date date = new Date(time);
                            SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒 EEE");
                            txt.setText(format.format(date));
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

    private Runnable r0 = new Runnable() {
        //工作內容寫進run(),繁雜的工作可以給另一個特約工人做
        public void run() {
            while (true) {
                try {
                    if(Looper.myLooper() == Looper.getMainLooper())
                    {
                        Log.d("jjj r0", "run on ui thread");
                    }
                    Log.d("jjj r0", Thread.currentThread().getName());
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(10000); //每隔1秒顯示一次
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void click_runthread(View view) {
        user_handler.post(r0);
    }

}
