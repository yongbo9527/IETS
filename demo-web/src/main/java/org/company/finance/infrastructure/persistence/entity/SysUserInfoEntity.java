package org.company.finance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("sys_user_info")
public class SysUserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    private Long id;

    private Long userId;

    private String realName;

    private String email;

    private String phone;

    private Integer gender;

    private LocalDateTime birthday;

    private String avatar;

    private String department;

    private String position;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
