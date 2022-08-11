package com.example.rsocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class EventBuilder {

    private final Logger logger = LoggerFactory.getLogger(EventBuilder.class);

    private Event.Type type;
    private final PayloadBuilder payloadBuilder = new PayloadBuilder();

    public EventBuilder type(Event.Type type) {
        logger.info("EventBuilder type {}", type);
        this.type = type;
        return this;
    }

    public PayloadBuilder withPayload() {
        logger.info("EventBuilder withPayload");
        return payloadBuilder;
    }

    private Event buildEvent(Payload payload) {
        logger.info("EventBuilder buildEvent {}", payload);
        return new Event(type, payload);
    }

    protected class PayloadBuilder {

        private final Logger logger = LoggerFactory.getLogger(PayloadBuilder.class);

        private String alias;
        private String avatar;
        private final Map<String, Object> properties = new HashMap<>();

        public PayloadBuilder userAlias(String alias) {
            logger.info("PayloadBuilder userAlias: {}", alias);
            this.alias = alias;
            return this;
        }

        public PayloadBuilder userAvatar(String avatar) {
            logger.info("PayloadBuilder userAvatar: {}", avatar);
            this.avatar = avatar;
            return this;
        }

        public PayloadBuilder user(User user) {
            logger.info("PayloadBuilder user: {}", user);
            this.alias = user.getAlias();
            this.avatar = user.getAvatar();
            return this;
        }

        public PayloadBuilder systemUser() {
            logger.info("PayloadBuilder systemUser");
            user(User.systemUser());
            return this;
        }

        public PayloadBuilder property(String property, Object value) {
            logger.info("PayloadBuilder property: property={}; value={}", property, value);
            properties.put(property, value);
            return this;
        }


        public Event build() {
            logger.info("PayloadBuilder build");
            return buildEvent(new Payload(new User(payloadBuilder.alias, payloadBuilder.avatar), properties));
        }
    }
}
