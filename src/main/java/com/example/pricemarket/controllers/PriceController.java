package com.example.pricemarket.controllers;

import com.example.pricemarket.models.PriceStorage;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    @Autowired
    private PriceStorage priceStorage;

    @GetMapping("/all")
    public String getAll(PriceStorage.Currency currency) {
        return new Gson().toJson(priceStorage.getPrices(currency));
    }

    @GetMapping("/get-in-range")
    public String getInRange(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam PriceStorage.Currency currency) {
        var res = priceStorage.getInRange(
                Instant.parse(start),
                Instant.parse(end),
                currency
        );
        return new Gson().toJson(res);
    }
}