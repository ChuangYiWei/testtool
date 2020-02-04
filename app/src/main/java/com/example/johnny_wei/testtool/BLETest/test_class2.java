package com.example.johnny_wei.testtool.BLETest;

import android.util.Log;

public class test_class2 {
    String TAG = getClass().getSimpleName();
    private String name;


    public test_class2(String name){
        this.name = "test_class1";
        Log.w(TAG,"create class:"+name);

    }

    public boolean test2_func1(String[] str) {
        Log.d(TAG,"test2_func1");
        int i;
        for(i=0;i<str.length;i++)
        {
            Log.d(TAG,"str["+i+"]:" + str[i]);
        }
        return true;
    }

}
