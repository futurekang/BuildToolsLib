package com.futurekang.buildtools.net.retrofit;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;

public class BaseTask {

    private String TAG = "BaseTask";

    // 设置变量
    // 可重试次数
    private int maxConnectCount = 3;
    // 当前已重试次数
    private int currentRetryCount = 0;
    // 重试等待时间
    private int waitRetryTime = 0;

    //指定观察者和被观察者的线程
    protected <T> Observable<T> subscribeThread(Observable<T> observable) {
        return observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    protected Function<Observable<Throwable>, ObservableSource<?>> addRetryStrategy() {
        return throwableObservable ->
                throwableObservable.flatMap((Function<Throwable, ObservableSource<?>>)
                        throwable -> {
                            // 输出异常信息
                            Log.d(TAG, "发生异常 = " + throwable.toString());
                            /**
                             * 需求1：根据异常类型选择是否重试
                             * 即，当发生的异常 = 网络异常 = IO异常 才选择重试
                             */
                            if (throwable instanceof IOException) {
                                Log.d(TAG, "属于IO异常，需重试");
                                /**
                                 * 需求2：限制重试次数
                                 * 即，当已重试次数 < 设置的重试次数，才选择重试
                                 */
                                if (currentRetryCount < maxConnectCount) {
                                    // 记录重试次数
                                    currentRetryCount++;
                                    Log.d(TAG, "重试次数 = " + currentRetryCount);
                                    /**
                                     * 需求2：实现重试
                                     * 通过返回的Observable发送的事件 = Next事件，从而使得retryWhen（）重订阅，最终实现重试功能
                                     *
                                     * 需求3：延迟1段时间再重试
                                     * 采用delay操作符 = 延迟一段时间发送，以实现重试间隔设置
                                     *
                                     * 需求4：遇到的异常越多，时间越长
                                     * 在delay操作符的等待时间内设置 = 每重试1次，增多延迟重试时间1s
                                     */
                                    // 设置等待时间
                                    waitRetryTime = 1000 + currentRetryCount * 1000;
                                    Log.d(TAG, "等待时间 =" + waitRetryTime);
                                    return Observable.just(1).delay(waitRetryTime, TimeUnit.MILLISECONDS);
                                } else {
                                    // 若重试次数已 > 设置重试次数，则不重试
                                    // 通过发送error来停止重试（可在观察者的onError（）中获取信息）
                                    return Observable.error(throwable);
                                }
                            }
                            // 若发生的异常不属于I/O异常，则不重试
                            // 通过返回的Observable发送的事件 = Error事件 实现（可在观察者的onError（）中获取信息）
                            else {
                                return Observable.error(throwable);
                            }
                        });
    }


    //转换JsonBody
    public RequestBody getJSonBody(Map<String, Object> key) {
        String json = new JSONObject(key).toString();
        Log.d(TAG, "getJSonBody: " + json);
        return RequestBody.create(json, MediaType.parse("application/json; charset=utf-8"));
    }

    public String beanToJson(Object bean) {
        Gson gson = new Gson();
        String json = gson.toJson(bean);
        return json;
    }

    public Map<String, RequestBody> generateRequestBody(Map<String, Object> requestDataMap) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        File file = new File(requestDataMap.get("path").toString());
        requestBodyMap.put(String.format("file\"; fileName=\"" + file.getName()), RequestBody.create(MediaType.parse("image/jpg"), file));
        requestBodyMap.put("jsondata", toRequestBody(requestDataMap.get("jsondata").toString()));
        return requestBodyMap;
    }

    private RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
    }

    /**
     * 将文件进行转换
     *
     * @param param 为文件类型
     * @return
     */
    public RequestBody convertToRequestBody(File param) {
        return RequestBody.create(param, MediaType.parse("multipart/form-data"));
    }

    /**
     * 将Bitmap进行转换
     *
     * @param param
     * @return
     */
    public RequestBody convertToRequestBody(Bitmap param, Bitmap.CompressFormat compressFormat) {
        int quality = 100;
        return new RequestBody() {
            @Nullable
            public MediaType contentType() {
                Bitmap.CompressFormat format = compressFormat;
                String contentType;
                if (compressFormat == Bitmap.CompressFormat.PNG) {
                    contentType = "image/png";
                } else {
                    format = compressFormat;
                    contentType = compressFormat == Bitmap.CompressFormat.WEBP ? "image/webp" : "image/jpeg";
                }
                return MediaType.parse(contentType);
            }

            public void writeTo(BufferedSink var1x) {
                param.compress(compressFormat, quality, var1x.outputStream());
            }
        };

    }

    /**
     * 将参数封装成requestBody形式上传参数
     *
     * @param param 参数
     * @return RequestBody
     */
    public RequestBody convertToRequestBody(String param) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), param);
        return requestBody;
    }


    public Map<String, RequestBody> convertToPartMap(Map<String, Object> map) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof File) {
                File file = (File) value;
                requestBodyMap.put(key + "\";filename=\"" + file.getName(), convertToRequestBody(file));
            } else if (value instanceof Bitmap) {
                Bitmap bitmap = (Bitmap) value;
                requestBodyMap.put(key + "\";filename=\"file", convertToRequestBody(bitmap, Bitmap.CompressFormat.PNG));
            } else {
                requestBodyMap.put(key, convertToRequestBody(String.valueOf(value)));
            }
        }
        return requestBodyMap;
    }


}