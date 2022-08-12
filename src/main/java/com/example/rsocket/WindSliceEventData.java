package com.example.rsocket;

public class WindSliceEventData {
    private final byte key;
    private final float value;

    public WindSliceEventData(byte key, float value) {
        this.key = key;
        this.value = value;
    }

    public byte getKey() {
        return key;
    }

    public float getValue() {
        return value;
    }
}
