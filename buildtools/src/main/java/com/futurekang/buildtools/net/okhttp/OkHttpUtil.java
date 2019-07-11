package com.futurekang.buildtools.net.okhttp;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpUtil {
    private static OkHttpUtil okHttpUtil;
    private OkHttpClient mClient = new OkHttpClient();
    private Handler mHandler;

    private OkHttpUtil() {
        this.mClient.newBuilder().connectTimeout(3L, TimeUnit.MINUTES)
                .readTimeout(3L, TimeUnit.MINUTES)
                .writeTimeout(3L, TimeUnit.MINUTES);
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtil getInstance() {
        if (okHttpUtil == null) {
            Class var0 = OkHttpUtil.class;
            synchronized (OkHttpUtil.class) {
                if (okHttpUtil == null) {
                    okHttpUtil = new OkHttpUtil();
                }
            }
        }

        return okHttpUtil;
    }

    public OkHttpClient getOkHttpClient() {
        return this.mClient;
    }

    public void setSSLSocketFactory() {
        this.mClient.newBuilder().socketFactory(this.createSSLSocketFactory());
        this.mClient.newBuilder().hostnameVerifier(new TrustAllHostnameVerifier());
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory, HostnameVerifier hostnameVerifier) {
        this.mClient.newBuilder().socketFactory(sslSocketFactory);
        this.mClient.newBuilder().hostnameVerifier(hostnameVerifier);
    }

    public Response getSync(String url) {
        Request request = (new Request.Builder()).url(url).build();
        Response response = null;

        try {
            response = this.mClient.newCall(request).execute();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return response;
    }

    public Response getSync(String url, Object tag) {
        Request request = (new Request.Builder()).url(url).tag(tag).build();
        Response response = null;

        try {
            response = this.mClient.newCall(request).execute();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return response;
    }

    public Response getSync(String url, Map<String, String> params) {
        String strPara = setUrlParams(params);
        String strUrl = url + "?" + strPara.substring(1);
        Request request = (new Request.Builder()).url(strUrl).build();
        Response response = null;

        try {
            response = this.mClient.newCall(request).execute();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return response;
    }

    public Response getSync(String url, Object tag, Map<String, String> params) {
        String strPara = setUrlParams(params);
        String strUrl = url + "?" + strPara.substring(1);
        Request request = (new Request.Builder()).url(strUrl).tag(tag).build();
        Response response = null;

        try {
            response = this.mClient.newCall(request).execute();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return response;
    }

    public String getSyncString(String url) {
        Request request = (new Request.Builder()).url(url).build();
        String result = null;

        try {
            Response e = this.mClient.newCall(request).execute();
            result = e.body().string();
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return result;
    }

    public String getSyncString(String url, Object tag) {
        Request request = (new Request.Builder()).url(url).tag(tag).build();
        String result = null;

        try {
            Response e = this.mClient.newCall(request).execute();
            result = e.body().string();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return result;
    }

    public String getSyncString(String url, Map<String, String> params) {
        String strPara = setUrlParams(params);
        String strUrl = url + "?" + strPara.substring(1);
        Request request = (new Request.Builder()).url(strUrl).build();
        String result = null;

        try {
            Response e = this.mClient.newCall(request).execute();
            result = e.body().string();
        } catch (IOException var8) {
            var8.printStackTrace();
        }

        return result;
    }

    public String getSyncString(String url, Object tag, Map<String, String> params) {
        String strPara = setUrlParams(params);
        String strUrl = url + "?" + strPara.substring(1);
        Request request = (new Request.Builder()).url(strUrl).tag(tag).build();
        String result = null;

        try {
            Response e = this.mClient.newCall(request).execute();
            result = e.body().string();
        } catch (IOException var9) {
            var9.printStackTrace();
        }

        return result;
    }

    public void getAsync(String url, DataCallBack callBack) {
        Request request = (new Request.Builder()).url(url).build();
        this.enqueue(request, callBack);
    }

    public void getAsync(String url, Object tag, DataCallBack callBack) {
        Request request = (new Request.Builder()).url(url).build();
        this.enqueue(request, callBack);
    }

    public void getAsync(String url, Map<String, String> params, DataCallBack callBack) {
        String strPara = setUrlParams(params);
        String strUrl = url + "?" + strPara.substring(1);
        Request request = (new Request.Builder()).url(strUrl).build();
        this.enqueue(request, callBack);
    }

    public void getAsync(String url, Object tag, Map<String, String> params, DataCallBack callBack) {
        String strPara = setUrlParams(params);
        String strUrl = url + "?" + strPara.substring(1);
        Request request = (new Request.Builder()).url(strUrl).tag(tag).build();
        this.enqueue(request, callBack);
    }

    public String postSync(String url, Map<String, String> params) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set request = params.entrySet();
            Iterator response = request.iterator();

            while (response.hasNext()) {
                Map.Entry e = (Map.Entry) response.next();
                formBody.add((String) e.getKey(), (String) e.getValue());
            }
        }

        Request request1 = (new Request.Builder()).url(url).post(formBody.build()).build();

        try {
            Response response1 = this.mClient.newCall(request1).execute();
            return response1.isSuccessful() ? response1.body().string() : null;
        } catch (IOException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public String postSync(String url, Object tag, Map<String, String> params) {
        FormBody.Builder formBody = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set request = params.entrySet();
            Iterator response = request.iterator();

            while (response.hasNext()) {
                Map.Entry e = (Map.Entry) response.next();
                formBody.add((String) e.getKey(), (String) e.getValue());
            }
        }

        Request request1 = (new Request.Builder()).url(url).tag(tag).post(formBody.build()).build();

        try {
            Response response1 = this.mClient.newCall(request1).execute();
            return response1.isSuccessful() ? response1.body().string() : null;
        } catch (IOException var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public String postSync(String url, String json) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).build();

        try {
            Response e = this.mClient.newCall(request).execute();
            return e.isSuccessful() ? e.body().string() : null;
        } catch (IOException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public void postSync(String url, String json, DataCallBack callBack) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).build();
        this.enqueue(request, callBack);
    }

    public void encryptPostSync(String url, String json, String key, DataCallBack callBack) {
        json = EncryptUtil.Encrypt(json, key);
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).build();
        this.encryEnqueue(request, key, callBack);
    }

    public String postSync(String url, Object tag, String json) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).tag(tag).build();

        try {
            Response e = this.mClient.newCall(request).execute();
            return e.isSuccessful() ? e.body().string() : null;
        } catch (IOException var8) {
            var8.printStackTrace();
            return null;
        }
    }

    public String postSync(String url, String json, Map<String, String> headersParams) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        Headers headers = setHeaders(headersParams);
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).headers(headers).build();

        try {
            Response e = this.mClient.newCall(request).execute();
            return e.isSuccessful() ? e.body().string() : null;
        } catch (IOException var9) {
            var9.printStackTrace();
            return null;
        }
    }

    public String postSync(String url, Object tag, String json, Map<String, String> headersParams) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        Headers headers = setHeaders(headersParams);
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).headers(headers).tag(tag).build();

        try {
            Response e = this.mClient.newCall(request).execute();
            return e.isSuccessful() ? e.body().string() : null;
        } catch (IOException var10) {
            var10.printStackTrace();
            return null;
        }
    }

    public void postAsync(String url, String json, Map<String, String> headersParams, DataCallBack callBack) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        Headers headers = setHeaders(headersParams);
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).headers(headers).build();
        this.enqueue(request, callBack);
    }

    public void postAsync(String url, Object tag, String json, Map<String, String> headersParams, DataCallBack callBack) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        Headers headers = setHeaders(headersParams);
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).headers(headers).build();
        this.enqueue(request, callBack);
    }

    public void postAsync(String url, Map<String, String> params, DataCallBack callBack) {
        FormBody requestBody = null;
        if (params == null) {
            params = new HashMap();
        }

        FormBody.Builder builder = new FormBody.Builder();

        String key;
        String value;
        for (Iterator request = ((Map) params).entrySet().iterator(); request.hasNext(); builder.add(key, value)) {
            Map.Entry map = (Map.Entry) request.next();
            key = ((String) map.getKey()).toString();
            value = null;
            if (map.getValue() == null) {
                value = "";
            } else {
                value = (String) map.getValue();
            }
        }

        requestBody = builder.build();
        Request request1 = (new Request.Builder()).url(url).post(requestBody).build();
        this.enqueue(request1, callBack);
    }

    public void postAsync(String url, Object tag, Map<String, String> params, DataCallBack callBack) {
        FormBody requestBody = null;
        if (params == null) {
            params = new HashMap();
        }

        FormBody.Builder builder = new FormBody.Builder();

        String key;
        String value;
        for (Iterator request = ((Map) params).entrySet().iterator(); request.hasNext(); builder.add(key, value)) {
            Map.Entry map = (Map.Entry) request.next();
            key = ((String) map.getKey()).toString();
            value = null;
            if (map.getValue() == null) {
                value = "";
            } else {
                value = (String) map.getValue();
            }
        }

        requestBody = builder.build();
        Request request1 = (new Request.Builder()).url(url).tag(tag).post(requestBody).build();
        this.enqueue(request1, callBack);
    }

    public void encryPostAsync(String url, Object tag, String json, String encryKey, Map<String, String> headersParams, DataCallBack callBack) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        Headers headers = setHeaders(headersParams);
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).headers(headers).build();
        this.encryEnqueue(request, encryKey, callBack);
    }

    public void encryNewPostAsync(String url, Object tag, String json, String encryKey, Map<String, String> headersParams, ResponseDataCallBack callBack) {
        MediaType medialType = MediaType.parse("application/json; charset=utf-8");
        Headers headers = setHeaders(headersParams);
        RequestBody body = RequestBody.create(medialType, json);
        Request request = (new Request.Builder()).url(url).post(body).headers(headers).build();
        this.encryNewEnqueue(request, encryKey, callBack);
    }

    public void postAsync(String url, Map<String, String> headersParams, Map<String, String> params, DataCallBack callBack) {
        FormBody requestBody = null;
        if (params == null) {
            params = new HashMap();
        }

        Headers headers = setHeaders(headersParams);
        FormBody.Builder builder = new FormBody.Builder();

        String key;
        String value;
        for (Iterator request = ((Map) params).entrySet().iterator(); request.hasNext(); builder.add(key, value)) {
            Map.Entry map = (Map.Entry) request.next();
            key = ((String) map.getKey()).toString();
            value = null;
            if (map.getValue() == null) {
                value = "";
            } else {
                value = (String) map.getValue();
            }
        }

        requestBody = builder.build();
        Request request1 = (new Request.Builder()).url(url).headers(headers).post(requestBody).build();
        this.enqueue(request1, callBack);
    }

    public void postAsync(String url, Object tag, Map<String, String> headersParams, Map<String, String> params, DataCallBack callBack) {
        FormBody requestBody = null;
        if (params == null) {
            params = new HashMap();
        }

        Headers headers = setHeaders(headersParams);
        FormBody.Builder builder = new FormBody.Builder();

        String key;
        String value;
        for (Iterator request = ((Map) params).entrySet().iterator(); request.hasNext(); builder.add(key, value)) {
            Map.Entry map = (Map.Entry) request.next();
            key = ((String) map.getKey()).toString();
            value = null;
            if (map.getValue() == null) {
                value = "";
            } else {
                value = (String) map.getValue();
            }
        }

        requestBody = builder.build();
        Request request1 = (new Request.Builder()).url(url).tag(tag).headers(headers).post(requestBody).build();
        this.enqueue(request1, callBack);
    }

    private void enqueue(final Request request, final DataCallBack callBack) {
        this.mClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                OkHttpUtil.this.deliverDataFailure(request, -1, e, callBack);
            }

            public void onResponse(Call call, Response response) {
                String result = null;
                InputStream inputStream = null;
                int statusCode = response.code();
                try {
                    result = response.body().string();
                    inputStream = response.body().byteStream();
                    OkHttpUtil.this.deliverDataSuccess(result, inputStream, callBack);
                } catch (IOException var6) {
                    OkHttpUtil.this.deliverDataFailure(request, statusCode, var6, callBack);
                }

            }
        });
    }

    private void encryEnqueue(final Request request, final String encryKey, final DataCallBack callBack) {
        this.mClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                OkHttpUtil.this.deliverDataFailure(request, -1, e, callBack);
            }

            public void onResponse(Call call, Response response) {
                String result = null;
                InputStream inputStream = null;

                try {
                    String e = response.body().string();
                    JSONObject jsonObject = new JSONObject(e);
                    result = EncryptUtil.Decrypt(jsonObject.getString("Response"), encryKey);
                    inputStream = response.body().byteStream();
                    OkHttpUtil.this.deliverDataSuccess(result, inputStream, callBack);
                } catch (Exception var7) {
                    int status = response.code();
                    OkHttpUtil.this.deliverDataFailure(request, status, var7, callBack);
                }

            }
        });
    }

    private void encryNewEnqueue(final Request request, final String encryKey, final ResponseDataCallBack callBack) {
        this.mClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                OkHttpUtil.this.deliverNewDataFailure(request, e, callBack);
            }

            public void onResponse(Call call, Response response) {
                JSONObject result = null;
                InputStream inputStream = null;

                try {
                    String e = response.body().string();
                    ResponseObject responseObject = new ResponseObject();
                    JSONObject jsonObject = new JSONObject(e);
                    if (jsonObject.optBoolean("Success") && jsonObject.optString("Response") != "") {
                        String encryResult = EncryptUtil.Decrypt(jsonObject.optString("Response"), encryKey);
                        result = new JSONObject(encryResult);
                    }

                    inputStream = response.body().byteStream();
                    responseObject.setJsonObject(result);
                    responseObject.setSuccess(jsonObject.optBoolean("Success"));
                    responseObject.setMessage(jsonObject.optString("Message"));
                    OkHttpUtil.this.deliverNewDataSuccess(responseObject, inputStream, callBack);
                } catch (Exception var9) {
                    OkHttpUtil.this.deliverNewDataFailure(request, var9, callBack);
                }

            }
        });
    }

    public void postFileAsync(String url, String name, File file, DataCallBack callback) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody requestBody = (new MultipartBody.Builder()).setType(MultipartBody.FORM).addFormDataPart(name, file.getName(), fileBody).build();
        Request requestPostFile = (new Request.Builder()).url(url).post(requestBody).build();
        this.enqueue(requestPostFile, callback);
    }

    public void postFileAsync(String url, Object tag, String name, File file, DataCallBack callback) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody requestBody = (new MultipartBody.Builder()).setType(MultipartBody.FORM).addFormDataPart(name, file.getName(), fileBody).build();
        Request requestPostFile = (new Request.Builder()).url(url).tag(tag).post(requestBody).build();
        this.enqueue(requestPostFile, callback);
    }

    public void postFileAsync(String url, Map<String, String> paramMap, Map<String, List<File>> fileList, String mediaType, DataCallBack callBack) {
        RequestBody requestBody = this.setRequestBody(MediaType.parse(mediaType), paramMap, fileList);
        Request request = (new Request.Builder()).url(url).post(requestBody).build();
        this.enqueue(request, callBack);
    }

    public void postFileAsync(String url, Object tag, Map<String, String> paramMap, Map<String, List<File>> fileList, String mediaType, DataCallBack callBack) {
        RequestBody requestBody = this.setRequestBody(MediaType.parse(mediaType), paramMap, fileList);
        Request request = (new Request.Builder()).url(url).tag(tag).post(requestBody).build();
        this.enqueue(request, callBack);
    }

    private RequestBody setRequestBody(MediaType mediaType, Map<String, String> paramMap, Map<String, List<File>> fileList) {
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);
        if (paramMap != null) {
            Iterator requestBody = paramMap.keySet().iterator();

            while (requestBody.hasNext()) {
                String key = (String) requestBody.next();
                multipartBodyBuilder.addFormDataPart(key, (String) paramMap.get(key));
            }
        }

        if (fileList != null) {
            Set requestBody1 = fileList.entrySet();
            Iterator key2 = requestBody1.iterator();

            while (key2.hasNext()) {
                Map.Entry entry = (Map.Entry) key2.next();
                String key1 = (String) entry.getKey();
                List value = (List) entry.getValue();
                Iterator var10 = value.iterator();

                while (var10.hasNext()) {
                    File file = (File) var10.next();
                    multipartBodyBuilder.addFormDataPart(key1, (String) null, RequestBody.create(mediaType, file));
                }
            }
        }

        MultipartBody requestBody2 = multipartBodyBuilder.build();
        return requestBody2;
    }

    private RequestBody setFileRequestBody(MediaType mediaType, Map<String, String> bodyParams, Map<String, String> fileParams) {
        MultipartBody body = null;
        MultipartBody.Builder MultipartBodyBuilder = new MultipartBody.Builder();
        MultipartBodyBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody = null;
        Set entrySet;
        Iterator var8;
        Map.Entry entry;
        if (bodyParams != null) {
            entrySet = bodyParams.entrySet();
            var8 = entrySet.iterator();

            while (var8.hasNext()) {
                entry = (Map.Entry) var8.next();
                MultipartBodyBuilder.addFormDataPart((String) entry.getKey(), (String) entry.getValue());
            }
        }

        if (fileParams != null) {
            entrySet = fileParams.entrySet();
            var8 = entrySet.iterator();

            while (var8.hasNext()) {
                entry = (Map.Entry) var8.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                File file = new File(value);
                fileBody = RequestBody.create(mediaType, file);
                MultipartBodyBuilder.addFormDataPart(key, file.getName(), fileBody);
            }
        }

        body = MultipartBodyBuilder.build();
        return body;
    }

    public void downloadAsync(String url, String desDir, ProgressCallBack callBack) {
        Request request = (new Request.Builder()).url(url).build();
        this.downloadEnqueue(request, url, desDir, (String) null, callBack);
    }

    public void downloadAsync(String url, Object tag, String desDir, ProgressCallBack callBack) {
        Request request = (new Request.Builder()).url(url).tag(tag).build();
        this.downloadEnqueue(request, url, desDir, (String) null, callBack);
    }

    public void downloadAsync(String url, Object tag, String desDir, String fileName, ProgressCallBack callBack) {
        Request request = (new Request.Builder()).url(url).tag(tag).build();
        this.downloadEnqueue(request, url, desDir, fileName, callBack);
    }

    private void downloadEnqueue(final Request request, final String url, final String desDir, final String fileName, final ProgressCallBack callBack) {
        this.mClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                OkHttpUtil.this.deliverDataFailure(request, -1, e, callBack);
            }

            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                int status = response.code();
                try {
                    String e = "";
                    if (fileName == null) {
                        e = OkHttpUtil.this.getFileName(url);
                    } else {
                        e = fileName;
                    }

                    File file = new File(desDir, e);
                    long total = response.body().contentLength();
                    inputStream = response.body().byteStream();
                    fileOutputStream = new FileOutputStream(file);
                    boolean len = false;
                    long current = 0L;
                    byte[] bytes = new byte[2048];

                    int len1;
                    while ((len1 = inputStream.read(bytes)) != -1) {
                        current += (long) len1;
                        fileOutputStream.write(bytes, 0, len1);
                        // OkHttpUtil.access$600(this, total, current, callBack);
                    }

                    fileOutputStream.flush();
                    OkHttpUtil.this.deliverDataSuccess(file.getAbsolutePath(), (InputStream) null, callBack);
                } catch (IOException var16) {
                    OkHttpUtil.this.deliverDataFailure(request, status, var16, callBack);
                    var16.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }

                }

            }
        });
    }

    private String getFileName(String url) {
        int separatorIndex = url.lastIndexOf("/");
        String path = separatorIndex < 0 ? url : url.substring(separatorIndex + 1, url.length());
        return path;
    }

    private static Headers setHeaders(Map<String, String> headersParams) {
        Headers headers = null;
        Headers.Builder headersbuilder = new Headers.Builder();
        if (headersParams != null) {
            Iterator iterator = headersParams.keySet().iterator();
            String key = "";

            while (iterator.hasNext()) {
                key = ((String) iterator.next()).toString();
                headersbuilder.add(key, (String) headersParams.get(key));
            }
        }

        headers = headersbuilder.build();
        return headers;
    }

    private static String setUrlParams(Map<String, String> mapParams) {
        String strParams = "";
        if (mapParams != null) {
            Iterator iterator = mapParams.keySet().iterator();
            for (String key = ""; iterator.hasNext(); strParams = strParams + "&" + key + "=" + (String) mapParams.get(key)) {
                key = ((String) iterator.next()).toString();
            }
        }

        return strParams;
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init((KeyManager[]) null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception var3) {
            ;
        }

        return sSLSocketFactory;
    }

    private void deliverDataFailure(final Request request, final int statusCode, final Exception e, final DataCallBack callBack) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(request, statusCode, e);
                }

            }
        });
    }

    private void deliverDataSuccess(final String result, final InputStream inputStream, final DataCallBack callBack) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onSuccess(result, inputStream);
                    } catch (Exception var2) {
                        var2.printStackTrace();
                    }
                }

            }
        });
    }

    private void deliverNewDataFailure(final Request request, final Exception e, final ResponseDataCallBack callBack) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (callBack != null) {
                    callBack.onFailure(request, e);
                }

            }
        });
    }

    private void deliverNewDataSuccess(final ResponseObject result, final InputStream inputStream, final ResponseDataCallBack callBack) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onSuccess(result, inputStream);
                    } catch (Exception var2) {
                        var2.printStackTrace();
                    }
                }

            }
        });
    }

    private <T> void progressCallBack(final long total, final long current, final ProgressCallBack callBack) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (callBack != null) {
                    callBack.onProgress(total, current);
                }

            }
        });
    }

    public interface ProgressCallBack extends DataCallBack {
        void onProgress(long var1, long var3);
    }

    public interface ResponseDataCallBack {
        void onFailure(Request var1, Exception var2);

        void onSuccess(ResponseObject var1, InputStream var2);
    }

    public interface DataCallBack {
        void onFailure(Request var1, int statusCode, Exception var2);

        void onSuccess(String var1, InputStream var2);
    }

    private class TrustAllHostnameVerifier implements HostnameVerifier {
        private TrustAllHostnameVerifier() {
        }

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private class TrustAllManager implements X509TrustManager {
        private TrustAllManager() {
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}

