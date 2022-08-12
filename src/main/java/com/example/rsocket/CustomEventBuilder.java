package com.example.rsocket;

public class CustomEventBuilder<T> {

    private EventTypes type;

    private T data;

    public CustomEventBuilder<T> type(EventTypes type) {
        this.type = type;
        return this;
    }

    public CustomEventBuilder<T> data(T data) {
        this.data = data;
        return this;
    }

    public CustomEvent<T> build() {
        return new CustomEvent<>(type, data);
    }
}
