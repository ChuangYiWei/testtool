package com.BLE.johnny_wei.testtool.drawUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;

public class TestPathView extends View {
    private final String TAG = getClass().getSimpleName();
    private Path mPath;
    private Paint mPaint;
    int baseline = 0; //起始點座標
    int sample = 200;
    int[] data_x = new int[sample];
    int add_sample = 6;
    int mWidth = 0;
    int mHeight = 0;
    int max=1;
    int min=0;
    int out_range_max=1;
    int out_range_min=0;
    int new_max=1;
    int new_min=0;
    int produce_ms=100;
    int draw_ms=5;
    int y_axis_positive_max = 0;//最大顯示正y軸座標
    private Object lock = new Object();

    LinkedList<Integer> data_list = new LinkedList<Integer>();
    //手指按下的位置
    private float startX,startY;
    public  Handler ui_handler = new Handler(Looper.getMainLooper());
    public TestPathView(Context context) {
        super(context);
        init();
        //init_data();
        ui_handler.postDelayed(ui_runnable,1000);
        producer_thread.start();
    }

    void init_data() {
        for (int i = 0; i < 300; i++) {
            data_list.add(i*10);
        }
        Log.d("test_surface", "list size:" + data_list.size());
    }

    private Runnable ui_runnable = new Runnable() {
        @Override
        public void run() {
            try {
//                mydraw();
                //mydraw2();
                mydraw3();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ui_handler.postDelayed(ui_runnable,draw_ms);
        }
    };

    // Create producer thread
    Thread producer_thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    //produce();
                    produce_rawdata();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(produce_ms);
            }
        }
    });

    public void produce() throws InterruptedException {
        synchronized (lock) {
            for (int i = 0; i < add_sample; i++) {
                if (i % 2 == 0) {
                    //data_list.add(generatRandomPositiveNegitiveValue(500,100));
                    data_list.add(100);
                } else {
                    //data_list.add(-100);
                    data_list.add(-100);
                }
            }
        }
    }

    int g_int=0;
    public void produce_rawdata() throws InterruptedException {
        synchronized (lock) {
//            System.out.println("produce enter");
            for (int i = 0; i < add_sample; i++) {
                data_list.add(raw_data[g_int]);
                g_int++;
                if (g_int > raw_data.length - 1) {
                    g_int = 0;
                }
            }
        }
    }

    int sample_idx=0;
    void mydraw() throws InterruptedException
    {
        if(sample_idx ==0)
        {
            mPath.moveTo(0,baseline);
        }
        Log.d("MovePathView", "sample_idx:"+sample_idx);
        mPath.lineTo(data_x[sample_idx],baseline-raw_data[sample_idx]);
        int tmp_y=baseline-raw_data[sample_idx];
        Log.d("MovePathView", "x:"+data_x[sample_idx] + " y:" + tmp_y);
        if(sample_idx ==9)
        {
            //last one
            sample_idx =0;
            mPath.reset();
            invalidate();
            return;
        }
        sample_idx++;
        invalidate();
    }

    void mydraw2() throws InterruptedException
    {
        Log.d(TAG, "data_list.size() is:" + data_list.size());
        if(data_list.size() ==0)
        {
            Log.d(TAG, "data_list.size() is 0");
            return;
        }
        if(sample_idx ==0)
        {
            mPath.moveTo(0,baseline);
        }
        Log.d("MovePathView", "sample_idx:"+sample_idx);
        synchronized (lock) {
            int tmp_y = baseline - data_list.getFirst();
            mPath.lineTo(data_x[sample_idx], tmp_y);
            Log.d("MovePathView", "x:" + data_x[sample_idx] + " y:" + tmp_y);
            data_list.removeFirst();
        }
        if(sample_idx == sample - 1)
        {
            //last one
            sample_idx =0;
            mPath.reset();
            invalidate();
            return;
        }
        sample_idx++;
        invalidate();
    }
    int tmp_first_data=0;
    int range_in_min_max_data=0;
    int current_min=0;
    int current_max=1;
    int tmp_minus_min;
    float f_tmp=0.0f;
    int scaled=0;
    int data_y=0;
    int max_min_interval=0;
    boolean first_init=true;
    void mydraw3() throws InterruptedException
    {
        Log.d(TAG, "data_list.size() is:" + data_list.size());
        if(data_list.size() ==0)
        {
            Log.d(TAG, "data_list.size() is 0");
            return;
        }
        if(first_init && data_list.size() < 80  )
        {
            Log.d(TAG, "data_list.size() < 20");
            return;
        }
        if (first_init && data_list.size() > 80) {
            min = data_list.getFirst();
            for (int i = 0; i < 80; i++) {
                if (data_list.get(i) < min) {
                    min = data_list.get(i);
                }
                if (data_list.get(i) > max) {
                    max = data_list.get(i);
                }
            }

            out_range_max = max ;
            out_range_min = min ;
            first_init = false;
        }

        if (sample_idx == 0) {
            mPath.moveTo(0, baseline);
        }

        Log.d("MovePathView", "sample_idx:"+sample_idx);
        synchronized (lock) {
            tmp_first_data = data_list.getFirst();

            //control display range
            if (tmp_first_data > max) {
                range_in_min_max_data = out_range_max;
            } else if (tmp_first_data < min) {
                range_in_min_max_data = out_range_min;
            } else {
                range_in_min_max_data = tmp_first_data;
            }

            //scale
            tmp_minus_min= range_in_min_max_data - min;
            max_min_interval = max-min;
            f_tmp = (float)tmp_minus_min/max_min_interval;
            scaled = (int) (y_axis_positive_max * f_tmp);
            data_y = baseline - scaled;
            mPath.lineTo(data_x[sample_idx], data_y);
            Log.d(TAG, "min:" + min + " max:" + max+", max_min_interval:" +max_min_interval + ",range_in_min_max_data"+range_in_min_max_data);
            Log.d(TAG, "x:" + data_x[sample_idx] + " y:" + data_y);
            Log.d(TAG, "tmp_first_data:" + tmp_first_data + " ,tmp_minus_min:"+tmp_minus_min+" f_tmp:" + f_tmp + ",scaled:" + data_y+",baseline:"+baseline+"y_axis_positive_max"+y_axis_positive_max);
            Log.d(TAG, "out_range_min:" + out_range_min + ",out_range_max:" + out_range_max);
            data_list.removeFirst();
            //find min max in last 80 sample
            if (sample > 80) {
                if (sample_idx > sample - 80) {
                    if (sample_idx == (sample - 80 + 1)) {
                        new_min = tmp_first_data;
                        new_max = tmp_first_data;
                    }
                    if (tmp_first_data < new_min) {
                        new_min = tmp_first_data;
                        out_range_min = new_min;
                    }
                    if (tmp_first_data > new_max) {
                        new_max = tmp_first_data;
                        out_range_max = new_max;
                    }
                    Log.d(TAG, "current new_min:" + new_min + ",current new_max:" + new_max);
                }
            }
        }
        //last one , reset and redraw, update min/max
        if (sample_idx == sample - 1) {
            if (sample > 80) {
                min = new_min;
                max = new_max;
                Log.w(TAG, "new_min:" + new_min + "new_max:" + new_max);
            }
            //last one
            sample_idx = 0;
            mPath.reset();
            invalidate();
            return;
        }
        sample_idx++;
        invalidate();
    }
    //初始化
    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
    }

    public TestPathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.w(TAG, "onSizeChanged:"+ "w" + w + " h" + h);
        mWidth = w;
        mHeight = h;
        //baseline = mHeight/2;
        baseline = mHeight;
        y_axis_positive_max = mHeight;
        for (int i = 0; i < sample; i++) {
            data_x[i] = (i) * (mWidth / sample);
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                //设置原点
                mPath.moveTo(startX,startY);
                Log.d("MovePathView", "startX:"+startX+" startY"+startY);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                float currX = event.getX();
                float currY = event.getY();
                Log.d("MovePathView", "currX:"+currX+" currY"+currY);
                //连线
                mPath.lineTo(currX,currY);
                //刷新view
                invalidate();
                break;

        }
        //返回true，消费事件
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath,mPaint);
    }
    //对外提供的方法，重新绘制
    public void reset(){
        mPath.reset();
        invalidate();
    }


    int[] raw_data1 = new int[]{
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 5000, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,
            400, 450,  460, 800, 500, 100 ,100, 100,100,100,

    };

    int[] raw_data2 = new int[]{
            100, -100,  100, -100, 100, -100 ,100, -100,100,-100,
            10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
            10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
            10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
    };

    int[] raw_data = new int[]{
            0x21DF60,0x21DF60,0x21E060,0x21E000,0x21E200,0x21E880,0x21EF40,0x21F5A0,0x21FAA0,0x21FF40,
            0x220480,0x220640,0x220800,0x220AA0,0x220B60,0x220C40,0x220BA0,0x220A20,0x220860,0x220500,
            0x220100,0x21FDE0,0x21FBA0,0x21F700,0x21F020,0x21E960,0x21E2A0,0x21DF80,0x21E0C0,0x21E0E0,
            0x21E040,0x21E060,0x21DF20,0x21E020,0x21E0E0,0x21E0E0,0x21E180,0x21E140,0x21E0A0,0x21E020,
            0x21E040,0x21E020,0x21E080,0x21E060,0x21E0A0,0x21E0C0,0x2201A0,0x227B00,0x232D20,0x23E1C0,
            0x249540,0x254700,0x258160,0x2509E0,0x245800,0x23A560,0x22F260,0x224060,0x21ADE0,0x21AB20,
            0x21E1C0,0x21E1E0,0x21E280,0x21E260,0x21E1C0,0x21E1C0,0x21E160,0x21E160,0x21E2E0,0x21E720,
            0x21ED80,0x21F360,0x21F8C0,0x21FF20,0x2204A0,0x220780,0x220AA0,0x220D80,0x220F20,0x221160,
            0x221140,0x221060,0x220FC0,0x220D40,0x220A40,0x220720,0x2203A0,0x21FEC0,0x21FA60,0x21F440,
            0x21EDA0,0x21E8E0,0x21E600,0x21E480,0x21E420,0x21E4E0,0x21E540,0x21E4E0,0x21E4C0,0x21E4C0,
            0x21E480,0x21E4E0,0x21E600,0x21E580,0x21E4E0,0x21E5E0,0x21E6E0,0x21E760,0x21E6A0,0x21E660,
            0x21E660,0x21FEA0,0x226FC0,0x232200,0x23D3E0,0x2487E0,0x253C80,0x258860,0x252180,0x246E60,
            0x23BC60,0x230AC0,0x2257E0,0x21B9E0,0x21ADE0,0x21E6E0,0x21E7A0,0x21E7E0,0x21E840,0x21E720,
            0x21E6E0,0x21E860,0x21E7E0,0x21E800,0x21EB20,0x21F0A0,0x21F720,0x21FE00,0x220380,0x2206E0,
            0x220B40,0x220F00,0x221280,0x2214A0,0x221500,0x221500,0x221540,0x221500,0x2212A0,0x220FC0,
            0x220D20,0x220900,0x2203C0,0x21FF40,0x21FB60,0x21F640,0x21EF80,0x21EBC0,0x21EAC0,0x21E9A0,
            0x21E980,0x21EA20,0x21E9E0,0x21EA60,0x21EB00,0x21EAA0,0x21EA80,0x21EA80,0x21EA20,0x21EA00,
            0x21EA20,0x21E9A0,0x21E960,0x21EA40,0x21EB20,0x21EB60,0x21FC40,0x2263C0,0x231440,0x23C6C0,
            0x2479E0,0x252D80,0x258C40,0x2537E0,0x2485E0,0x23D440,0x232080,0x226CC0,0x21C540,0x21AE80,
            0x21E900,0x21EBC0,0x21EBC0,0x21ECC0,0x21EC60,0x21EC00,0x21EC60,0x21EBE0,0x21ED20,0x21F080,
            0x21F540,0x21FBA0,0x220200,0x220680,0x220A00,0x220D80,0x2211C0,0x2215C0,0x221800,0x221960,
            0x2219E0,0x221A20,0x221880,0x221600,0x2213E0,0x221140,0x220D40,0x220880,0x2203A0,0x21FE80,
            0x21F9C0,0x21F400,0x21EE40,0x21ED20,0x21EE20,0x21EDE0,0x21EEE0,0x21EF40,0x21EF20,0x21EFE0,
            0x21F0C0,0x21EFA0,0x21EEE0,0x21EF40,0x21EF60,0x21F060,0x21EFC0,0x21EFA0,0x21F080,0x21EF80,
            0x21EF40,0x21FA40,0x225900,0x2305E0,0x23B920,0x246D80,0x252040,0x259080,0x254DE0,0x249B60,
            0x23E9C0,0x233760,0x228460,0x21D600,0x21B260,0x21EA20,0x21EFC0,0x21F020,0x21F0C0,0x21EFC0,
            0x21F020,0x21F020,0x21EF80,0x21EFE0,0x21F3E0,0x21FAC0,0x220080,0x220460,0x220860,0x220D60,
            0x221240,0x2216E0,0x221AA0,0x221D20,0x221E40,0x221E40,0x221DE0,0x221E40,0x221CE0,0x221980,
            0x221600,0x221220,0x220CE0,0x220860,0x2203A0,0x21FCC0,0x21F6E0,0x21F360,0x21F2A0,0x21F2A0,
            0x21F240,0x21F200,0x21F1C0,0x21F240,0x21F240,0x21F1E0,0x21F2E0,0x21F340,0x21F240,0x21F240,
            0x21F280,0x21F200,0x21F1E0,0x21F2C0,0x21F3E0,0x21F380,0x21F8E0,0x224F20,0x22F8C0,0x23AB40,
            0x245D80,0x2511C0,0x259380,0x256300,0x24B200,0x23FE40,0x234BA0,0x229980,0x21E880,0x21B7C0,
            0x21E9A0,0x21F400,0x21F320,0x21F360,0x21F440,0x21F460,0x21F400,0x21F360,0x21F440,0x21F7A0,
            0x21FD40,0x220360,0x220820,0x220CE0,0x221240,0x2216E0,0x221A80,0x221D00,0x221F00,0x222160,
            0x2221C0,0x2221E0,0x222160,0x221F40,0x221D60,0x221B00,0x221680,0x221100,0x220BC0,0x220700,
            0x2201A0,0x21FB00,0x21F720,0x21F600,0x21F580,0x21F5A0,0x21F560,0x21F500,0x21F560,0x21F620,
            0x21F700,0x21F780,0x21F740,0x21F700,0x21F6C0,0x21F640,0x21F6C0,0x21F740,0x21F680,0x21F5E0,
            0x21F720,0x21FA80,0x2246C0,0x22E9E0,0x239CE0,0x245120,0x250320,0x259260,0x257520,0x24C7C0,
            0x2415C0,0x2362A0,0x22AFA0,0x21FD40,0x21BCE0,0x21E6A0,0x21F720,0x21F7A0,0x21F8C0,0x21F820,
            0x21F780,0x21F800,0x21F800,0x21F760,0x21FA80,0x2200A0,0x220420,0x220940,0x220FC0,0x221500,
            0x221A00,0x221E60,0x2221C0,0x2223A0,0x222540,0x222580,0x222580,0x2224E0,0x2223A0,0x222200,
            0x221EC0,0x221B40,0x221680,0x2210A0,0x220B00,0x220540,0x21FF00,0x21FB00,0x21F9A0,0x21F920,
            0x21F940,0x21F9A0,0x21FA80,0x21F9E0,0x21FA80,0x21FB20,0x21F9E0,0x21F9C0,0x21FA00,0x21FAA0,
            0x21FBC0,0x21FC40,0x21FA80,0x21F9A0,0x21FAC0,0x21FAC0,0x21FB60,0x223FC0,0x22DC20,0x238E00,
            0x244080,0x24F2E0,0x259100,0x258740,0x24DE00,0x242B00,0x237760,0x22C4E0,0x221320,0x21C140,
            0x21E220,0x21FAE0,0x21FBA0,0x21FC60,0x21FCA0,0x21FC20,0x21FC00,0x21FBC0,0x21FAC0,0x21FD00,
            0x220220,0x220720,0x220CC0,0x221280,0x2217E0,0x221CE0,0x222100,0x222380,0x222480,0x2226A0,
            0x222860,0x2227E0,0x2227A0,0x222800,0x222560,0x2221C0,0x221DA0,0x2218E0,0x221520,0x220FA0,
            0x220A00,0x220300,0x21FCC0,0x21FD40,0x21FEC0,0x21FE00,0x21FD40,0x21FC60,0x21FCE0,0x21FE00,
            0x21FCE0,0x21FD80,0x21FEA0,0x21FEA0,0x21FF00,0x21FD80,0x21FD00,0x21FDC0,0x21FDA0,0x21FEA0,
            0x21FF40,0x21FF00,0x223920,0x22CC00,0x237E60,0x243280,0x24E620,0x258C60,0x259200,0x24F100,
            0x243E60,0x238BE0,0x22DA60,0x222800,0x21C520,0x21DD00,0x21FDC0,0x21FE00,0x21FE60,0x21FF40,
            0x21FF80,0x21FF60,0x220060,0x21FEA0,0x220000,0x220440,0x220860,0x220FA0,0x221640,0x221AC0,
            0x221F20,0x2223E0,0x222780,0x222A60,0x222B60,0x222C00,0x222C00,0x222AE0,0x222AA0,0x2228C0,
            0x222500,0x222120,0x221D20,0x2217E0,0x2212E0,0x220D60,0x2206C0,0x2201E0,0x21FFC0,0x220140,
            0x220080,0x21FE80,0x21FFC0,0x220220,0x2201E0,0x220000,0x21FEE0,0x21FFC0,0x220160,0x21FFA0,
            0x220040,0x220020,0x21FE00,0x21FEE0,0x220120,0x220100,0x21FFC0,0x223320,0x22BE00,0x236FE0,
            0x242420,0x24D680,0x258340,0x259BA0,0x250780,0x245500,0x23A140,0x22EEC0,0x223C80,0x21C8E0,
            0x21D700,0x220080,0x220040,0x21FF80,0x2200A0,0x220020,0x21FF80,0x220220,0x2201A0,0x220000,
            0x2204C0,0x220BA0,0x221280,0x221960,0x221E40,0x222240,0x222540,0x222940,0x222CA0,0x222DE0,
            0x223000,0x223040,0x222E20,0x222D00,0x222B40,0x222880,0x222480,0x221FE0,0x221C60,0x221700,
            0x221120,0x220BC0,0x220520,0x2201A0,0x220260,0x2202E0,0x220240,0x220100,0x220020,0x220000,
            0x21FFC0,0x220300,0x220380,0x220280,0x220380,0x220260,0x2201C0,0x2201A0,0x220280,0x220220,
            0x220140,0x220220,0x222BE0,0x22AEE0,0x2362E0,0x241620,0x24C820,0x257720,0x25A220,0x251CA0,
            0x246A00,0x23B740,0x230440,0x2251E0,0x21CD00,0x21D2C0,0x220460,0x220440,0x220340,0x220320,
            0x220220,0x220320,0x220380,0x2202C0,0x2204C0,0x220880,0x220E60,0x221560,0x221B60,0x222020,
            0x222440,0x222880,0x222CC0,0x222F40,0x2230E0,0x223220,0x2232A0,0x223240,0x2230E0,0x222F00,
            0x222CE0,0x222940,0x2223E0,0x221FA0,0x221B00,0x2213E0,0x220EC0,0x2209C0,0x220560,0x220600,
            0x2205E0,0x220540,0x220600,0x2205A0,0x220520,0x2204E0,0x220560,0x220660,0x220620,0x2204E0,
            0x2206A0,0x2207C0,0x220660,0x220660,0x220600,0x220640,0x220680,0x222620,0x229FE0,0x235440,
            0x2406A0,0x24B840,0x256B40,0x25A840,0x2531C0,0x247E60,0x23CD00,0x231A80,0x226780,0x21D500,
            0x21D0E0,0x2206C0,0x220740,0x220720,0x220720,0x220780,0x2207E0,0x2207A0,0x220700,0x220720,
            0x220AC0,0x221140,0x2217C0,0x221E80,0x2223C0,0x222860,0x222C40,0x222EA0,0x223200,0x2234A0,
            0x223680,0x2236E0,0x2236E0,0x223700,0x223480,0x2230C0,0x222CE0,0x222900,0x222440,0x221F00,
            0x221920,0x221340,0x220E20,0x220A60,0x2208C0,0x220860,0x2208C0,0x220900,0x2208E0,0x2208E0,
            0x2208C0,0x2208E0,0x2209A0,0x220A20,0x220A00,0x220920,0x220960,0x220A60,0x220A60,0x220A40,
            0x2209E0,0x220A40,0x2222A0,0x229220,0x234440,0x23F780,0x24ABA0,0x255EC0,0x25AC80,0x2548E0,
            0x249580,0x23E1A0,0x232F60,0x227E20,0x21DF60,0x21D1E0,0x220BA0,0x220C40,0x220B60,0x220B60,
            0x220B80,0x220BE0,0x220CC0,0x220BC0,0x220BE0,0x221080,0x221580,0x221AC0,0x2220E0,0x222600,
            0x222B20,0x223040,0x223380,0x223580,0x223780,0x223A00,0x223AC0,0x223A20,0x223960,0x223700,
            0x223520,0x223240,0x222CE0,0x222840,0x2223C0,0x221E60,0x2217A0,0x2211A0,0x220E80,0x220D60,
            0x220D80,0x220D80,0x220D00,0x220CA0,0x220CC0,0x220D00,0x220DC0,0x220E20,0x220D80,0x220E00,
            0x220EA0,0x220E20,0x220DC0,0x220DE0,0x220E80,0x220EA0,0x220E60,0x221F20,0x2286A0,0x2336E0,
            0x23EAA0,0x249E60,0x2550C0,0x25B060,0x255E00,0x24ABC0,0x23F920,0x234660,0x229320,0x21EA40,
            0x21D2A0,0x220CC0,0x220F80,0x220F40,0x220F60,0x220F40,0x220FC0,0x220FC0,0x220FA0,0x221040,
            0x2212E0,0x221900,0x221EE0,0x222480,0x222A00,0x222E40,0x2232C0,0x2236A0,0x223A80,0x223D40,
            0x223E60,0x223FE0,0x224080,0x223E40,0x223BE0,0x223A00,0x223600,0x223140,0x222CA0,0x2227C0,
            0x222260,0x221CC0,0x221660,0x221180,0x2210A0,0x221020,0x220F60,0x2211C0,0x221300,0x221200,
            0x221160,0x221140,0x221180,0x2211C0,0x2211C0,0x221180,0x2210A0,0x2210C0,0x221200,0x2211A0,
            0x221180,0x221240,0x221C60,0x227AE0,0x232900,0x23DAC0,0x248DA0,0x254200,0x25B3E0,0x257420,
            0x24C280,0x240E40,0x235A60,0x22A8A0,0x21FAC0,0x21D6A0,0x220DE0,0x2213A0,0x2213A0,0x221360,
            0x221320,0x2212E0,0x2212A0,0x2212C0,0x2212A0,0x2215C0,0x221C00,0x2221C0,0x222760,0x222D00,
            0x2231E0,0x2236A0,0x223B00,0x223DC0,0x224020,0x224160,0x2241E0,0x2241A0,0x224000,0x223EA0,
            0x223D80,0x223A60,0x2235A0,0x223100,0x222BA0,0x2226A0,0x222120,0x221A60,0x221640,0x2214C0,
            0x221420,0x221480,0x221520,0x2214C0,0x2213C0,0x221500,0x2215C0,0x221500,0x221540,0x221500,
            0x221460,0x2214C0,0x2214C0,0x2214C0,0x2215A0,0x221620,0x2215A0,0x221B40,0x2270C0,0x231980,
            0x23CCA0,0x248040,0x253300,0x25B620,0x2587A0,0x24D640,0x2424A0,0x237100,0x22BD20,0x220D20,
            0x21DB40,0x220BE0,0x221700,0x2215A0,0x221620,0x221680,0x221620,0x2216C0,0x2216C0,0x2216C0,
            0x221940,0x221E40,0x222420,0x222A80,0x222FE0,0x223600,0x223A60,0x223E20,0x2241E0,0x2242E0,
            0x224420,0x2244A0,0x224420,0x224320,0x2241C0,0x223FC0,0x223E00,0x223A80,0x223520,
    };
}
