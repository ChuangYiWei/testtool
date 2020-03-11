package com.example.johnny_wei.testtool;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Random;

public class test_surface extends AppCompatActivity {
    private final String className = getClass().getSimpleName();
    LinearLayout layoutGet;
    LinkedList<Integer> data_list = new LinkedList<Integer>();
    MySurfaceView mSurfaceView;
    private Object lock = new Object();
    int sample = 300;
    int add_sample = 5;
    int update_sample = 1;
    int g_int;
    int mWidth = 0;
    int mHeight = 0;
    int y_axis_positive_max = 0;//最大顯示正y軸座標
    int y_axis_negtive_max = 0;//最大顯示負y軸座標
    int y_data_max_value = 1;//當前y最大值,max(abs(valus)})
    int baseline = 0; //起始點座標

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("test_surface", "onConfigurationChanged");
    }

    @Override
    protected void onPause() {
        Log.d("test_surface", "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("test_surface", "onResume");
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_surface);
        init_data();
        //setContentView(new MyView(this));
        layoutGet = (LinearLayout) findViewById(R.id.linear);
        //setContentView(new MySurfaceView(this));
        mSurfaceView = new MySurfaceView(this);
        layoutGet.addView(mSurfaceView);
        mSurfaceView.enable_draw();
        Log.d("test_surface", "layoutGet.getWidth() : " + layoutGet.getWidth());
        Log.d("test_surface", "layoutGet.getLayoutParams().width : " + layoutGet.getLayoutParams().width);
        Log.d("test_surface", "layoutGet.getLayoutParams().height : " + layoutGet.getLayoutParams().height);

        layoutGet.measure(0, 0);
        Log.d("test_surface", "layoutGet.getMeasuredWidth() : " + layoutGet.getMeasuredWidth());
        Log.d("test_surface", "layoutGet.getMeasuredHeight() : " + layoutGet.getMeasuredHeight());

        //textview
        TextView tv = findViewById(R.id.tv_test);
        Log.d("test_surface", "tv.getWidth : " + tv.getWidth());
        Log.d("test_surface", "tv.getLayoutParams().width : " + tv.getLayoutParams().width);
        Log.d("test_surface", "tv.getLayoutParams().height : " + tv.getLayoutParams().height);

        //phone-----------------------
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int vWidth = dm.widthPixels;
        int vHeight = dm.heightPixels;

        Log.d("test_surface", "vWidth : " + vWidth);
        Log.d("test_surface", "vHeight : " + vHeight);

        producer_thread.start();
    }

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
                SystemClock.sleep(100);
            }
        }
    });

    void init_data() {
        for (int i = 0; i < sample; i++) {
            data_list.add(i);
        }
        Log.d("test_surface", "list size:" + data_list.size());
    }

    public void produce() throws InterruptedException {
        synchronized (lock) {
//            System.out.println("produce enter");
            int random_data;
            for (int i = 0; i < add_sample; i++) {
//                random_data = generatRandomPositiveNegitiveValue(500,-100);
//                Log.d(className, "random_data:"+random_data);
//                data_list.add(random_data);
                if (i % 2 == 0) {
                    data_list.add(100);
                } else {
                    //data_list.add(-100);
                    data_list.add(-100);
                }
            }
//            System.out.println("Wake up consumer thread");
        }
    }

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

    public int generatRandomPositiveNegitiveValue(int max, int min) {
        Random r = new Random();
        int ii = r.nextInt(max - min + 1) + min;
        return (ii - 140);
    }

    public void btn_stop(View view) {
        mSurfaceView.disable_draw();
    }

    public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
        private SurfaceHolder mHolder; // 用于控制SurfaceView
        //state
        boolean enable_draw = false;

        public Handler ui_handler = new Handler(Looper.getMainLooper());

        int draw_ms=0;

        private Thread t; // 声明一条线程
        private volatile boolean flag; // 线程运行的标识，用于控制线程
        private Canvas mCanvas; // 声明一张画布
        private Paint mPaint; // 声明一支画笔
        float m_circle_r = 10;

        public MySurfaceView(Context context) {
            super(context);

            mHolder = getHolder(); // 获得SurfaceHolder对象
            mHolder.addCallback(this); // 为SurfaceView添加状态监听
            mPaint = new Paint(); // 创建一个画笔对象
            mPaint.setStrokeWidth(3);
            mPaint.setColor(Color.RED); // 设置画笔的颜色为白色
            setFocusable(true); // 设置焦点                //red color

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Log.w("MySurfaceView", "onSizeChanged Height is :" + h);
            Log.w("MySurfaceView", "onSizeChanged width is :" + w);
            //y_axis_positive_max = h / 2;
            y_axis_positive_max = h ;
            y_axis_negtive_max = h / 2;
            mWidth = w;
            mHeight = h;
//            baseline = h / 2;
            baseline = h;
            for (int i = 0; i < sample; i++) {
                data_x[i] = (i) * (mWidth / sample);
            }
        }

        /**
         * 当SurfaceView创建的时候，调用此函数
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.w("MySurfaceView", "surfaceCreated");
//            t = new Thread(this); // 创建一个线程对象
//            flag = true; // 把线程运行的标识设置成true
//            t.start(); // 启动线程

        }

        /**
         * 当SurfaceView的视图发生改变的时候，调用此函数
         */
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
        }

        /**
         * 当SurfaceView销毁的时候，调用此函数
         */
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            flag = false; // 把线程运行的标识设置成false
            mHolder.removeCallback(this);
        }

        /**
         * 当屏幕被触摸时调用
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {

            return true;
        }

        /**
         * 当用户按键时调用
         */
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            }
            return super.onKeyDown(keyCode, event);
        }

        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            surfaceDestroyed(mHolder);
            return super.onKeyDown(keyCode, event);
        }

        @Override
        public void run() {
            while (flag) {
                try {
                    synchronized (mHolder) {
                        Thread.sleep(1); // 让线程休息1000毫秒
                        //Draw(); // 调用自定义画画方法
                        //test_draw_run();
                        test_draw_data_own_data();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
                        mHolder.unlockCanvasAndPost(mCanvas);// 完成画画，把画布显示在屏幕上结束锁定画图，并提交改变。
                    }
                }
            }
        }

        private Runnable draw_ppg_runnable = new Runnable() {
            @Override
            public void run() {
                if(enable_draw) {
                    try {
                        synchronized (mHolder) {
                            test_draw_data_own_data();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        if (mCanvas != null) {
                            mHolder.unlockCanvasAndPost(mCanvas);// 完成画画，把画布显示在屏幕上结束锁定画图，并提交改变。
                        }
                    }

                    ui_handler.postDelayed(draw_ppg_runnable, draw_ms);
                }
            }
        };

        public void enable_draw()
        {
            enable_draw = true;
            ui_handler.postDelayed(draw_ppg_runnable,1000);

//        ui_handler.postDelayed(check_size_timer_run,1000);
        }

        public void disable_draw()
        {
            enable_draw = false;
            ui_handler.removeCallbacks(draw_ppg_runnable);
        }

        int g_x = 0;
        int g_y = 0;
        int w = 800;
        int h = 380;

        int[] data_x = new int[sample];
        int[] data_y = new int[sample];
        float tmp = 1;
        int min;
        int max;
        int[] tmp_data_array = new int[sample];
        public void test_draw_data_own_data() throws InterruptedException {

            mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
            if (mCanvas != null) {
                Log.w("MySurfaceView", "data_list size:" + data_list.size());
                synchronized (lock) {
                    if (data_list.size() >= sample) {
                        for (int i = 0; i < sample; i++) {
                            tmp_data_array[i] = data_list.get(i);
                        }
                        min = findMin(tmp_data_array);
                        for (int i = 0; i < sample; i++) {
                            tmp_data_array[i] = tmp_data_array[i] - min;
                        }
                        max = findMax(tmp_data_array);
                        //after modify
//                        Log.d("tmp_data_list", "tmp_data_array max:" + max + ",tmp_data_array min:" + min);
                        for (int i = 0; i < sample; i++) {
                            //Log.d("consume", data_list.get(i).toString());
                            tmp = (float) tmp_data_array[i] / max;
                            data_y[i] = (int) (y_axis_positive_max * tmp);
                            //Log.d("MySurfaceView", "data_y idx " + i + "tmp:" + tmp + ",data_y:" + data_y[i] + ",y_max_value:" + y_data_max_value);
                        }
                        //remove first nth data
                        for (int i = 0; i < update_sample; i++) {
                            data_list.removeFirst();
                        }
                    }
                }
                //Log.w("MySurfaceView", "mWidth: " + mWidth + ",mHeight:" + mHeight);

                //clear canvas
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                for (int i = 0; i < sample -update_sample; i+=update_sample) {
                    mCanvas.drawLine(data_x[i], baseline-data_y[i], data_x[i+update_sample], baseline-data_y[i+update_sample], mPaint);
//                    Log.d("MySurfaceView", "x idx " + i + " data_x[i]:" + data_x[i]+ " data_y[i]:" + data_y[i]);
//                    Log.d("MySurfaceView", "y idx " + i + " data_x[i+1]:" + data_x[i+update_sample]+ " data_y[i+1]:" + data_y[i+update_sample]);
                    try {
                        Thread.sleep(0);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                //normal
//                for (int i = 0; i < sample - 1; i++) {
//                    mCanvas.drawLine(data_x[i], baseline - data_y[i], data_x[i + 1], baseline - data_y[i + 1], p);
////                    Log.d("MySurfaceView", "x idx " + i + " data_x[i]:" + data_x[i] + " data_y[i]:" + data_y[i]);
////                    Log.d("MySurfaceView", "y idx " + i + " data_x[i+1]:" + data_x[i+1]+ " data_y[i+1]:" + data_y[i+1]);
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                }
            }
        }

        int[] dataX = new int[w / 20];
        int[] dataY = new int[w / 20];


        public void test_draw_run() {

            mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
            if (mCanvas != null) {

                //Generate random
                int min = 0;
                int max = 280;

                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(6);
                dataX[0] = 0;
                for (int i = 0; i < w / 20 - 1; i++) {
                    dataX[i + 1] = (i + 1) * w / 20;
                    dataY[w / 20 - 1] = generatRandomPositiveNegitiveValue(max, min);
                    dataY[i] = dataY[i + 1];
//                    Log.d("MySurfaceView","X idx " + i + "is:" +  dataX[i]);
//                    Log.d("MySurfaceView","Y idx " + i + "is:" +  dataY[i]);
                }

                //clear canvas
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//                g_x++;
//                g_y++;
//                p.setStrokeWidth(10);
//                p.setStyle(Paint.Style.STROKE);
//                mCanvas.drawColor(Color.BLACK);
                for (int i = 0; i < w / 20 - 1; i++) {
                    ///for (int i = 0; i <  1; i++) {
                    // apply some transformation on data in order to map it correctly
                    // in the coordinates of the canvas
                    mCanvas.drawLine(dataX[i], h / 2 - dataY[i], dataX[i + 1], h / 2 - dataY[i + 1], paint);
                    //mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
                //mCanvas.drawLine(g_x, g_y, 100, 100, p);
            }
        }


        /**
         * 自定义一个方法，在画布上画一个圆
         */
        protected void Draw() {
            mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
            if (mCanvas != null) {
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setColor(Color.RED);
                paint.setStrokeWidth(10);
                paint.setStyle(Paint.Style.FILL);
                if (m_circle_r >= (getWidth() / 10)) {
                    m_circle_r = 0;
                } else {
                    m_circle_r++;
                }
                Bitmap pic = ((BitmapDrawable) getResources().getDrawable(
                        R.drawable.close)).getBitmap();
                mCanvas.drawBitmap(pic, 0, 0, paint);
                for (int i = 0; i < 5; i++)
                    for (int j = 0; j < 8; j++)
                        mCanvas.drawCircle(
                                (getWidth() / 5) * i + (getWidth() / 10),
                                (getHeight() / 8) * j + (getHeight() / 16),
                                m_circle_r, paint);
                mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
            }
        }
    }


    //内部类
    class MyView extends SurfaceView implements SurfaceHolder.Callback {

        SurfaceHolder holder;

        public MyView(Context context) {
            super(context);
            holder = this.getHolder();//获取holder
            holder.addCallback(this);
            //setFocusable(true);

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            new Thread(new MyThread()).start();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            holder.removeCallback(this);
        }

        //内部类的内部类
        class MyThread implements Runnable {

            @Override
            public void run() {
                Canvas canvas = holder.lockCanvas(null);//获取画布
                Paint mPaint = new Paint();
                mPaint.setColor(Color.BLUE);

                canvas.drawRect(new RectF(100, 100, 200, 200), mPaint);
                holder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像

            }

        }

    }

    /*find max and min and return max and min*/
    int[] findMax_Min(int[] arrays)
    {
        int max = arrays[0];
        int min = arrays[0];
        int length = arrays.length;
        for (int i = 1; i < length; i++) {
            if (arrays[i] > max) {
                max = arrays[i];
            }
            if (arrays[i] < min) {
                min = arrays[i];
            }
        }
        int[] convertedValues = new int[2];
        convertedValues[0] = min;
        convertedValues[1] = max;
        return  convertedValues;
    }

    int findMin(int[] arrays)
    {
        int min = arrays[0];
        int length = arrays.length;
        for (int i = 1; i < length; i++) {
            if (arrays[i] < min) {
                min = arrays[i];
            }
        }
        return min;
    }

    int findMax(int[] arrays)
    {
        int max = arrays[0];
        int length = arrays.length;
        for (int i = 1; i < length; i++) {
            if (arrays[i] > max) {
                max = arrays[i];
            }
        }
        return max;
    }
    int[] raw_data1 = new int[]{
            99,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,500,
            500, 500, 500, 500,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,100,
            0,20,30,40,50,60,70,80,90,99,
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
//https://blog.csdn.net/cnbloger/article/details/7404170
