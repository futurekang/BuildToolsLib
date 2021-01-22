package com.futurekang.buildtools.net.retrofit.exception;

import androidx.annotation.Nullable;

public class BusinessException extends Exception {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    @Nullable
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
