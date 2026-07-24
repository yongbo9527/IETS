package org.company.finance.domain.model;

import java.math.BigDecimal;

/**
 * 金额值对象
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
public class Money {

    private final BigDecimal value;

    private Money(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("金额必须大于0");
        }
        this.value = value;
    }

    public static Money of(BigDecimal value) {
        return new Money(value);
    }

    public BigDecimal toBigDecimal() {
        return value;
    }
}
