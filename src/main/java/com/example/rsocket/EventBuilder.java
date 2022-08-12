package com.example.rsocket;

public class EventBuilder {

    private Event.Type type;

    public EventBuilder type(Event.Type type) {
        this.type = type;
        return this;
    }

    public Event build() {
        return new Event(type);
    }
}
