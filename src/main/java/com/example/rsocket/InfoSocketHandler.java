package com.example.rsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.*;

import java.util.concurrent.Executors;

import static com.example.rsocket.Event.Type.USER_STATS;

public class InfoSocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(InfoSocketHandler.class);

    private final Flux<String> outputEvents;

    private final ObjectMapper mapper;

    public InfoSocketHandler(Sinks.Many<Event> eventPublisher, Flux<Event> events) {
        this.mapper = new ObjectMapper();
        this.outputEvents = Flux.from(events).map(this::toJSON);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                while (true) {
                    Thread.sleep(10000);
                    eventPublisher.tryEmitNext(Event
                            .type(USER_STATS)
                            .build());
                }
            }
            catch (InterruptedException ex) {
                logger.error(ex.getMessage());
            }
        });
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        return session
                .receive()
                .map(WebSocketMessage::getPayloadAsText) // просто подавляю возможные передачи
                .doOnError(this::onError)
                .doOnComplete(this::onComplete)
                        .zipWith(session.send(outputEvents.map(session::textMessage)))
                                .then();
    }


    private void onError(Throwable error) {
        logger.error(error.getMessage());
    }

    private void onComplete() {
        logger.info("ChatSocketHandler -> onComplete");
    }

    private String toJSON(Event event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
