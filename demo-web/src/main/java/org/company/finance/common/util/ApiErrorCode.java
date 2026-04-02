package org.company.finance.common.util;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-14 11:45
 *  @Description:
 *
 */
public enum ApiErrorCode {
    FAILED(0L, "操作失败"),
    SUCCESS(200L, "执行成功"),
    PARAM_ERROR(400L, "参数错误"),
    NOT_FOUND(404L, "资源不存在"),
    UNAUTHORIZED(401L, "未授权"),
    FORBIDDEN(403L, "禁止访问"),
    INTERNAL_SERVER_ERROR(500L, "系统内部错误");

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
