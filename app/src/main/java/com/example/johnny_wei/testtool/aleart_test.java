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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class aleart_test extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    EditText txt;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
     View dialogView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aleart_test);

        //quick setup
        //CreateAlertDialogWithRadioButtonGroup();


    }

    RadioButton rdx_ecg;
    RadioButton rdx_ppg;
    private void radio_setup() {
        rdx_ecg = findViewById(R.id.radio_ecg);
        rdx_ppg = findViewById(R.id.radio_ppg);
        RadioGroup radioGroup_tx_rx = dialogView.findViewById(R.id.radio_ppg_ecg);
        radioGroup_tx_rx.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_ecg:
                    Toast.makeText(aleart_test.this, "ECG select", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.radio_ppg:
                    Toast.makeText(aleart_test.this, "PPG select", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void clk_start(View view) {
        //custom view
//custom alert
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
        radio_setup();
    }

    AlertDialog alertDialog1;
    CharSequence[] values = {" First Item "," Second Item "," Third Item "};
    public void CreateAlertDialogWithRadioButtonGroup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(aleart_test.this);
        builder.setTitle("Select Your Choice");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Toast.makeText(aleart_test.this, "First Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(aleart_test.this, "Second Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(aleart_test.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

}
