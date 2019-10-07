package com.example.johnny_wei.testtool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class test_surface extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test_surface);
        //setContentView(new MyView(this));
        setContentView(new MySurfaceView(this));
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

        /**
         * 当SurfaceView创建的时候，调用此函数
         */
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
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
                        Thread.sleep(100); // 让线程休息1000毫秒
                        //Draw(); // 调用自定义画画方法
                        test_draw_run();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (mCanvas != null) {
                        // mHolder.unlockCanvasAndPost(mCanvas);//结束锁定画图，并提交改变。

                    }
                }
            }
        }
        int g_x=0;
        int g_y=0;
        public void test_draw_run() {
            mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
            if (mCanvas != null) {
                g_x++;
                g_y++;
                p.setStrokeWidth(10);
                p.setStyle(Paint.Style.STROKE);
                mCanvas.drawColor(Color.BLACK);
                mCanvas.drawLine(g_x, g_y, -10+g_x, -10+g_y, p);
                mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上

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