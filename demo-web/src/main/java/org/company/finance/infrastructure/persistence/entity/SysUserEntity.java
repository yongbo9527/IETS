package org.company.finance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:16
 *
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private String username;

    private String password;

    private Integer statusFlag;

    private String createName;

    private LocalDateTime createTime;

    private String updateName;

    private LocalDateTime updateTime;

    private Integer delFlag;

    private LocalDateTime lastLoginTime;

    private String lastLoginIp;

    private Integer loginCount;

    private Integer failedLoginCount;

    private LocalDateTime lockUntil;
}
