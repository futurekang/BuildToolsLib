package com.futurekang.buildtools.net.retrofit.interceptor;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author Futurekang
 * @createdate 2019/10/10 10:52
 * @description
 */
public class URLInterceptor implements Interceptor {
    private String TAG = "URLInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request oldRequest = chain.request();
        //构建新的请求，代替原来的请求
        Request.Builder requestBuilder = oldRequest.newBuilder();
        requestBuilder.method(oldRequest.method(), oldRequest.body());
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url().newBuilder();
        HttpUrl oldHttpUrl = authorizedUrlBuilder.build();
        URL orgUrl = oldHttpUrl.url();

        String url = orgUrl.getProtocol() +
                "://" + orgUrl.getAuthority() +
                orgUrl.getPath().replace("%2F", "/");

        HttpUrl newHttpUrl = HttpUrl.parse(url);
        requestBuilder.url(newHttpUrl);
        // 新的请求
        Request newRequest = requestBuilder.build();
        return chain.proceed(newRequest);
    }
}


