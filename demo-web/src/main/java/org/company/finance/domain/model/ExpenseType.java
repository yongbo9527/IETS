package org.company.finance.domain.model;

/**
 * 收支类型
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
public enum ExpenseType {
    EXPENSE(1),
    INCOME(2);

    private final int code;

    ExpenseType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ExpenseType ofCode(Integer code) {
        if (code == null) {
            throw new IllegalArgumentException("收支类型不能为空");
        }
        for (ExpenseType expenseType : values()) {
            if (expenseType.code == code) {
                return expenseType;
            }
        }
        throw new IllegalArgumentException("收支类型不合法");
    }
}
