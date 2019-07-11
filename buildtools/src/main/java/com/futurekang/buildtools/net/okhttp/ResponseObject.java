package com.futurekang.buildtools.net.okhttp;

import org.json.JSONObject;

public class ResponseObject {

    private JSONObject jsonObject;
    private boolean boolSuccess;
    private String strMessage;

    public ResponseObject() {
    }

    public JSONObject getJsonObject() {
        return this.jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public boolean getSuccess() {
        return this.boolSuccess;
    }

    public void setSuccess(boolean boolSuccess) {
        this.boolSuccess = boolSuccess;
    }

    public String getMessage() {
        return this.strMessage;
    }

    public void setMessage(String strMessage) {
        this.strMessage = strMessage;
    }
}
