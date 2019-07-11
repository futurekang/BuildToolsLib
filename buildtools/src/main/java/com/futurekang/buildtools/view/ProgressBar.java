package com.futurekang.buildtools.view;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;

public class ProgressBar extends View {


    String TAG = "MYLOG-ProgressBar";

    private float margin = 10f;//控件的外边距
    private float padding = 30f;//控件的内边距
    private int max = 360;//最大值
    private int progress = 0;//进度
    private float pbHeight = 0f;//进度条的矩形的宽高
    private float pbWidth = 0f;
    private float pbRadius = 0f;//半径
    private boolean flag = true;
    private float cx = 150f;
    private float cy = 150f;//圆心
    private float rTop = 0f;//矩形的上边距
    private float rLeft = 0f;
    private float rRight = 0f;
    private float rBottom = 0f;
    private long speed = 3;//旋转速度
    private float lineHeight = 0; //基线的高度


    private Paint mPaint = new Paint();//矩形的画笔
    private Paint linePaint = new Paint();//点状圆环的画笔
    private Paint arcPaint = new Paint();//底部圆环的画笔
    private Paint arcPaint2 = new Paint();//进度圆环的画笔
    private Paint textPaint = new Paint();//文字的画笔
    private Paint circlePaint = new Paint();//画圆
    private Style style = Style.ARC_TEXT;

    public ProgressBar(Context context) {
        super(context);
        initPaint();
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);//宽的测量大小，模式
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);//高的测量大小，模式
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

//        int w = 300;   //定义测量宽，高(不包含测量模式),并设置默认值，查看View#getDefaultSize可知
//        int h = 300;
//
//        //处理wrap_content的几种特殊情况
//        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
//            w = 300;  //单位是px
//            h = 300;
//        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
//            //只要宽度布局参数为wrap_content， 宽度给固定值200dp(处理方式不一，按照需求来)
//            w = 300;
//            //按照View处理的方法，查看View#getDefaultSize可知
//            h = heightSpecSize;
//        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
//            w = widthSpecSize;
//            h = 300;
//        }
//        //给两个字段设置值，完成最终测量
//        setMeasuredDimension(w, h);

    }

    /**
     * 初始化参数
     */
    private void initParams() {
        pbWidth = getWidth();
        pbHeight = pbWidth;
        pbRadius = 150;
        rTop = 0;
        rLeft = 0;
        rRight = 300;
        rBottom = 300;
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        Log.d(TAG, "initPaint: ");
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20f);

        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(20f);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcPaint.setColor(Colors.darkgray);

        arcPaint2.setAntiAlias(true);
        arcPaint2.setStyle(Paint.Style.STROKE);
        arcPaint2.setStrokeWidth(20f);
        arcPaint2.setStrokeCap(Paint.Cap.ROUND);
        arcPaint2.setColor(Color.WHITE);
        arcPaint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
