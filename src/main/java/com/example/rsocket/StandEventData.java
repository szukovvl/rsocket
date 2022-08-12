package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonInclude;

public record StandEventData(boolean isRunner, String errMsg) {

    @Override
    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public String errMsg() {
        return errMsg;
    }
}
