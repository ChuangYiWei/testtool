package com.example.johnny_wei.testtool.drawUtil;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class PaintView extends View {

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //畫筆
        Paint paint = new Paint();

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

