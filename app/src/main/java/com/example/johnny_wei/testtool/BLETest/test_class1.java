package com.example.johnny_wei.testtool.BLETest;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.johnny_wei.testtool.invok;

public class test_class1 extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    private String name;


    public test_class1(String name){
        this.name = "test_class1";
        Log.w(TAG,"create class:"+name);

    }

    public boolean test_func1(String[] str) {
        Log.d(TAG,"test_func1");
        int i;
        for(i=0;i<str.length;i++)
        {
            Log.d(TAG,"str["+i+"]:" + str[i]);
        }
        return true;
    }

    public boolean HCI_LE_Set_Advertising_Data(String[] str) {
        Log.d(TAG,"HCI_LE_Set_Advertising_Data");
        int i;
        for(i=0;i<str.length;i++)
        {
            Log.d(TAG,"str["+i+"]:" + str[i]);
        }
        return true;
    }

    public boolean HCI_loopback(String[] str) {
        Log.d(TAG,"HCI_loopback");
        int i;
        for(i=0;i<str.length;i++)
        {
            Log.d(TAG,"str["+i+"]:" + str[i]);
        }
        return true;
    }

    public boolean test_func2(String arg) {
        Log.d(TAG,"test_func2");
        Log.d(TAG,"arg:" + arg);
        return true;
    }

    public boolean test_func3(String arg) {
        Log.d(TAG,"test_func2");
        Log.d(TAG,"arg:" + arg);
        return true;
    }
}
