package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.concurrent.atomic.AtomicInteger;

public class Event {
    public enum Type {
        USER_STATS
    }

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);

    private final Type type;

    private final int id;

    private final long timestamp;

    @JsonCreator
    public Event(@JsonProperty("type") Type type) {

        this.type = type;
        this.id = ID_GENERATOR.addAndGet(1);
        this.timestamp = System.currentTimeMillis();
    }


    public Type getType() {
        return type;
    }

    public int getId() {
        return id;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public static EventBuilder type(Type type) {
        return new EventBuilder().type(type);
    }
}
