# buildtoolslib

#### 介绍
快速构建项目工具库
部分代码收集于网络，感谢原作者们。
整合工具类的清单：

（一）适配器：
CommAdapter :ListView万能适配器
RCommAdapter ：RcyclerView万能适配器

（二）动画：
TVOffAnimation ：电视机关闭动画效果

（三）网络：
1： okhttp封装 
 使用：okhttp.getInstance...
2:  retrofit2 +Rxjava +OkHttp3 封装
使用 ：
 	HttpCommInterceptor.Builder httpCommInterceptor = new HttpCommInterceptor.Builder()
                .addQueryParam("key", ApiConfig.API_KEY);
        Map<Integer, Interceptor> interceptorMap = new HashMap<>();
        interceptorMap.put(1, httpCommInterceptor.build());
        lotteryService = RetrofitServiceManager
                .getInstance(ApiConfig.HOST, interceptorMap)
                .create(LotteryService.class);


#### 安装教程

How to
To get buildtoolslib into your build:

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.futurekang:BuildToolsLib:alpha-1.0'
	}
 


 
 
