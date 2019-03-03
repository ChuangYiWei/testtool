package com.example.johnny_wei.testtool;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class test_layout extends AppCompatActivity {

    EditText ed1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);
        ed1 = findViewById(R.id.ed_test1);
        String txt = ed1.getText().toString();
        Log.d("test_layout","edit txt is:" + txt);
    }
}
