package com.example.johnny_wei.testtool;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.johnny_wei.testtool.config.globalConfig;

public class spinner_option extends AppCompatActivity {
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View dialogView;
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_option);//使用Spinner
        //setupSpinner();
    }

    void setupSpinner()
    {
        Spinner notifySpinner = (Spinner)findViewById(R.id.notify_spinner);
        String[] testModeArray = {"SPI mode","UART mode", "AIR UART CMD mode", "AIR HCI CMD mode"};
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.myspinner, testModeArray);

        adapter.setDropDownViewResource(R.layout.myspinner);
        notifySpinner.setAdapter(adapter);
        notifySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView adapterView, View view, int position, long id){

                Toast.makeText(spinner_option.this, "You choose "+adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(spinner_option.this, "You did not choose", Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 設置要用哪個menu檔做為選單
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("jjj","onOptionsItemSelected");
        // 取得點選項目的id
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.action_settings) {
            // 按下「設定」要做的事
            setupSpinner();
            Toast.makeText(this, "設定", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_help) {
            // 按下「使用說明」要做的事
            Toast.makeText(this, "使用說明", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_help) {
            // 按下「使用說明」要做的事
            Toast.makeText(this, "幫忙", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.baud_rate) {
            // 按下「使用說明」要做的事
            Toast.makeText(this, "baud rate", Toast.LENGTH_SHORT).show();
            inflater = this.getLayoutInflater();
            dialogView = inflater.inflate(R.layout.test_aleart, null);
            builder = new AlertDialog.Builder(this);
            builder.setTitle("這是標題")
                    .setView(dialogView)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do something
                        }
                    });
            AlertDialog dialog = builder.create();
//        AlertDialog dialog = (AlertDialog) createAddPersonDialog();
            dialog.show();

            Toast.makeText(this, "baud_rate", Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
