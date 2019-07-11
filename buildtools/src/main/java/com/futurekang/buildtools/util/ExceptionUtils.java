package com.futurekang.buildtools.util;

import android.text.TextUtils;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ExceptionUtils {
    public NetErrorException convertException(Throwable e) {
        NetErrorException error = null;
        if (e instanceof UnknownHostException) {
            error = new NetErrorException(e, NetErrorException.NO_CONNECT_ERROR);
        } else if (e instanceof JSONException || e instanceof JsonParseException) {
            error = new NetErrorException(e, NetErrorException.PARSE_ERROR);
        } else if (e instanceof SocketTimeoutException) {
            error = new NetErrorException(e, NetErrorException.SocketTimeoutError);
        } else if (e instanceof ConnectException) {
            error = new NetErrorException(e, NetErrorException.CONNECT_EXCEPTION);
        } else {
            error = new NetErrorException(e, NetErrorException.OTHER);
        }
        return error;
    }


    public class NetErrorException extends IOException {
        private Throwable exception;
        private int mErrorType = NO_CONNECT_ERROR;
        private String mErrorMessage;
        /**
         * 数据解析异常
         */
        public static final int PARSE_ERROR = 0;
        /**
         * 无连接异常
         */
        public static final int NO_CONNECT_ERROR = 1;

        /*网络连接超时*/
        public static final int SocketTimeoutError = 6;
        /**
         * 无法连接到服务
         */
        public static final int CONNECT_EXCEPTION = 7;
        /**
         * 服务器错误
         */
        public static final int HTTP_EXCEPTION = 8;
        /**
         * 其他
         */
        public static final int OTHER = -99;
        /**
         * 没有网络
         */
        public static final int UNOKE = -1;
        /**
         * 无法找到
         */
        public static final int NOT_FOUND = 404;

        public NetErrorException(Throwable exception, int mErrorType) {
            this.exception = exception;
            this.mErrorType = mErrorType;
        }

        public NetErrorException(String message, Throwable cause) {
            super(message, cause);
        }

        public NetErrorException(String message, int mErrorType) {
            super(message);
            this.mErrorType = mErrorType;
            this.mErrorMessage = message;
        }


        @Override
        public String getMessage() {
            if (!TextUtils.isEmpty(mErrorMessage)) {
                return mErrorMessage;
            }
            switch (mErrorType) {
                case PARSE_ERROR:
                    return "数据解析异常";
                case NO_CONNECT_ERROR:
                    return "无法连接到服务器，请检查网络连接后再试！";
                case OTHER:
                    return exception.getMessage();
                case UNOKE:
                    return "当前无网络连接";
                case CONNECT_EXCEPTION:
                    return "无法连接到服务器，请检查网络连接后再试！";
                case HTTP_EXCEPTION:
                    try {
                        if (exception.getMessage().equals("HTTP 500 Internal Server Error")) {
                            return "服务器发生错误！";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (exception.getMessage().contains("Not Found"))
                        return "无法连接到服务器，请检查网络连接后再试！";
                    return "服务器发生错误";
            }
            try {
                return exception.getMessage();
            } catch (Exception e) {
                return "未知错误";
            }

        }

        /**
         * 获取错误类型
         */
        public int getErrorType() {
            return mErrorType;
        }
        /*其他*/
    }
}
