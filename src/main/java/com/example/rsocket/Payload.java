package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class Payload {

    private final Logger logger = LoggerFactory.getLogger(Payload.class);

    private final User user;

    private Map<String, Object> properties = new HashMap<>();

    public Payload(User user, Map<String, Object> properties){
        this(user);
        logger.info("Payload create: user={}; properties={}", user, properties);
        this.properties = properties;
    }
    @JsonCreator
    private Payload(@JsonProperty("user") User user) {
        logger.info("Payload create: user={}", user);
        this.user = user;
    }

    public User getUser() {
        logger.info("Payload getUser: {}", user);
        return user;
    }

    @JsonAnySetter
    private void setProperties(String name, Object value){
        logger.info("Payload setProperties: name={}; value={}", name, value);
        properties.put(name, value);
    }

    @JsonAnyGetter
    private Map<String, Object> getProperties(){
        logger.info("Payload getProperties: {}", properties);
        return properties;
    }
}
