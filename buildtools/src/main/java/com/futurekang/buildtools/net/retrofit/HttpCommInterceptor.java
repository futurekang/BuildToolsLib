package com.futurekang.buildtools.net.retrofit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


//公共参数拦截器
public class HttpCommInterceptor implements Interceptor {
    private String TAG = "HttpCommInterceptor";
    private Map<String, String> headerParamMap = new HashMap<>();
    private Map<String, String> queryParamMap = new HashMap<>();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        //构建新的请求，代替原来的请求
        Request.Builder requestBuilder = oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());

        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder();

        //添加header
        if (headerParamMap.size() > 0) {
            for (Map.Entry<String, String> params : headerParamMap.entrySet()) {
                requestBuilder.addHeader(params.getKey(), params.getValue());
            }
        }
        //添加公共参数
        if (queryParamMap.size() > 0) {
            for (Map.Entry<String, String> params : queryParamMap.entrySet()) {
                authorizedUrlBuilder.addQueryParameter(params.getKey(), params.getValue());
            }
        }
        requestBuilder.url(authorizedUrlBuilder.build());
        // 新的请求
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }

    //这里使用建造者模式
    public static class Builder {
        HttpCommInterceptor commInterceptor;

        public Builder() {
            commInterceptor = new HttpCommInterceptor();
        }

        public Builder addHeaderParam(String key, Object value) {
            commInterceptor.headerParamMap.put(key, String.valueOf(value));
            return this;
        }

        public Builder addQueryParam(String key, Object value) {
            commInterceptor.queryParamMap.put(key, String.valueOf(value));
            return this;
        }

        public HttpCommInterceptor build() {
            return commInterceptor;
        }

    }
}


