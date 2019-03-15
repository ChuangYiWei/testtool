package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import static android.view.View.VISIBLE;

public class test_layout extends AppCompatActivity {

    EditText ed1;
    private Button mButton;
    private ConstraintLayout mConstraintLayout;
    ConstraintLayout mIncludeConstraintLayout;
    private Context mContext;
    private PopupWindow mPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);


        // Get the application context
        //mContext = getApplicationContext();
        mContext = this;
        mConstraintLayout = (ConstraintLayout) findViewById(R.id.parentCS);
        mButton = (Button) findViewById(R.id.btn);

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Initialize a new instance of LayoutInflater service

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.custom_layout,null);

                // Initialize a new instance of popup window
                if(mPopupWindow == null) {
                    mPopupWindow = new PopupWindow(
                            customView,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT,
                            ConstraintLayout.LayoutParams.WRAP_CONTENT
                    );
                }

//                if(Build.VERSION.SDK_INT>=21){
//                    mPopupWindow.setElevation(5.0f);
//                }

                if(null == mPopupWindow)
                {
                    Log.e("jjj", "mPopupWindow is null");
                }
                try {
                    TextView tv = (TextView) customView.findViewById(R.id.tv);
                    String str = "THIS　is Friday Night !!!!";
                    tv.setText(str);
                    tv.setTextColor(0xffaabb00);
                    tv.setTextSize(20);
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            Log.e("jjj", "dismiss mPopupWindow ");
                            mPopupWindow.dismiss();
                        }
                    });

                    // Finally, show the popup window at the center location of root relative layout

                    mPopupWindow.showAtLocation(mConstraintLayout, Gravity.CENTER, 0, 0);
                }catch (Exception e)
                {
                    Log.e("jjj", e.toString());
                }
            }
        });
    }

    void fromIncludeLayout()
    {
        ed1 = findViewById(R.id.ed_test1);
        String txt = ed1.getText().toString();
        Log.d("test_layout","edit txt is:" + txt);
    }

    public void clk_include(View view) {
        //find included layout
        View includedlayout = (ConstraintLayout)findViewById(R.id.inc1);
        includedlayout.setVisibility(View.VISIBLE);

        //get obj from included layout
        EditText tx = includedlayout.findViewById(R.id.ed_test1);
        Log.d("jjj","tx from sublayout:" + tx.getText().toString());
    }
}
