package com.futurekang.buildtools.view.button;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class SwitchButton extends View {
    float switchWidth = 150f;//按钮的长度
    float switchHeight = 70f;
    float radius = 0;//圆角半径为高度的一半
    Paint mPaint = new Paint();
    String white = "#FFFFFF",
            springGreen = "#00FF7F",
            lightGrey = "#D3D3D3";
    float pointRadius = 0f;
    float CircleX = 0f, CircleY = 0f;//小圆点中心坐标
    float CircleMargin = 4f;//小圆点的外边距
    float endX = 0f;
    float startX = 0f;
    float left = 100f;
    float currentX = 0;
    float pointBackGroundColorW = left;

    public SwitchButton(Context context) {
        super(context);
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        reSetParameter();
    }

    //小圆点可移动的区间为  外边距 + 圆点半径 到 控件宽度 - 外边距- 圆点半径的位置
    //              startX =    CircleMargin + pointRadius ;
    //              endX = switchWidth - CircleMargin - pointRadius;
    public void reSetParameter() {
        switchWidth = switchWidth + left;//
        radius = switchHeight / 2;//圆角半径为高度的一半
        pointRadius = switchHeight / 2 - CircleMargin;//小圆点的半径 =  控件高度一半 - 外边距
        CircleX = radius + CircleMargin;//X轴初始位置 = 控件小圆点半径+ 外边距
        CircleY = switchHeight / 2;//Y轴为控件高度的一半
        startX = CircleMargin + pointRadius + left;//小圆点可以移动的最小值
        endX = switchWidth - CircleMargin - pointRadius;//小圆点可以移动的最大值
        currentX = startX;
        setSwitchButtonStatus();
    }

    public void setChecked(boolean flag) {
        isChecked = flag;
        setSwitchButtonStatus();
    }

    public boolean isChecked() {
        return isChecked;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制按钮底色
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(lightGrey));//设置底色为灰色
        RectF rectF = new RectF(left, 0, switchWidth, switchHeight);
        canvas.drawRoundRect(rectF, radius, radius, mPaint);//绘制一个圆角矩形 背景
        mPaint.reset();
        //绘制小圆点进度 渐变底色
        mPaint.setStyle(Paint.Style.FILL);//设置画笔的风格为填充
        if (isChecked) {
            mPaint.setColor(Color.parseColor(springGreen));//
        } else {
            mPaint.setColor(Color.parseColor(lightGrey));//
        }
        RectF rectF1 = new RectF(left, 0, pointBackGroundColorW, switchHeight);
        canvas.drawRoundRect(rectF1, radius, radius, mPaint);

        mPaint.setColor(Color.parseColor(white));//设置圆点的颜色
        //绘制小圆点
        canvas.drawCircle(currentX, CircleY, pointRadius, mPaint);

    }

    private boolean isChecked = false;

    private void setSwitchButtonStatus() {
        if (isChecked) {
            pointBackGroundColorW = switchWidth;
            currentX = endX;
            isChecked = true;
        } else {
            currentX = startX;
            pointBackGroundColorW = left;
            isChecked = false;
        }
        setAnimator("currentX", 150, currentX);
        setAnimator("pointBackGroundColorW", 150, pointBackGroundColorW);
    }

    private void setAnimator(String propertyName, long duration, float... values) {
        @SuppressLint("ObjectAnimatorBinding")
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, propertyName, values);
        objectAnimator.setDuration(duration);//设置动画时长
        objectAnimator.setInterpolator(new DecelerateInterpolator());//差速器设置 持续减速直到 0
        objectAnimator.start();//开始动画
    }

    public float getPointBackGroundColorW() {
        return pointBackGroundColorW;
    }

    public void setPointBackGroundColorW(float pointBackGroundColorW) {
        this.pointBackGroundColorW = pointBackGroundColorW;
        invalidate();
    }

    public float getCurrentX() {
        return startX;
    }

    public void setCurrentX(float currentX) {
        this.currentX = currentX;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //在这里设置动画如果是选中那么设置关闭动画，如果没选中设置开启动画。
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if (x + getLeft() < getRight() && y + getTop() < getBottom()) {
                    if (isChecked) {
                        isChecked = false;
                    } else {
                        isChecked = true;
                    }
                    setSwitchButtonStatus();
                    onClickListener.onClick(this);
                }
                break;
        }
        return true;
    }

    OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View view);
    }
}
