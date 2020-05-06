package com.example.johnny_wei.testtool;

import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class time extends AppCompatActivity {
    public int tt= 0;
    TextView tv_time;
    String TAG;
    long new_time =0;
    long current_time =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        TAG = getClass().getSimpleName();
        tv_time = findViewById(R.id.tv_time);

        //CountTimeThread();
//        updateTimeThread();
        check_unix_time();

    }

    void check_unix_time()
    {
        long unixTime = System.currentTimeMillis() / 1000L;
        Log.w(TAG, "unixTime is:" + unixTime);
        SystemClock.sleep(3000);
        unixTime = System.currentTimeMillis() / 1000L;
        Log.w(TAG, "unixTime after 3 secs is:" + unixTime);
        //讀取Unix時間
        Date dt=new Date(unixTime);//UnixTime毫秒
        //定義時間格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //轉換字串
        String time=sdf.format(new Date());
        Log.w(TAG, "date time is:" + time);
        Log.d(TAG,"["+ time +"]" + "=>interval between is " + unixTime);
    }

    private void CountTimeThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    while (true) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tt = tt + 60000;
                                tv_time.setText(getElapsedTimeMinutesSecondsString(tt));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
                Log.d("jjj", "thread stop");
            }
        };

        thread.start();
    }

    private void updateTimeThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {

                    while (true) {
                        Thread.sleep(1000);
                        new_time = tt;
                        Log.d(TAG, "new_time  is:" + new_time + "cur time:" + current_time);
                        if(current_time != new_time)
                        {
                            if((new_time - current_time) > 1000)
                            {
                                long diff_time = new_time - current_time;
                                Log.w(TAG, "new_time - current_time is:" + diff_time);
                            }
                        }
                        current_time = new_time;
                        tt = tt + 100;
                        Log.d(TAG, "tt  is:" + tt);
                    }
                } catch (InterruptedException e) {
                }
                Log.d("jjj", "thread stop");
            }
        };

        thread.start();
    }

    public static String getElapsedTimeMinutesSecondsString(int miliseconds) {
        int elapsedTime = miliseconds;
        String format = String.format("%%0%dd", 2);
        elapsedTime = elapsedTime / 1000;
        int mins = elapsedTime / 60;
        int hrs = elapsedTime / 3600;
        String seconds = String.format(format, elapsedTime % 60);
        String minutes = String.format(format, mins % 60);
        String hours = String.format(format, hrs);
        return hours + ":" + minutes + ":" + seconds;
    }
}
