package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonInclude;

public class WindEventData {

    private final float value;
    private final String url;
    private final String errMsg;

    public WindEventData(float value, String url, String errMsg) {
        this.value = value;
        this.url = url;
        this.errMsg = errMsg;
    }

    public float getValue() {
        return value;
    }

    public String getUrl() {
        return url;
    }

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public String getErrMsg() {
        return errMsg;
    }
}
