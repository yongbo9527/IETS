package org.company.finance.application.vo;

import lombok.Data;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-17 14:37
 *  @Description: 用户信息数据传输对象,包含用户身份标识、展示信息及权限控制所需数据
 *
 */
@Data
public class UserInfoVO {

    /**
     * 用户唯一标识 ID
     * 数据库主键，用于唯一识别用户
     */
    private Long userId;

    /**
     * 登录账号/用户名
     * 可能是用户名、手机号或邮箱
     */
    private String username;

    /**
     * 用户真实姓名
     * 用于界面显示，如“欢迎回来，张三”
     */
    private String realName;

    /**
     * 用户头像 URL
     * 前端可用于展示用户头像
     * 示例值：https://example.com/avatar/1001.jpg
     */
    private String avatar;

    /**
     * 用户所属角色列表
     * 用于控制菜单展示和页面访问权限
     * 示例值：["admin", "user", "editor"]
     */
    private List<String> roles;

    /**
     * 用户拥有的权限码列表
     * 用于控制按钮级操作权限（如新增、删除、导出）
     * 示例值：["user:add", "user:delete", "order:export"]
     */
    private List<String> permissions;
}
