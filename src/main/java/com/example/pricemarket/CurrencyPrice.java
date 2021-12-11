package com.example.pricemarket;

import com.example.pricemarket.models.Price;
import com.example.pricemarket.models.PriceStorage;

public class CurrencyPrice {
    private final PriceStorage.Currency currency;
    private final Price price;

    public CurrencyPrice(PriceStorage.Currency currency, Price price) {
        this.currency = currency;
        this.price = price;
    }

    public PriceStorage.Currency getCurrency() {
        return currency;
    }

    public Price getPrice() {
        return price;
    }
}
