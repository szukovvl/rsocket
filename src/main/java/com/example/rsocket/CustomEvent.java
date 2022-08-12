package com.example.rsocket;

import java.util.concurrent.atomic.AtomicInteger;

public class CustomEvent<T> implements ICustomEvent<T> {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final EventTypes type;

    private final int id;

    private final T data;

    private final long timestamp;

    public CustomEvent(EventTypes type, T data) {
        this.type = type;
        this.id = ID_GENERATOR.addAndGet(1);;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    //region ICustomEvent
    @Override
    public EventTypes getType() {
        return this.type;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public T getData() {
        return this.data;
    }
    //endregion

    public static <T> CustomEventBuilder<T> type(EventTypes type) {
        return new CustomEventBuilder<T>().type(type);
    }
}
