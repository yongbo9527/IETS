package org.company.finance.auth.interfaces.rest.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 刷新 Token 请求
 *
 * @author Ron Yu
 * @date 2026-07-28
 */
@Data
public class RefreshTokenRequestVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "refreshToken 不能为空")
    private String refreshToken;
}
