package com.futurekang.buildtools.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;

//沉浸式窗口工具类  //Activity中调用
public class StatusBarUtil {

    /**
     * 1.设置状态栏为指定颜色，
     * 2.是否让当前窗口沉浸状态栏 true 否 ，false 是
     *
     * @param isW
     */
    public static void setStatusBarColor(int ColorId, boolean isW, Activity mActivity) {
        Window window = mActivity.getWindow();
        if (isW) {
            setStatusBarColor(window, ColorId);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {//低于6.0颜色不能正确控制
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                setStatusBarColor(window, Color.DKGRAY);
            }
            window.getDecorView().setFitsSystemWindows(true);
        } else {
            window.getDecorView().setFitsSystemWindows(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //设置全屏显示
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//清除指定的样式
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId
     */
    private static void setStatusBarColor(Window window, @ColorInt int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(colorId);
        }
    }
    public static void setWindowNoTitle(Activity activity){
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    public static void setAppCompatWindowNoTitle(AppCompatActivity activity){
        activity.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    }
}
