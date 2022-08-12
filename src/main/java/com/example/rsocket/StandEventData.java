package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonInclude;

public class StandEventData {
    private final boolean isRunner;
    private final String errMsg;

    public StandEventData(boolean isRunner, String errMsg) {
        this.isRunner = isRunner;
        this.errMsg = errMsg;
    }

    public boolean isRunner() {
        return isRunner;
    }

    @JsonInclude(JsonInclude.Include.NON_ABSENT)
    public String getErrMsg() {
        return errMsg;
    }
}
