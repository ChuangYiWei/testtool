package com.example.johnny_wei.testtool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.johnny_wei.testtool.drawUtil.PaintView;

public class test_draw extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(new Panel(this));
        PaintView pv = new PaintView(this, null);
        setContentView(pv);
//        setContentView(R.layout.activity_test_draw);
//        init();
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
    }
}


