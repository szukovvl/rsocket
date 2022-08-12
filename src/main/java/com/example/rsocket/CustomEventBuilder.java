package com.example.rsocket;

public class CustomEventBuilder<T> {

    private EventTypes type;

    private T data;

    public CustomEventBuilder type(EventTypes type) {
        this.type = type;
        return this;
    }

    public CustomEventBuilder data(T data) {
        this.data = data;
        return this;
    }

    public CustomEvent build() {
        return new CustomEvent<T>(type, data);
    }
}
