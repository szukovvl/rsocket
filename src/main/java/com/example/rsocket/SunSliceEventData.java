package com.example.rsocket;

public class SunSliceEventData {
    private final byte key;
    private final float value;

    public SunSliceEventData(byte key, float value) {
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
