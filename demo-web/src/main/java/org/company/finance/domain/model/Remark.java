package org.company.finance.domain.model;

/**
 * 备注值对象
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
public class Remark {

    private static final int MAX_LENGTH = 500;

    private final String value;

    private Remark(String value) {
        this.value = value;
    }

    public static Remark of(String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("备注长度不能超过500");
        }
        return new Remark(value);
    }

    public String getValue() {
        return value;
    }
}
