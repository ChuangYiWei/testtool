package com.example.johnny_wei.testtool;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.example.johnny_wei.testtool.utils.AlertDialogUtil;

import java.io.File;


public class open_file_intent extends AppCompatActivity {
    Handler user_handler;
    //Intent it = getTextFileIntent("/mnt/sdcard/hello.txt",false);
    static final String PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath();
    EditText editText;
    AlertDialog.Builder editDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_txt);


        //read current edit text
        HandlerThread ht = new HandlerThread("userdefine");
        ht.start();
        user_handler = new Handler(ht.getLooper());
        user_handler.postDelayed(r0,3000);
        showdeditText();

        //use intent to go to other activity
//        Intent it = getTextFileIntent(PATH_SD+'/'+"test.txt",false);
//        startActivity( it );
    }

    void showdeditText()
    {
        editDialog = new AlertDialog.Builder(open_file_intent.this);
        editText= new EditText(open_file_intent.this);
        //editText.setText("01");
        editDialog.setView(editText);
        editText.setText("1234");
        editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                Log.d("open_file_intent", "edit txt:" + (editText.getText().toString()));
            }
        });
        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface arg0, int arg1) {
                //...
            }
        });

        editDialog.show();
    }

    private Runnable r0 = new Runnable() {
        //工作內容寫進run(),繁雜的工作可以給另一個特約工人做
        public void run() {
            while (true) {
                try {
                    if(Looper.myLooper() == Looper.getMainLooper())
                    {
                        Log.d("r0", "run on ui thread");
                    }
                    SystemClock.sleep(5000);
                    Log.d("open_file_intent", "cur edit txt is:" + (editText.getText().toString()));
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            editText.setText("");
//                        }
//                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };


    //android獲取一個用於開啟文字檔案的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean) {
            Uri uri1 = Uri.parse(param );
            intent.setDataAndType(uri1, "text/plain");
        } else {
            Uri uri2 = Uri.fromFile(new File(param ));
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
}

//ref https://www.itread01.com/content/1549353630.html
