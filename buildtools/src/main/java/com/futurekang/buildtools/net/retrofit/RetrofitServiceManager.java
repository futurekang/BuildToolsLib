package com.futurekang.buildtools.net.retrofit;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServiceManager {
    private static final int DEFAULT_TIME_OUT = 15;
    private static final int DEFAULT_ACTION_TIME_OUT = 15;
    private Retrofit retrofit;


    public RetrofitServiceManager(String baseUrl, Map<Integer, Interceptor> interceptorMap, List<Converter.Factory> factories) {
        //通过构建器 创建OKHttp客户端
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //链接超时时间
        builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS);
        //写入超时时间
        builder.writeTimeout(DEFAULT_ACTION_TIME_OUT, TimeUnit.SECONDS);
        //读取超时时间
        builder.readTimeout(DEFAULT_ACTION_TIME_OUT, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        if (null != interceptorMap) {//添加拦截器
            Iterator iterator = interceptorMap.keySet().iterator();
            while (iterator.hasNext()) {
                builder.addInterceptor(interceptorMap.get(iterator.next()));
            }
        }

        Retrofit.Builder myRetrofit = new Retrofit.Builder().client(builder.build());

        if (null != factories && factories.size() > 0) {
            for (Converter.Factory factory : factories) {
                myRetrofit.addConverterFactory(factory);
            }
        }
        myRetrofit.addConverterFactory(new NullOnEmptyConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl);


        retrofit = myRetrofit.build();
    }


    private static RetrofitServiceManager serviceManager;

    //单例
    public static RetrofitServiceManager getInstance(String baseUrl) {
        return getInstance(baseUrl, null, null);
    }

    //单例
    public static RetrofitServiceManager getInstance(String baseUrl, Map<Integer, Interceptor> interceptorMap) {
        return getInstance(baseUrl, interceptorMap, null);
    }

    public static RetrofitServiceManager getInstance(String baseUrl, Map<Integer, Interceptor> interceptorMap, List<Converter.Factory> factories) {
        if (serviceManager == null) {
            synchronized (RetrofitServiceManager.class) {
                if (serviceManager == null) {
                    serviceManager = new RetrofitServiceManager(baseUrl, interceptorMap, factories);
                }
            }
        }
        return serviceManager;
    }

    public <T> T create(Class<T> service) {
        return retrofit.create(service);
    }
}
