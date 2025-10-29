package org.company.finance.application.vo.response;

import lombok.Data;
import org.company.finance.application.vo.UserInfoVO;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-28 15:17
 *  @Description: 登录接口返回的响应数据对象（VO）,包含 Token 信息、过期时间、刷新令牌和用户基本信息
 *
 */
@Data
public class LoginResponseVO {
    /**
     * 访问令牌（Access Token）
     * 前端在后续请求中需将其放入请求头：
     * Authorization: Bearer <token>
     */
    private String token;

    /**
     * Token 有效期（单位：秒）
     * 例如：7200 表示 2 小时
     * 前端可据此判断是否需要刷新 Token
     */
    private Integer expiresIn;

    /**
     * 刷新令牌（Refresh Token）
     * 当 Access Token 过期后，可用此 Token 获取新的 Token
     * 提高安全性，避免频繁输入密码
     * （可选字段，根据系统安全策略决定是否返回）
     */
    private String refreshToken;

    /**
     * 用户基本信息与权限数据
     * 避免登录后再次调用 /user/info 接口获取信息
     * 提升首屏加载速度
     */
    private UserInfoVO userInfo;
}
