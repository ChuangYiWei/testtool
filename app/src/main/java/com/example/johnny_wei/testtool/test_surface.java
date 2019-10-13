package com.example.johnny_wei.testtool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Random;

public class test_surface extends AppCompatActivity {
    LinearLayout layoutGet;
    LinkedList<Integer> data_list = new LinkedList<Integer>();
    private Object lock = new Object();
    int sample = 20;
    int add_sample = 2;
    int update_sample = 1;
    int g_int;
    int mWidth = 0;
    int mHeight = 0;
    int y_axis_positive_max = 0;//最大顯示正y軸座標
    int y_axis_negtive_max = 0;//最大顯示負y軸座標
    int y_data_max_value = 1;//當前y最大值,max(abs(valus)})
    int baseline=0; //起始點座標
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_surface);
        init_data();
        //setContentView(new MyView(this));
        layoutGet=(LinearLayout) findViewById(R.id.linear);
        //setContentView(new MySurfaceView(this));
        layoutGet.addView(new MySurfaceView(this));

        Log.d("test_surface", "layoutGet.getWidth() : "+ layoutGet.getWidth());
        Log.d("test_surface", "layoutGet.getLayoutParams().width : "+ layoutGet.getLayoutParams().width);
        Log.d("test_surface", "layoutGet.getLayoutParams().height : "+ layoutGet.getLayoutParams().height);

        layoutGet.measure(0,0);
        Log.d("test_surface", "layoutGet.getMeasuredWidth() : "+ layoutGet.getMeasuredWidth());
        Log.d("test_surface", "layoutGet.getMeasuredHeight() : "+ layoutGet.getMeasuredHeight());

        //textview
        TextView tv = findViewById(R.id.tv_test);
        Log.d("test_surface", "tv.getWidth : "+ tv.getWidth());
        Log.d("test_surface", "tv.getLayoutParams().width : "+ tv.getLayoutParams().width);
        Log.d("test_surface", "tv.getLayoutParams().height : "+ tv.getLayoutParams().height);

        //phone-----------------------
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int vWidth = dm.widthPixels;
        int vHeight = dm.heightPixels;

        Log.d("test_surface", "vWidth : "+ vWidth);
        Log.d("test_surface", "vHeight : "+ vHeight);

        producer_thread.start();
    }

    // Create producer thread
    Thread producer_thread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            while (true) {
                try {
                    produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SystemClock.sleep(10);
            }
        }
    });

    void init_data()
    {
        for(int i=0;i<sample;i++) {
            data_list.add(i);
        }
        Log.d("test_surface","list size:" + data_list.size());
    }

    public void produce() throws InterruptedException {
        synchronized (lock) {
//            System.out.println("produce enter");

            for (int i = 0; i < add_sample; i++) {

                //Log.d("produce", "g_int:" + g_int);
                //g_int++;
                if (i % 2 == 0) {
                    //data_list.add(generatRandomPositiveNegitiveValue(500,100));
                    data_list.add(100);
                } else {
                    //data_list.add(-100);
                    data_list.add(-100);
                }
            }
//            System.out.println("Wake up consumer thread");
        }
    }
    public int generatRandomPositiveNegitiveValue(int max , int min) {
        Random r = new Random();
        int ii = r.nextInt(max - min + 1) + min;
        return (ii-140);
    }
    public class MySurfaceView extends SurfaceView implements Runnable, SurfaceHolder.Callback {
        private SurfaceHolder mHolder; // 用于控制SurfaceView
        private Thread t; // 声明一条线程
        private volatile boolean flag; // 线程运行的标识，用于控制线程
        private Canvas mCanvas; // 声明一张画布
        private Paint p; // 声明一支画笔
        float m_circle_r = 10;

        public MySurfaceView(Context context) {
            super(context);

            mHolder = getHolder(); // 获得SurfaceHolder对象
            mHolder.addCallback(this); // 为SurfaceView添加状态监听
            p = new Paint(); // 创建一个画笔对象
            p.setColor(Color.RED); // 设置画笔的颜色为白色
            setFocusable(true); // 设置焦点

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            Log.w("MySurfaceView","onSizeChanged Height is :" + h);
            Log.w("MySurfaceView","onSizeChanged width is :" + w);
            y_axis_positive_max = h/2;
            y_axis_negtive_max = h/2;
            mWidth = w;
            mHeight = h;
            baseline = h/2;
        }

        /**
         * 当SurfaceView创建的时候，调用此函数
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.w("MySurfaceView","surfaceCreated");
            t = new Thread(this); // 创建一个线程对象
            flag = true; // 把线程运行的标识设置成true
            t.start(); // 启动线程

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
                        Thread.sleep(5); // 让线程休息1000毫秒
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
        int g_x=0;
        int g_y=0;
        int w=800;
        int h=380;

        int[] data_x=new int[sample];
        int[] data_y=new int[sample];
        public void test_draw_data_own_data() {
            Log.w("test_draw_data_own_data", "test_draw_data_own_data");
            mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
            if (mCanvas != null) {

                for (int i = 0; i <  sample ; i++) {
                    data_x[i]=(i)*(mWidth/sample);
                }
                int max = 0;
                //red color
                p.setColor(Color.RED);
                p.setStrokeWidth(6);
                synchronized (lock) {
                    if (data_list.size() >= sample) {
                        for (int i = 0; i < sample; i++) {
                            Log.d("consume", data_list.get(i).toString());
                            if(Math.abs(data_list.get(i)) > y_data_max_value) {
                                y_data_max_value = Math.abs(data_list.get(i));
                            }
                            data_y[i] = data_list.get(i);
                            if (data_y[i] > 0){
                                data_y[i] = y_axis_positive_max * data_y[i] / y_data_max_value;//限制範圍落在y顯示區間內
                            } else {
                                data_y[i] = y_axis_negtive_max * data_y[i] / y_data_max_value;//限制範圍落在y顯示區間內
                            }
                            Log.w("MySurfaceView", "data_y idx " + i + "is:" + data_y[i] + ",y_max_value:" + y_data_max_value);
                        }
                        //remove first nth data
                        for (int i = 0; i < update_sample; i++) {
                            data_list.removeFirst();
                        }
                    }

                }
                Log.w("MySurfaceView", "mWidth: " + mWidth + ",mHeight:" + mHeight);

                //clear canvas
                mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                for (int i = 0; i < sample -1; i++) {
                    mCanvas.drawLine(data_x[i], baseline-data_y[i], data_x[i+1], baseline-data_y[i+1],p);
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }
        }

        int[] dataX=new int[w/20];
        int[] dataY=new int[w/20];


        public void test_draw_run() {

            mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
            if (mCanvas != null) {

                //Generate random
                int min = 0;
                int max = 280;

                Paint paint = new Paint();
                paint.setColor(Color.RED);
                paint.setStrokeWidth(6);
                dataX[0]=0;
                for (int i = 0; i <  w/20 - 1; i++) {
                    dataX[i+1]=(i+1)*w/20;
                    dataY[w/20 - 1]=generatRandomPositiveNegitiveValue(max ,min);
                    dataY[i]=dataY[i+1];
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
                for (int i = 0; i <  w/20 -1; i++) {
                ///for (int i = 0; i <  1; i++) {
                    // apply some transformation on data in order to map it correctly
                    // in the coordinates of the canvas
                    mCanvas.drawLine(dataX[i], h/2-dataY[i], dataX[i+1], h/2-dataY[i+1],paint);
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
    class MyView extends SurfaceView implements SurfaceHolder.Callback{

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
        class MyThread implements Runnable{

            @Override
            public void run() {
                Canvas canvas = holder.lockCanvas(null);//获取画布
                Paint mPaint = new Paint();
                mPaint.setColor(Color.BLUE);

                canvas.drawRect(new RectF(100,100,200,200), mPaint);
                holder.unlockCanvasAndPost(canvas);//解锁画布，提交画好的图像

            }

        }

    }
}
//https://blog.csdn.net/cnbloger/article/details/7404170