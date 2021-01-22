package com.futurekang.buildtools.net.retrofit.exception;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

public class NetWorkExceptionUtil {
    private static String TAG = "NetWorkExceptionUtil";

    /**
     * 统一错误处理 -> 汉化了提示，以下错误出现的情况 (ps:不一定百分百按我注释的情况，可能其他情况)
     */
    public static Throwable unifiedError(Context context, Throwable e) {
        Throwable throwable;
        if (e instanceof UnknownHostException || e instanceof HttpException) {
            //无网络的情况，或者主机挂掉了。返回，
            if (isNetworkAvailable(context)) {
                //无网络
                throwable = new Throwable("当前网络不可用，请检查您的网络设置", e.getCause());
            } else {
                //主机挂了，也就是你服务器关了
                throwable = new Throwable("服务器开小差,请稍后重试！", e.getCause());
            }
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof SocketException) {
            if (isNetworkAvailable(context)) {
                //无网络
                throwable = new Throwable("当前网络不可用，请检查您的网络设置", e.getCause());
            } else {
                //连接超时等
                throwable = new Throwable("网络连接超时，请检查您的网络状态！", e.getCause());
            }
        } else if (e instanceof NumberFormatException || e instanceof IllegalArgumentException || e instanceof JsonSyntaxException) {
            //也就是后台返回的数据不一致，导致解析异常
            throwable = new Throwable("未能请求到数据!", e.getCause());
        } else if (e instanceof BusinessException) {
            //业务流程异常
            throwable = new Throwable(e.getMessage(), e.getCause());
        } else {
            //其他 未知
            throwable = new Throwable("未知错误", e.getCause());
            Log.d(TAG, "unifiedError: ");
        }
        return throwable;
    }

    /**
     * 判断是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity == null) {
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].isAvailable()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
