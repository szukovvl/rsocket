package com.example.rsocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfiguration {

    @Bean
    public Sinks.Many<Event> eventPublisher() {
        return Sinks.many().replay().all();
    }

    @Bean
    public Flux<Event> events(Sinks.Many<Event> eventPublisher) {
        return eventPublisher
                .asFlux()
                .replay(1)
                .autoConnect();
    }

    @Bean
    public HandlerMapping webSocketMapping(Sinks.Many<Event> eventPublisher, Flux<Event> events) {
        Map<String, Object> map = new HashMap<>();
        map.put("/websocket/chat", new InfoSocketHandler(eventPublisher, events));
        SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
        simpleUrlHandlerMapping.setUrlMap(map);

        //Without the order things break :-/
        simpleUrlHandlerMapping.setOrder(10);
        return simpleUrlHandlerMapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
