package com.example.rsocket;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class User {

    private final Logger logger = LoggerFactory.getLogger(User.class);

    private final String alias;
    private final String avatar;

    public static User systemUser(){
        return new User("System", "https://robohash.org/system.png");
    }

    @JsonCreator
    public User(@JsonProperty("alias") String alias, @JsonProperty("avatar") String avatar) {
        logger.info("User create: alias={}; avatar={}", alias, avatar);
        this.alias = alias;
        this.avatar = avatar;
    }

    public String getAlias() {
        logger.info("User getAlias: {}", alias);
        return alias;
    }

    public String getAvatar() {
        logger.info("User getAvatar: {}", avatar);
        return avatar;
    }

}
