package com.example.rsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.io.IOException;
import java.util.Optional;

import static com.example.rsocket.Event.Type.USER_LEFT;

public class ChatSocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(ChatSocketHandler.class);

    private final UnicastProcessor<Event> eventPublisher;
    private final Flux<String> outputEvents;
    private final ObjectMapper mapper;

    public ChatSocketHandler(UnicastProcessor<Event> eventPublisher, Flux<Event> events) {
        logger.info("ChatSocketHandler create: eventPublisher={}; events={}", eventPublisher, events);
        this.eventPublisher = eventPublisher;
        this.mapper = new ObjectMapper();
        this.outputEvents = Flux.from(events).map(this::toJSON);
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        logger.info("ChatSocketHandler handle {}", session);
        WebSocketMessageSubscriber subscriber = new WebSocketMessageSubscriber(eventPublisher);
        return session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toEvent)
                .doOnNext(subscriber::onNext)
                .doOnError(subscriber::onError)
                .doOnComplete(subscriber::onComplete)
                .zipWith(session.send(outputEvents.map(session::textMessage)))
                .then();
    }


    private Event toEvent(String json) {
        logger.info("ChatSocketHandler toEvent {}", json);
        try {
            return mapper.readValue(json, Event.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }

    private String toJSON(Event event) {
        logger.info("ChatSocketHandler toJSON {}", event);
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static class WebSocketMessageSubscriber {

        private final Logger logger = LoggerFactory.getLogger(WebSocketMessageSubscriber.class);
        private final UnicastProcessor<Event> eventPublisher;
        private Optional<Event> lastReceivedEvent = Optional.empty();

        public WebSocketMessageSubscriber(UnicastProcessor<Event> eventPublisher) {
            logger.info("WebSocketMessageSubscriber create {}", eventPublisher);
            this.eventPublisher = eventPublisher;
        }

        public void onNext(Event event) {
            logger.info("WebSocketMessageSubscriber onNext {}", event);
            lastReceivedEvent = Optional.of(event);
            eventPublisher.onNext(event);
        }

        public void onError(Throwable error) {
            logger.info("WebSocketMessageSubscriber onError {}", error);
            //TODO log error
            error.printStackTrace();
        }

        public void onComplete() {

            logger.info("WebSocketMessageSubscriber onComplete");

            lastReceivedEvent.ifPresent(event -> eventPublisher.onNext(
                    Event.type(USER_LEFT)
                            .withPayload()
                            .user(event.getUser())
                            .build()));
        }

    }
}
