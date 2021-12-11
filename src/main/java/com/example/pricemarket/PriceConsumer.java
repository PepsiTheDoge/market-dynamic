package com.example.pricemarket;

import com.example.pricemarket.models.CurrencyPrice;
import com.example.pricemarket.models.PriceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class PriceConsumer implements Consumer<CurrencyPrice> {

    @Autowired
    PriceStorage priceStorage;

    @Override
    public void accept(CurrencyPrice priceEntry) {
        priceStorage.add(priceEntry.getPrice(), priceEntry.getCurrency());
    }
}
