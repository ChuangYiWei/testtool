package com.BLE.johnny_wei.testtool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.BLE.johnny_wei.testtool.drawUtil.MovePathView;
import com.BLE.johnny_wei.testtool.drawUtil.MySurfaceView;
import com.BLE.johnny_wei.testtool.drawUtil.PaintView;
import com.BLE.johnny_wei.testtool.drawUtil.TestPathView;

import java.util.ArrayList;
import java.util.List;

public class test_draw extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    public List<String> data_list = new ArrayList<>();
    public int[] data_array = new int[1000];
    public static test_draw static_test_draw;
    Activity mActivity = this;
    PaintView pv;
    MovePathView mv;
    TestPathView test_view;
    LinearLayout layoutGet;
    RadioButton rdx_ecg;
    RadioButton rdx_ppg;

    MySurfaceView mySurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new Panel(this));
        //pv = new PaintView(this, null);
        setContentView(R.layout.activity_test_draw);
        layoutGet = (LinearLayout) findViewById(R.id.lin_layout);

        //draw by canvas
        mySurfaceView = new MySurfaceView(this);
        layoutGet.addView(mySurfaceView);

        //simulate ppg
//        test_view = new TestPathView(this);
//        layoutGet.addView(test_view);

        //mv = new MovePathView(this);
        //setContentView(mv);


        radio_setup();
        static_test_draw = this;
        init();

    }
    private void radio_setup() {
        rdx_ecg = findViewById(R.id.radio_ecg);
        rdx_ppg = findViewById(R.id.radio_ppg);
        RadioGroup radioGroup_tx_rx = findViewById(R.id.radio_ppg_ecg);
        radioGroup_tx_rx.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }

    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.radio_ecg:
                    Toast.makeText(mActivity, "ECG select", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.radio_ppg:
                    Toast.makeText(mActivity, "PPG select", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy");
    }

    public void init()
    {
        for (int i = 0; i < 1000; i++) {
            data_array[i] = -i*5;
        }
    }

    public void clk_test(View view) {
        pv.start_draw();
    }

    public void clk_reset(View view) {
        mv.reset();
    }
}



class Panel extends View {

    public Panel(Context context) {
        super(context);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("test_draw","Height:" + w + ", width:" + h);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Bitmap bmp = BitmapFactory.decodeResource(
                getResources(), R.drawable.close);
        Paint paint = new Paint();
        //畫布底色
        canvas.drawColor(Color.BLACK);
        //畫筆色
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        // paint.setStrokeWidth(1);


        canvas.drawLine(0,0, 100, 100, paint);
        //畫筆色
        paint.setColor(Color.YELLOW);
        canvas.drawRect(300, 300, 600, 600, paint);
        paint.setColor(Color.GREEN);
        canvas.drawCircle(50, 50, 40, paint);
        paint.setColor(Color.GRAY);
        canvas.drawText("Hello!", 1, 100, paint);
        canvas.drawBitmap(bmp, 1, 120, null);

        //畫布底色
        canvas.drawColor(Color.WHITE);
        //畫筆色(灰)
        paint.setColor(Color.GRAY);
        //畫圓
        canvas.drawCircle(160, 160, 150, paint);

        //畫方
        paint.setColor(Color.BLUE);
        Rect rect = new Rect(100, 110, 120, 130);
        canvas.drawRect(rect, paint);

        //畫圓角方
        paint.setColor(Color.GREEN);
        RectF rectf = new RectF(200, 110, 220, 130);
        canvas.drawRoundRect(rectf, 7, 7, paint);

        //畫弧
        paint.setColor(Color.YELLOW);
        RectF oval = new RectF(50, 150, 270, 250);
        canvas.drawArc(oval, 180, -180, true, paint);

        //畫字
        paint.setColor(Color.BLACK);
        canvas.drawText("Andy", 160, 350, paint);
    }
}


