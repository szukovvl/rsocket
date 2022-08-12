package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonInclude;

public record WindEventData(float value, String url, String errMsg) {

    @Override
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public String errMsg() {
        return errMsg;
    }
}
