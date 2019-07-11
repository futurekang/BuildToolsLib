package com.futurekang.buildtools.net.okhttp;


public interface HttpCallback {

    void success(Object object);

    void fail(int statusCode, Object object);

    void exception();

}
