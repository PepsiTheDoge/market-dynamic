package com.example.pricemarket;

import com.example.pricemarket.models.Price;
import static org.junit.Assert.*;
import com.example.pricemarket.models.PriceStorage;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.Instant;

@SpringBootTest
class PriceMarketApplicationTests {

	private final PriceStorage priceStorage = new PriceStorage();

	@Test
	void testGetRetroData() {
		for (int i = 1; i < 21; i++) {
			priceStorage.add(new Price(Instant.ofEpochSecond(i),
					BigDecimal.valueOf(i + 1)), PriceStorage.Currency.UAH);
		}

		Instant start1 = Instant.ofEpochSecond(1);
		Instant end1 = Instant.ofEpochSecond(2);
		var result = priceStorage.getInRange(start1, end1, PriceStorage.Currency.UAH);
		assertEquals(2, result.size());

	}

	@Test
	void testGetRetroDataStartIsAfterEnd() {
		for (int i = 1; i < 3; i++) {
			priceStorage.add(new Price(Instant.ofEpochSecond(i),
					BigDecimal.valueOf(i + 1)), PriceStorage.Currency.UAH);
		}

		Instant start1 = Instant.ofEpochSecond(0);
		Instant end1 = Instant.ofEpochSecond(40);
		var result = priceStorage.getInRange(start1, end1, PriceStorage.Currency.UAH);
		assertEquals(2, result.size());
	}
}
