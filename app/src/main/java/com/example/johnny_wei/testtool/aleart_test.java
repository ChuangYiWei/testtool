package com.example.johnny_wei.testtool;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class aleart_test extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    EditText txt;
    LayoutInflater inflater;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aleart_test);
    }

    public void clk_start(View view) {
        inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.test_aleart, null);
        builder = new AlertDialog.Builder(this);
        builder.setTitle("這是標題")
                .setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do something
                        txt = dialogView.findViewById(R.id.edt_name_alert);
                        Log.d(TAG,"name in alert is:" + txt.getText().toString());
                    }
                });
        AlertDialog dialog = builder.create();
//        AlertDialog dialog = (AlertDialog) createAddPersonDialog();
        dialog.show();


    }
    private Dialog createAddPersonDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(aleart_test.this);

        // Inflate using dialog themed context.
        final Context context = builder.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.activity_aleart_test, null, false);

        // Find widgets inside "view".
        final EditText name = (EditText) view.findViewById(R.id.edt_name_alert);


        final View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = (String) name.getText().toString();
                Log.d(TAG,"name in alert is:" + user_name);
            }
        };

        builder
                .setView(view)
                .setCancelable(false);
//                .setPositiveButton("OK", listener)
//                .setNegativeButton("Cancel", listener);
        return builder.create();
    }
}
