package org.company.finance.auth.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:36
 *
 */
@Data
@TableName("sys_user_token")
public class SysUserTokenEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private Long userId;

    private String accessToken;

    private LocalDateTime accessExpireTime;

    private String refreshToken;

    private LocalDateTime refreshExpireTime;

    private Integer tokenVersion;

    private Integer statusFlag;

    private LocalDateTime updateTime;
}
