package com.futurekang.buildtools.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import static android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;

//沉浸式窗口工具类
// Activity中调用

/**
 * @View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN 让app的内容顶部延伸到状态栏的位置，状态栏悬浮在内容上方
 * @View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR 设置状态栏的图标为暗色
 * @
 */
public class StatusBarUtil {

    /**
     * @param color  状态栏的颜色
     * @param enable 是否开启状态栏沉浸(这里其实隐藏了actionBar，但是状态栏还在,开启前确认布局顶部是自己要的颜色)
     * @param drak   切换图标颜色（不能同FLAG_TRANSLUCENT_STATUS（setStatusBarTransparent） 同时为true，会失效）
     */
    public static void setStatusBarStyle(Activity mActivity, @ColorInt int color, boolean enable, boolean drak) {
        clearFlag(mActivity);
        //注三个方法调用顺序不能更改
        setStatusBarColor(mActivity, color);
        setStatusDarkEnable(mActivity, drak);
        setTransparentEnable(mActivity, enable);
    }

    /**
     * 清理之前设置的Flag
     *
     * @param activity
     */
    public static void clearFlag(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(0);
    }

    /**
     * 沉浸式效果
     *
     * @param mActivity
     * @param enable
     */
    public static void setTransparentEnable(Activity mActivity, boolean enable) {
        Window window = mActivity.getWindow();
        View decorView = mActivity.getWindow().getDecorView();
        int originFlag = mActivity.getWindow().getDecorView().getSystemUiVisibility();
        if (enable) {
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(originFlag | option);
            window.getDecorView().setFitsSystemWindows(false);
        } else {
//            decorView.setSystemUiVisibility(originFlag | View.SYSTEM_UI_FLAG_VISIBLE ^ View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.getDecorView().setFitsSystemWindows(true);
        }
    }

    /**
     * 设置状态栏色调风格  亮色/暗色
     *
     * @param mActivity
     * @param dark      暗色
     */
    public static void setStatusDarkEnable(Activity mActivity, boolean dark) {
        Window window = mActivity.getWindow();
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                int originFlag = mActivity.getWindow().getDecorView().getSystemUiVisibility();
                //1、设置状态栏文字深色，同时保留之前的flag
                mActivity.getWindow().getDecorView().setSystemUiVisibility(originFlag | View
                        .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {//低于6.0设置灰色背景
                setStatusBarColor(mActivity, Color.GRAY);
            }
        } else {
            int originFlag = mActivity.getWindow().getDecorView().getSystemUiVisibility();
            //使用异或清除  SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.getWindow().getDecorView().setSystemUiVisibility(originFlag ^ View
                        .SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            //设置状态栏文字颜色及图标为浅色
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }


    /**
     * 设置状态栏背景色透明(注：不能同以上其他方法一起使用会有冲突，建议每个activity请单独设置，继承baseActivity可能会有冲突）
     */
    public static void setStatusBarTransparent(Activity mActivity) {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = mActivity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            //也可以设置成灰色透明的，比较符合Material Design的风格
            mActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
    }


    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId
     */
    public static void setStatusBarColor(Activity mActivity, @ColorInt int colorId) {
        Window window = mActivity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorId);
        }
    }


    public static void setWindowNoTitle(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    public static void setAppCompatWindowNoTitle(AppCompatActivity activity) {
        activity.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
