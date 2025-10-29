package org.company.finance.application.command.service;

import com.auth0.jwt.JWT;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.company.finance.application.vo.UserInfoVO;
import org.company.finance.application.vo.request.SysLoginRequestVO;
import org.company.finance.application.vo.response.LoginResponseVO;
import org.company.finance.common.util.JwtUtil;
import org.company.finance.domain.repository.QueryCaptchaRepository;
import org.company.finance.domain.repository.QueryUserRepository;
import org.company.finance.domain.repository.UserPermissionRepository;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-17 14:32
 *  @Description:
 *
 */
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final QueryUserRepository queryUserRepository;
    private final UserPermissionRepository userPermissionRepository;

    private final QueryCaptchaRepository queryCaptchaRepository;

    private final JwtUtil jwtUtil;

    @Value("${jwt.expire.minutes:120}") // 默认2小时
    private int expireMinutes;

    @Value("${jwt.remember.me.minutes:10080}") // 7天 = 10080分钟
    private int rememberMeMinutes;

    public LoginResponseVO login(SysLoginRequestVO requestVO) {
        // 1. 验证验证码
        boolean isValidCaptcha = queryCaptchaRepository.validate(requestVO.getCaptchaId(), requestVO.getCaptchaCode());
        if (!isValidCaptcha) {
            throw new IllegalArgumentException("验证码错误或已失效");
        }

        // 2. 查询用户
        SysUserEntity user = queryUserRepository.findByUsername(requestVO.getUsername());
        if (user == null || user.getDelFlag() == 1) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (user.getStatusFlag() == 0) {
            throw new IllegalArgumentException("账户已被禁用");
        }

        // 3. 校验密码（假设前端已解密，后端对比）
        String rawPassword = decryptPassword(requestVO.getPassword()); // 可选：RSA 解密
        if (StringUtils.endsWith(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 4. 生成 Token（JWT 或 UUID）
        String token = jwtUtil.generateToken(user.getId(), null);
        String refreshToken = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 5. 计算过期时间
        LocalDateTime expireTime = requestVO.getRememberMe() != null && requestVO.getRememberMe()
                ? LocalDateTime.now().plusMinutes(rememberMeMinutes)
                : LocalDateTime.now().plusMinutes(expireMinutes);

        // 6. 存储或更新 token（实现“单点登录”）
        LoginResponseVO response = new LoginResponseVO();
        response.setToken(token);
        int expireMinute = requestVO.getRememberMe() ? rememberMeMinutes : expireMinutes;
        int expiresIn = expireMinute * 60; // 转为秒
        response.setRefreshToken(refreshToken);

        // 插入或替换（保证一人一端）
//        tokenMapper.replaceInto(userToken); // 使用 REPLACE INTO 或先删后插

        // 7. 更新用户最后登录信息
//        userMapper.updateLastLogin(user.getId(), getClientIp(), LocalDateTime.now());

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(null);
        userInfo.setAvatar(null);
        userInfo.setRoles(null);
        userInfo.setPermissions(null);

        response.setUserInfo(userInfo);

        return response;
    }

    private String decryptPassword(String encryptedPassword) {
        // TODO: RSA/SM2 解密
        return encryptedPassword;
    }

}
