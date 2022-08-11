package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class Event {
    public enum Type {
        CHAT_MESSAGE, USER_JOINED, USER_STATS, USER_LEFT
    }


    private final Logger logger = LoggerFactory.getLogger(Event.class);

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);


    private final Type type;

    private final int id;

    private final Payload payload;

    private final long timestamp;



    @JsonCreator
    public Event(@JsonProperty("type") Type type,
                 @JsonProperty("payload") Payload payload) {

        logger.info("Event create: type={}; payload={}", type, payload);

        this.type = type;
        this.payload = payload;
        this.id = ID_GENERATOR.addAndGet(1);
        this.timestamp = System.currentTimeMillis();
    }


    public Type getType() {
        logger.info("Event getType: {}", type);
        return type;
    }

    public Payload getPayload() {
        logger.info("Event getPayload: {}", payload);
        return payload;
    }

    @JsonIgnore
    public User getUser(){
        logger.info("Event getUser");
        return getPayload().getUser();
    }

    public int getId() {
        logger.info("Event getId: {}", id);
        return id;
    }


    public long getTimestamp() {
        logger.info("Event getTimestamp: {}", timestamp);
        return timestamp;
    }

    public static EventBuilder type(Type type) {
        return new EventBuilder().type(type);
    }
}
