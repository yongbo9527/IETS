package org.company.finance.application.vo.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:11
 *
 */
@Data
public class SysLoginRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 4, max = 100, message = "密码长度必须在4-100之间")
    private String password;

    @Size(max = 10, message = "验证码长度不能超过10")
    private String captchaCode;

    @Size(max = 64, message = "验证码ID长度不能超过64")
    private String captchaId;

    private Boolean rememberMe = false;
}
