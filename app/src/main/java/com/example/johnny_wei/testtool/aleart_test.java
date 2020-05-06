package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class aleart_test extends AppCompatActivity {
    Activity mActivity = this;
    private final String TAG = getClass().getSimpleName();
    EditText txt;
    LayoutInflater inflater;
    AlertDialog.Builder builder;
    View dialogView;
    EditText alertEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aleart_test);

        //quick setup
        //CreateAlertDialogWithRadioButtonGroup();


    }

    RadioButton rdx_ecg;
    RadioButton rdx_ppg;

    //group
    private void radio_setup() {
        rdx_ecg = dialogView.findViewById(R.id.radio_ecg);
        rdx_ppg = dialogView.findViewById(R.id.radio_ppg);
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

    CheckBox checkBox1;
    CheckBox checkBox2;

    void setup_alert_checkbox()
    {
        checkBox1 = (CheckBox)dialogView.findViewById(R.id.checkBox_0);
        checkBox2 = (CheckBox)dialogView.findViewById(R.id.checkBox_1);


        checkBox1.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        checkBox2.setOnCheckedChangeListener(checkBoxOnCheckedChange);

    }

    //這是CompoundButton,不是RadioGroup
    private CompoundButton.OnCheckedChangeListener checkBoxOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                { //buttonView 為目前觸發此事件的 CheckBox, isChecked 為此 CheckBox 目前的選取狀態

                    if(isChecked)//等於 buttonView.isChecked()
                    {
                        Toast.makeText(getApplicationContext(),buttonView.getText()+" 被選取", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),buttonView.getText()+" 被取消", Toast.LENGTH_LONG).show();
                    }

                    Log.d(TAG,"buttonView.getId():"+buttonView.getId());
                    Log.d(TAG,"R.id.checkBox_0:"+R.id.checkBox_0);
                    Log.d(TAG,"R.id.checkBox_1:"+R.id.checkBox_1);

                }
            };


    RadioButton r00;
    RadioButton r01;
    //選了之後無法uncheck,通常不會這麼用
    void setup_radio_multi()
    {
        r00 = dialogView.findViewById(R.id.radio_00);
        r01 = dialogView.findViewById(R.id.radio_01);
        r00.setOnCheckedChangeListener(checkBoxOnCheckedChange);
        r01.setOnCheckedChangeListener(checkBoxOnCheckedChange);
    }

    //不知道為何default顯示不出來
    void setup_alert_Spinner()
    {
        String[] testModeArray = {"115200","921600","9600"};
        Spinner notifySpinner;
        //ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, testModeArray);
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.myspinner, testModeArray);
        notifySpinner= (Spinner)dialogView.findViewById(R.id.alert_00_spinner);

        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.myspinner);
        notifySpinner.setSelection(1, true);
        notifySpinner.setAdapter(adapter);
        notifySpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView adapterView, View view, int position, long id) {

                Log.d(TAG,"position:" + position);

                Toast.makeText(mActivity, "You choose " + adapterView.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView arg0) {
                Toast.makeText(mActivity, "You did not choose", Toast.LENGTH_LONG).show();
            }
        });
    }

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
                        Log.d(TAG,"Edit text is:" + alertEdt.getText().toString());
                    }
                });
        AlertDialog dialog = builder.create();
//        AlertDialog dialog = (AlertDialog) createAddPersonDialog();
        dialog.show();
        alertEdt = dialogView.findViewById(R.id.alert_edt);
        radio_setup();
        setup_alert_Spinner();
        setup_alert_checkbox();
        setup_radio_multi();
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

    //
    EditText editText;
    android.app.AlertDialog.Builder editDialog;
    void show_dialog_edit()
    {
        editDialog = new android.app.AlertDialog.Builder(mActivity);
        editText= new EditText(mActivity);
        editDialog.setView(editText);
        editText.setText("");
        editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface dialog, int arg1) {
                Log.d("open_file_intent", "edit txt:" + (editText.getText().toString()));
                dialog.cancel();
            }
        });
        editDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            // do something when the button is clicked
            public void onClick(DialogInterface dialog, int arg1) {
                //...
                dialog.cancel();
            }
        });

        editDialog.show();
    }

}
