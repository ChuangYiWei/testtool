package com.example.johnny_wei.testtool.drawUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.johnny_wei.testtool.test_draw;

import static com.example.johnny_wei.testtool.test_draw.static_test_draw;

public class PaintView extends View {

    private Paint mPaint;
    private int mWindowWidth;
    private int mWindowHeight;
    Path mpath;
//    HandlerThread ht_thread = new HandlerThread("auto_test");
    private Handler m_userHandler = new Handler();
    private int g_int = 0;
    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mpath =  new Path();
        //設定空心
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        //設定線寬
        mPaint.setStrokeWidth(15f);
        //設定抗鋸齒
        mPaint.setAntiAlias(true);
        //設定防抖動
        mPaint.setDither(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mWindowWidth = displayMetrics.widthPixels;
        mWindowHeight = displayMetrics.heightPixels;
        Log.d("PaintView","mWindowWidth:" + mWindowWidth);
        Log.d("PaintView","mWindowHeight:" + mWindowHeight);

        //m_userHandler.postDelayed(invokeRunnable,5000);
        start_draw();
    }

    Runnable invokeRunnable;
    public void start_draw()
    {
        invokeRunnable = new Runnable() {
            @Override
            public void run() {
                Log.d("PaintView","start_draw");
                g_int +=1;
                invalidate();
                //m_userHandler.postDelayed(invokeRunnable,100);
            }
        };
        m_userHandler.post(invokeRunnable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("PaintView"," getScrollX()"+  getScrollX());
        Log.d("PaintView"," getScrollY()" +  getScrollY());
        super.onDraw(canvas);
        mPaint.reset();

        //畫布底色
        canvas.drawColor(Color.BLACK);
        //畫筆色(灰)
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        if(g_int > 100)
        {
            g_int = 0;
            Log.w("PaintView"," reset");
            scrollTo(0,0);//move to original
            mpath.reset();
        }
        mpath.moveTo(0, mWindowHeight / 2);
//        mpath.lineTo(150, mWindowHeight / 2 + g_int);
//        mpath.lineTo(200, mWindowHeight / 2 - g_int);
//        mpath.lineTo(g_int+300, mWindowHeight / 2 -static_test_draw.data_array[g_int]);
//        mpath.lineTo(g_int+350, mWindowHeight / 2 - static_test_draw.data_array[g_int]);
        mpath.lineTo(100, -5);
        mpath.lineTo(350, mWindowHeight / 2);
        mpath.lineTo(600, -3);

        g_int++;
        canvas.drawPath(mpath, mPaint);

        scrollBy(5,0);
        //invalidate();
    }

    void Get_data()
    {
        static_test_draw.data_list.get(0);
    }
}

