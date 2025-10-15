package org.company.finance.common.util;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-14 11:45
 *  @Description:
 *
 */
public enum ApiErrorCode {
    FAILED(0L, "操作失败"),
    SUCCESS(200L, "执行成功");

    private final long code;
    private final String msg;

    ApiErrorCode(long code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public long getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
