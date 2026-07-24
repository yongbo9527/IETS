package org.company.finance.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTest {

    @Test
    void shouldRejectZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> Money.of(BigDecimal.ZERO));
    }

    @Test
    void shouldKeepValidAmount() {
        Money money = Money.of(new BigDecimal("18.80"));

        assertEquals(new BigDecimal("18.80"), money.toBigDecimal());
    }
}
