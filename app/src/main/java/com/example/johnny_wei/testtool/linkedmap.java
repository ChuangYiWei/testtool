package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class linkedmap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linkedmap);

        Map<String, String> map = new LinkedHashMap<String, String>();

        map.put("1", "11");
        map.put("22", "22");
        map.put("33", "33");

        Iterator it = map.entrySet().iterator();
        while (it.hasNext()){
            Log.d("jjj",it.next().toString());
        }

            for (Map.Entry<String, String> entry : map.entrySet()) {
            Log.d("jjj",entry.getKey() + " = " + entry.getValue());
        }
    }
}
