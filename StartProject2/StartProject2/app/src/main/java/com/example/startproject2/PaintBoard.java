package com.example.startproject2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

public class PaintBoard extends View {
    Canvas mCanvas;
    Bitmap mBitmap;
    Paint mPaint;
    float lastX, lastY;
    Path mPath = new Path();
    static final float TOUCH_TOLERANCE = 8;

    public PaintBoard(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(3.0F);
        this.lastX = -1;
        this.lastY = -1;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
       if(mCanvas==null) {
           Bitmap img = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
           Canvas canvas = new Canvas();
           canvas.setBitmap(img);
           canvas.drawColor(Color.WHITE);
           mBitmap = img;
           mCanvas = canvas;
       }
    }
    public void changeBitmap(Bitmap bitmap) {
        Canvas canvas = new Canvas();
        Bitmap copyBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        canvas.setBitmap(copyBitmap);
        mBitmap = copyBitmap;
        mCanvas = canvas;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                mPath.rewind();
            case MotionEvent.ACTION_MOVE:
                processMove(event);
                break;
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
        }
        invalidate();
        return true;
    }
    public  void clear(){
        mBitmap.eraseColor(Color.WHITE);

        invalidate();




    }
    private void touchDown(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        lastX = x;
        lastY = y;
        mPath.moveTo(x, y);
        mCanvas.drawPath(mPath, mPaint);
    }

    private void processMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float dx = Math.abs(x - lastX);
        float dy = Math.abs(y - lastY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            float cX = (x + lastX) / 2;
            float cY = (y + lastY) / 2;
            mPath.quadTo(lastX, lastY, cX, cY);
            lastX = x;
            lastY = y;
            mCanvas.drawPath(mPath, mPaint);
        }

    }
}