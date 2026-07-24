package org.company.finance.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExpenseRecordTest {

    @Test
    void shouldRejectMismatchedExpenseType() {
        Category category = Category.restore(1, "工资", 0, ExpenseType.INCOME, false);

        assertThrows(IllegalArgumentException.class, () -> ExpenseRecord.create(
                1,
                category,
                Money.of(new BigDecimal("10.00")),
                ExpenseType.EXPENSE,
                ExpenseDate.of("2026-01-01"),
                Remark.of("测试")
        ));
    }

    @Test
    void shouldRejectUpdateAfterDelete() {
        Category category = Category.restore(2, "午餐", 1, ExpenseType.EXPENSE, false);
        ExpenseRecord expenseRecord = ExpenseRecord.create(
                1,
                category,
                Money.of(new BigDecimal("20.00")),
                ExpenseType.EXPENSE,
                ExpenseDate.of("2026-01-01"),
                Remark.of("午饭")
        );

        expenseRecord.delete();

        assertThrows(IllegalStateException.class, () -> expenseRecord.update(
                category,
                Money.of(new BigDecimal("25.00")),
                ExpenseType.EXPENSE,
                ExpenseDate.of("2026-01-02"),
                Remark.of("晚饭")
        ));
    }

    @Test
    void shouldAllowValidCreateAndUpdate() {
        Category category = Category.restore(2, "午餐", 1, ExpenseType.EXPENSE, false);

        ExpenseRecord expenseRecord = ExpenseRecord.create(
                1,
                category,
                Money.of(new BigDecimal("20.00")),
                ExpenseType.EXPENSE,
                ExpenseDate.of("2026-01-01"),
                Remark.of("午饭")
        );

        assertDoesNotThrow(() -> expenseRecord.update(
                category,
                Money.of(new BigDecimal("22.00")),
                ExpenseType.EXPENSE,
                ExpenseDate.of("2026-01-02"),
                Remark.of("晚饭")
        ));
    }
}
