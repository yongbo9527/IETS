package org.oneself.balance.demo.vo;

import lombok.Data;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:11
 *
 */
@Data
public class SysLoginRequestVO {
    private String username;
    private String password;
    private String captcha;
    private String uuid;
}
