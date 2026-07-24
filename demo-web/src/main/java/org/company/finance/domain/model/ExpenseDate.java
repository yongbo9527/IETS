package org.company.finance.domain.model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * 消费日期值对象
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
public class ExpenseDate {

    private final String value;

    private ExpenseDate(String value) {
        this.value = value;
    }

    public static ExpenseDate of(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("消费日期不能为空");
        }
        try {
            LocalDate.parse(value);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("消费日期格式不正确");
        }
        return new ExpenseDate(value);
    }

    public String getValue() {
        return value;
    }
}
