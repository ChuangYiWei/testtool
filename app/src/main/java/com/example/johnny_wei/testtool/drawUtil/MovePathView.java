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

public class MovePathView extends View {

    private Path mPath;
    private Paint mPaint;
    //手指按下的位置
    private float startX,startY;
    public  Handler ui_handler = new Handler(Looper.getMainLooper());
    public MovePathView(Context context) {
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
        Log.d("MovePathView", "sample_idx:"+sample_idx);
        if(sample_idx ==0)
        {
            mPath.moveTo(0,106);
        }
        else if(sample_idx ==1)
        {
            mPath.lineTo(300,436);
        }
        else if(sample_idx ==2)
        {
            mPath.lineTo(500,106);
        }
        else if(sample_idx ==3)
        {
            mPath.lineTo(700,436);
        }
        else if(sample_idx ==4)
        {
            sample_idx =0;
            mPath.reset();
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

    public MovePathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MovePathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
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

}
