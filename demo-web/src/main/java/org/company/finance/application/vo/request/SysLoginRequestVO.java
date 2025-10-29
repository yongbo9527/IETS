package org.company.finance.application.vo.request;

import lombok.Data;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:11
 *
 */
@Data
public class SysLoginRequestVO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 建议前端加密后传输（如 RSA、SM2），或使用 HTTPS 保证安全
     */
    private String password;

    /**
     * 图形验证码（防止暴力破解）
     */
    private String captchaCode;

    /**
     * 验证码唯一标识（与 captchaCode 配对）
     */
    private String captchaId;

    /**
     * 是否记住我：true=7天免登录，false=关闭浏览器失效
     */
    private Boolean rememberMe = false;
}
