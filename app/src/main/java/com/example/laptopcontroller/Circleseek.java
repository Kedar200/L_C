package com.example.laptopcontroller;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class Circleseek extends View {

    private Paint circlePaint;
    private Paint progressPaint;
    private Paint thumbpaint;
    private Paint solidpaint;

    private float radius;
    private float centerX;
    private float centerY;

    private float progress = 0f;
    private float maxProgress = 100f;
    private OnProgressChangeListener progressChangeListener;




    public Circleseek(Context context) {
        super(context);
        init();
    }

    public Circleseek(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Circleseek(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setOnProgressChangeListener(null);
    }

    private void init() {
        // Initialize the paint for drawing the circle

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setTextSize(50);

        // Initialize the paint for drawing the progress arc
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setTextSize(70);
        progressPaint.setColor(Color.BLACK);

        solidpaint=new Paint();
        solidpaint.setStyle(Paint.Style.FILL);
        solidpaint.setColor(Color.BLACK);

        thumbpaint=progressPaint;

        thumbpaint.setStrokeWidth(10);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);

        // Calculate the radius and center coordinates based on the view size
        radius = Math.min(width, height) / 2f-50;
        centerX = width / 2f;
        centerY = height / 2f;
        progressPaint.setStrokeWidth(10%radius);


        circlePaint.setStrokeWidth(1%radius);
        solidpaint.setShadowLayer(40%radius,0,0,Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        // Draw the circle
        canvas.drawCircle(centerX, centerY, radius, circlePaint);
        canvas.drawCircle(centerX,centerY,(radius-(30%radius)),solidpaint);
        canvas.drawText(String.valueOf((int)progress),centerX-10%centerX,centerY-10%centerY,progressPaint);

        // Draw the progress arc
        float startAngle = 0f; // Start angle for the progress arc (top of the circle)
        float sweepAngle = (progress / maxProgress * 350f) ; // Calculate the sweep angle based on progress
        sweepAngle=Math.min(sweepAngle,350);
        RectF arcRect = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(arcRect, startAngle, sweepAngle, false, progressPaint);

        // Draw the thumb
        float thumbAngle = startAngle + sweepAngle; // Calculate the angle at the current progress position
        float thumbX = centerX + radius * (float) Math.cos(Math.toRadians(thumbAngle)+0.0872665f);
        float thumbY = centerY + radius * (float) Math.sin(Math.toRadians(thumbAngle)+0.0872665f);

        canvas.drawCircle(thumbX, thumbY, 15, thumbpaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float touchAngle = getAngleForPoint(x, y);
        if(Math.sqrt(Math.pow(x-centerX,2)+Math.pow(y-centerY,2))<97%radius){
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Update the progress based on the touch position
                if(progress-( touchAngle) / 360f * maxProgress>10 || progress-( touchAngle) / 360f * maxProgress<-10) {

                }
                else{
                progress =( touchAngle) / 360f * maxProgress;
                progressChangeListener.onProgressChanged(progress);
                invalidate();
                return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                progressChangeListener.onstopchange(progress);

        }

        return super.onTouchEvent(event);
    }

    private float getAngleForPoint(float x, float y) {
        float dx = x - centerX;
        float dy = y - centerY;
        double radians = Math.atan2(dy, dx);
        float angle = (float) Math.toDegrees(radians);
        if(angle>360){
        angle = angle  % 360;
        }
        else if(angle<0){
            angle=angle+360;
        }
        return angle;
    }


    public void setOnProgressChangeListener(OnProgressChangeListener listener) {
        this.progressChangeListener = listener;
    }

    public void setProgress(final int targetBrightness) {
        final ValueAnimator animator = ValueAnimator.ofInt((int) progress, targetBrightness);
        animator.setDuration(targetBrightness*5); // Set the duration of the animation (in milliseconds)
        animator.setInterpolator(new AccelerateDecelerateInterpolator()); // Apply ease-in and ease-out effect

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                progressChangeListener.onProgressChanged(progress);
                progress = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.start();
    }


    public interface OnProgressChangeListener {
        void onProgressChanged(float progress);
        void onstopchange(float progress);
    }

}
