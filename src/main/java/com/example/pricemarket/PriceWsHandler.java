package com.example.pricemarket;

import com.example.pricemarket.models.PriceStorage;
import com.google.gson.Gson;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PriceWsHandler implements WebSocketHandler {
    private final PriceGenerator priceGenerator;
    private final PriceStorage.Currency currency;

    public PriceWsHandler(PriceGenerator priceGenerator, PriceStorage.Currency currency) {
        this.priceGenerator = priceGenerator;
        this.currency = currency;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> messageFlux = priceGenerator.generateFlux(currency)
                .map(currencyPrice -> getSocketMessage(session, currencyPrice));
        return session.send(messageFlux);
    }

    private WebSocketMessage getSocketMessage(WebSocketSession session, CurrencyPrice currencyPrice) {
        Gson gson = new Gson();
        return session.textMessage(gson.toJson(currencyPrice.getPrice()));
    }
}
