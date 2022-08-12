package com.example.rsocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.*;

import java.util.Random;
import java.util.concurrent.Executors;

public class WSCommonInfoHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(WSCommonInfoHandler.class);

    private final Flux<String> outputEvents;

    private final ObjectMapper mapper;

    public WSCommonInfoHandler(Sinks.Many<CustomEvent<?>> eventPublisher, Flux<CustomEvent<?>> events) {
        this.mapper = new ObjectMapper();
        this.outputEvents = Flux.from(events).map(this::toJSON);

        Executors.newSingleThreadExecutor().execute(() -> {
            EventTypes[] types = EventTypes.values();
            Random random = new Random();
            try {
                while (true) {
                    Thread.sleep(3000);
                    eventPublisher.tryEmitNext(createEvent(types[random.nextInt(types.length)]));
                    eventPublisher.tryEmitNext(createEvent(types[random.nextInt(types.length)]));
                }
            }
            catch (InterruptedException ex) {
                logger.error(ex.getMessage());
            }
        });
    }

    private CustomEvent<?> createEvent(EventTypes event) {
        switch (event) {
            case STAND -> {
                return CustomEvent
                        .type(event)
                        .data(new StandEventData(true, null))
                        .build();
            }
            case SUN -> {
                return CustomEvent.type(event).data(new SunEventData(new Random().nextFloat(100.0f))).build();
            }
            case WIND -> {
                return CustomEvent.type(event).data(new WindEventData(new Random().nextFloat(100.0f), "local", null)).build();
            }
            case WIND_SLICE -> {
                return CustomEvent.type(event).data(new WindSliceEventData((byte) 0x10, new Random().nextFloat(100.0f))).build();
            }
            case SOLAR_SLICE -> {
                return CustomEvent.type(event).data(new SunSliceEventData((byte) 0x01, new Random().nextFloat(100.0f))).build();
            }
            default -> {
                logger.error("недопустимый тип события");
                throw new IllegalArgumentException("недопустимый тип события");
            }
        }
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

    private String toJSON(CustomEvent<?> event) {
        try {
            return mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