//        Shader arcShader = new SweepGradient(cx, cy, Color.GREEN, Color.RED);
//        arcPaint2.setShader(arcShader);

        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setStrokeWidth(8f);
        linePaint.setAntiAlias(true);
        linePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        Shader lineShader = new SweepGradient(0, 0, Colors.white, Colors.darkgray);
        linePaint.setShader(lineShader);

        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Colors.white);
        textPaint.setStrokeWidth(10);
        textPaint.setTextSize(40);

        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Colors.orange);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initParams();//初始化参数
        //画布移到中间的位置
        canvas.translate(getWidth() / 2 - 150, getHeight() / 2 - 150);
        switch (style) {
            case LOOP:
                drawRoundRect(canvas);//圆角矩形
                drawLine(canvas);//画刻度进度条
                break;
            case ARC:
                drawRoundRect(canvas);//圆角矩形
                drawNormalArc(canvas);//画底部圆环
                drawColorArc(canvas);//进度条圆环
                break;
            case ARC_TEXT:
                drawRoundRect(canvas);//圆角矩形
                drawNormalArc(canvas);//画底部圆环
                drawColorArc(canvas);//进度条圆环
                drawText(canvas);//文字
                break;
            case CIRCLE:
                drawCircle(canvas);
                drawText(canvas);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged: ");
        postInvalidateDelayed(1);
    }

    private void drawCircle(Canvas canvas) {
        int cRadius = (int) (pbRadius - padding);
        int circleHeight = cRadius * 2;
        float current = (float) progress / max * circleHeight;
        lineHeight = padding + (circleHeight - current);
        canvas.drawCircle(cx, cy, pbRadius - padding, circlePaint);
        Paint paint = new Paint();
        paint.setColor(Colors.limegreen);
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        float height = 0;
        for (int i = 0; i <= 180; i++) {
            Float[] startXY = getCircleXY(i, cx, cy, pbRadius - padding);
            Float[] stopXY = getCircleXY(360 - i, cx, cy, pbRadius - padding);
            if (lineHeight > stopXY[1] && lineHeight < startXY[1]) {
                height = lineHeight;
            } else {
                if (lineHeight < cy) {
                    height = stopXY[1];
                } else {
                    height = startXY[1];
                }
            }
            float[] floats = new float[]{startXY[0], startXY[1], stopXY[0], height};
            canvas.drawLines(floats, paint);
        }

    }

    /**
     * 绘制百分比文字
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        DecimalFormat df = new DecimalFormat("0");
        String current = df.format((float) progress / max * 100);
        String text = current + "%";
        float length = textPaint.measureText(text);
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        int height = rect.height();
        canvas.drawText(text, cx - length / 2, cy + height / 2, textPaint);
    }

    /**
     * 绘制背景圆角矩形
     *
     * @param canvas
     */
    private void drawRoundRect(Canvas canvas) {
        RectF rectF = new RectF(rLeft, rTop, rRight, rBottom);
        mPaint.setColor(Colors.darkgray);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, 30, 30, mPaint);//画一个圆角矩形
    }

    /**
     * 绘制点状进度条
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        canvas.rotate(progress, 150, 150);//以圆心为原点旋转  动态
        float lineRadius = pbRadius / 4;
        for (int i = 360; i >= 0; i--) {
            if (i % 30 == 0) {
                Float[] mXY = getCircleXY(i, 150, 150, lineRadius + 10);
                Float[] pXY = getCircleXY(i, 150, 150, lineRadius - 10);
                float[] pts = new float[]{mXY[0], mXY[1], pXY[0], pXY[1]};
                canvas.drawLines(pts, linePaint);
            }
        }
    }

    /**
     * 绘制底部的圆环
     *
     * @param canvas
     */
    private void drawNormalArc(Canvas canvas) {
        RectF rectF = new RectF(rLeft + padding, rTop + padding, rRight - padding, rBottom - padding);
        canvas.drawArc(rectF, 150, max, false, arcPaint);
    }

    /**
     * 绘制进度圆环
     *
     * @param canvas
     */
    private void drawColorArc(Canvas canvas) {
        DecimalFormat df = new DecimalFormat("0");
        float unit = (float) 360 / 100;
        int current = Integer.valueOf(df.format((float) progress / max * unit * 100));
        RectF rectF = new RectF(rLeft + padding, rTop + padding, rRight - padding, rBottom - padding);
        canvas.drawArc(rectF, 150, current, false, arcPaint2);
    }

    /**
     * 开始旋转
     */
    public void start() {
        if (null == runnable) {
            loading();
        }
        if (style == Style.LOOP && runnable.getState() != Thread.State.RUNNABLE) {
            runnable.start();
        }
    }

    private Thread runnable;

    /**
     * 创建旋转线程
     */
    private void loading() {
        runnable = new Thread(new Runnable() {
            @Override
            public void run() {
                int angle = 1;
                while (flag && style == Style.LOOP) {
                    angle++;
                    setProgress(angle);
                    if (angle == max) {
                        angle = 1;
                    }
                    SystemClock.sleep(speed);
                }
            }
        });
    }

    public void stop() {
        if (runnable.getState() == Thread.State.RUNNABLE) {
            runnable.stop();
        }
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        postInvalidate();
    }


    private void setAnimator(String propertyName, long duration, float... values) {
        @SuppressLint("ObjectAnimatorBinding")
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, propertyName, values);
        objectAnimator.setDuration(duration);//设置动画时长
        objectAnimator.setInterpolator(new OvershootInterpolator());//差速器设置 持续减速直到 0
        objectAnimator.start();//开始动画
    }

    private int oldProgress = 0;

    private void setValueAnimator(float value) {
        this.oldProgress = this.progress;
        ValueAnimator animator = ValueAnimator.ofFloat(oldProgress, value);
        animator.setInterpolator(new AnticipateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                setProgress((int) val);
            }
        });
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 已知度数和圆心坐标,半径
     * 求圆上某一点的坐标
     *
     * @param degree
     * @param cx
     * @param cy
     */
    private Float[] getCircleXY(int degree, float cx, float cy, float radius) {
        float x = (float) (cx + radius * Math.sin((-degree + 90) * Math.PI / 180));
        float y = (float) (cy + radius * Math.cos((-degree + 90) * Math.PI / 180));
        return new Float[]{x, y};
    }


    public static class Colors {

        public final static int white = Color.parseColor("#FFFFFF"),
                orange = Color.parseColor("#FFA500"),
                deepskyblue = Color.parseColor("#00BFFF"),
                limegreen = Color.parseColor("#32CD32"),
                black = Color.parseColor("#000050"),
                lightcyan = Color.parseColor("#E0FFFF"),
                red = Color.parseColor("#FF0000"),
                yellow = Color.parseColor("#FFFF00"),
                darkgray = Color.parseColor("#A9A9A9");
    }

    public enum MyColor {
        WHITE("#FFFFFF"),
        ORANGE("#FFA500"),
        DEEPSKYBLUE("#00BFFF"),
        LIMEGREEN("#32CD32"),
        BLACK("#000050"),
        LIGHTCYAN("#E0FFFF"),
        RED("#FF0000"),
        YELLOW("#FFFF00"),
        DARKGRAY("#A9A9A9");
        private String value;

        MyColor(String value) {
            this.value = value;
        }

        private String getValue() {
            return this.value;
        }
    }

    public enum Style {
        LOOP(1), ARC(2), ARC_TEXT(3), CIRCLE(4);

        private Style(int i) {
        }
    }

    public enum Value {
        MAX_CIRCLE,
        MAX_ARC;
    }

    /**
     * 将bitmap中的某种颜色值替换成新的颜色
     *
     * @param
     * @param newColor
     * @return
     */
    public static Bitmap replaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor) {
        //相关说明可参考 http://xys289187120.blog.51cto.com/3361352/657590/
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        //循环获得bitmap所有像素点
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        int mArrayColorLengh = mBitmapWidth * mBitmapHeight;
        int[] mArrayColor = new int[mArrayColorLengh];
        int count = 0;
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                int color = mBitmap.getPixel(j, i);
                //将颜色值存在一个数组中 方便后面修改
                if (i >= 24 && j > 24) {
                    mBitmap.setPixel(j, i, newColor);  //将白色替换成透明色
                } else {
                    mBitmap.setPixel(j, i, oldColor);
                }
                //获得Bitmap 图片中每一个点的color颜色值
                //将需要填充的颜色值如果不是
                //在这说明一下 如果color 是全透明 或者全黑 返回值为 0
                //getPixel()不带透明通道 getPixel32()才带透明部分 所以全透明是0x00000000
                //而不透明黑色是0xFF000000 如果不计算透明部分就都是0了
                //将颜色值存在一个数组中 方便后面修改
                //  //将白色替换成透明色
            }
        }
        return mBitmap;
    }
}
;