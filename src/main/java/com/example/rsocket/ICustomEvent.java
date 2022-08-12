package com.example.rsocket;

public interface ICustomEvent<T> {

    EventTypes getType();

    int getId();

    long getTimestamp();

    T getData();
}
