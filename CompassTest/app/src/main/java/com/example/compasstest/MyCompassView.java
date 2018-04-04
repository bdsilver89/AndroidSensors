package com.example.compasstest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MyCompassView extends View {

    private Paint paint;
    private Paint paint2;
    private float position = 0;

    public MyCompassView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setTextSize(25);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);

        paint2 = new Paint();
        paint2.setAntiAlias(true);
        paint2.setStrokeWidth(2);
        paint2.setTextSize(25);
        paint2.setStyle(Paint.Style.STROKE);
        paint2.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int xPoint = getMeasuredWidth() / 2;
        int yPoint = getMeasuredHeight() / 2;

        float radius = (float) (Math.max(xPoint, yPoint) * 0.6);
        canvas.drawCircle(xPoint, yPoint, radius, paint2);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        canvas.drawLine(
                xPoint,
                yPoint,
                (float)(xPoint + radius * Math.sin((double)(-position) / 180 * 3.143)),
                (float)(yPoint - radius * Math.cos((double)(-position) / 180 * 3.143)),
                paint2);

        canvas.drawText(String.valueOf(position), xPoint, yPoint, paint2);
    }

    public void updateData(float position) {
        this.position = position;
        invalidate();
    }
}
