package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class time extends AppCompatActivity {
    public int tt= 0;
    TextView tv_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        tv_time = findViewById(R.id.tv_time);

        CountTimeThread();


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
