package com.example.pricemarket.models;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

@Service
public class PriceStorage {
    private final Map<Currency, NavigableMap<Instant, Price>> priceMap = new HashMap<>();

    public PriceStorage() {
        priceMap.put(Currency.UAH, new TreeMap<>());
        priceMap.put(Currency.USD, new TreeMap<>());
        // Mocked retro data for 10 minutes in the past.
        for (int i = 600; i > 1; i--) {
            Instant ts = Instant.now().minus(Duration.ofSeconds(i * 2L));
            priceMap.get(Currency.UAH).put(ts,
                    new Price(ts, BigDecimal.valueOf(Math.random() * 31 + 1))
            );
            priceMap.get(Currency.USD).put(ts,
                    new Price(ts, BigDecimal.valueOf(Math.random() * 9 + 1))
            );
        }
    }

    public void add(Price price, Currency currency) {
        var prices = priceMap.get(currency);
        if (prices.ceilingKey(price.getTimestamp()) != null) {
            throw new IllegalArgumentException();
        }
        prices.put(price.getTimestamp(), price);
    }

    public List<Price> getPrices(Currency currency) {
        return new ArrayList<>(priceMap.get(currency).values());
    }

    public List<Price> getInRange(Instant start, Instant end, Currency currency) {
        if (start.isAfter(end)) {
            var temp = start;
            start = end;
            end = temp;
        }

        var prices = priceMap.get(currency);
        NavigableMap<Instant, Price> subMapInRange = prices.subMap(start, true, end, true);
        return new ArrayList<>(subMapInRange.values());
    }

    public enum Currency {
        UAH,
        USD
    }
}
