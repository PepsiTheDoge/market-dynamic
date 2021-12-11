package com.example.pricemarket;

import com.example.pricemarket.models.CurrencyPrice;
import com.example.pricemarket.models.Price;
import com.example.pricemarket.models.PriceStorage;
import java.math.BigDecimal;
import java.time.Duration;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PriceGenerator {
    @Autowired
    public PriceConsumer priceConsumer;

    @PostConstruct
    public void init() {
        generateFlux(PriceStorage.Currency.UAH).subscribe(priceConsumer);
        generateFlux(PriceStorage.Currency.USD).subscribe(priceConsumer);
    }

    public Flux<CurrencyPrice> generateFlux(PriceStorage.Currency currency) {
        return Flux.interval(Duration.ofSeconds(1))
                .map(o -> getRandomPrice(currency));
    }

    private CurrencyPrice getRandomPrice(PriceStorage.Currency currency) {
        double min = getCurrencyAdjust(currency);
        double max = min + 10;
        return new CurrencyPrice(currency,
                new Price(BigDecimal.valueOf(min + (Math.random() * ((max - min) + 1)))));
    }

    private double getCurrencyAdjust(PriceStorage.Currency currency) {
        switch (currency) {
            case UAH:
                return 30;
            case USD:
                return 5;
        }
        throw new IllegalArgumentException();
    }
}

