package org.company.finance.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-30 21:16
 *
 */
@Data
@TableName("sys_user")
public class SysUserEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    private Long Id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐
     */
    private String salt;

    /**
     * 状态  0：禁用   1：正常
     */
    private Integer statusFlag;

    /**
     * 创建者
     */
    private String createName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新者
     */
    private String updateName;

    /**
     * 更新时间
     */
    private Date updateTime;



}
