package com.example.johnny_wei.testtool.drawUtil;

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

public class TestPathView extends View {
    private final String TAG = getClass().getSimpleName();
    private Path mPath;
    private Paint mPaint;
    int baseline = 0; //起始點座標
    int sample = 10;
    int[] data_x = new int[sample];
    int mWidth = 0;
    int mHeight = 0;

    //手指按下的位置
    private float startX,startY;
    public  Handler ui_handler = new Handler(Looper.getMainLooper());
    public TestPathView(Context context) {
        super(context);
        init();
        ui_handler.postDelayed(ui_runnable,1000);

    }

    private Runnable ui_runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("MovePathView", "invokeRun");
            try {
                mydraw();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ui_handler.postDelayed(ui_runnable,1000);
        }
    };

    public void startDraw()
    {

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
        int mWidth = w;
        int mHeight = h;
        baseline = mHeight/2;
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

    int[] raw_data = new int[]{
            100, -100,  100, -100, 100, -100 ,100, -100,100,-100,
            10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
            10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
            10, 20, 30, 40, 50, 60, 70, 80, 90, 100,
    };
}