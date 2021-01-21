package com.futurekang.buildtools.anim;

import android.graphics.Matrix;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class TVOffAnimation extends Animation {
    private int halfWidth;
    private int halfHeight;

    @Override
    public void initialize(int width, int height, int parentWidth,
                           int parentHeight) {

        super.initialize(width, height, parentWidth, parentHeight);
        //保存View的中心点
        halfWidth = width / 2;
        halfHeight = height / 2;
        setInterpolator(new AccelerateDecelerateInterpolator());
        setCloseListener();
    }

    private void setCloseListener() {
        setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (closeListener != null) {
                    closeListener.onClose();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        final Matrix matrix = t.getMatrix();
        if (interpolatedTime < 0.8) {
            matrix.preScale(1 + 0.625f * interpolatedTime, 1 - interpolatedTime / 0.8f + 0.01f, halfWidth, halfHeight);
        } else {
            matrix.preScale(7.5f * (1 - interpolatedTime), 0.01f, halfWidth, halfHeight);
        }
    }

    public interface OnCloseListener {
        void onClose();
    }

    private OnCloseListener closeListener;

    public void setCloseListener(OnCloseListener closeListener) {
        this.closeListener = closeListener;
    }
}
