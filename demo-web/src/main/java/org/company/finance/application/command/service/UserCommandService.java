package org.company.finance.application.command.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.finance.application.vo.UserInfoVO;
import org.company.finance.application.vo.request.RegisterRequestVO;
import org.company.finance.application.vo.request.SysLoginRequestVO;
import org.company.finance.application.vo.response.LoginResponseVO;
import org.company.finance.common.util.JwtUtil;
import org.company.finance.domain.repository.CommandUserRepository;
import org.company.finance.domain.repository.QueryCaptchaRepository;
import org.company.finance.domain.repository.QueryUserRepository;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.infrastructure.persistence.entity.SysUserInfoEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int LOCK_MINUTES = 30;

    private final QueryUserRepository queryUserRepository;
    private final CommandUserRepository commandUserRepository;
    private final QueryCaptchaRepository queryCaptchaRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expire-minutes:120}")
    private int expireMinutes;

    @Value("${jwt.remember-me-minutes:10080}")
    private int rememberMeMinutes;

    @Transactional
    public LoginResponseVO login(SysLoginRequestVO requestVO, String clientIp) {
        // 1. 验证验证码（可选）
        if (requestVO.getCaptchaId() != null && requestVO.getCaptchaCode() != null) {
            boolean isValidCaptcha = queryCaptchaRepository.validate(requestVO.getCaptchaId(), requestVO.getCaptchaCode());
            if (!isValidCaptcha) {
                throw new IllegalArgumentException("验证码错误或已失效");
            }
        }

        // 2. 查询用户
        SysUserEntity user = queryUserRepository.findByUsername(requestVO.getUsername());
        if (user == null || user.getDelFlag() == 1) {
            throw new IllegalArgumentException("用户名或密码错误");
        }
        if (user.getStatusFlag() == 0) {
            throw new IllegalArgumentException("账户已被禁用");
        }

        // 3. 检查账户是否被锁定
        if (user.getLockUntil() != null && user.getLockUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("账户已被锁定，请" + LOCK_MINUTES + "分钟后再试");
        }

        // 4. 校验密码（使用 BCrypt）
        if (!passwordEncoder.matches(requestVO.getPassword(), user.getPassword())) {
            // 密码错误，记录失败次数
            handleLoginFailure(user);
            throw new IllegalArgumentException("用户名或密码错误");
        }

        // 5. 登录成功，重置失败计数，更新登录信息
        handleLoginSuccess(user, clientIp);

        // 6. 生成 Token
        int expireMinute = (requestVO.getRememberMe() != null && requestVO.getRememberMe()) 
                ? rememberMeMinutes : expireMinutes;
        
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateToken(user.getId(), user.getUsername());

        // 7. 构建返回对象
        LoginResponseVO response = new LoginResponseVO();
        response.setToken(token);
        response.setRefreshToken(refreshToken);
        response.setExpiresIn(expireMinute * 60);

        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setUserId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setRealName(user.getUsername());
        userInfo.setRoles(Collections.singletonList("USER"));
        userInfo.setPermissions(Collections.emptyList());

        response.setUserInfo(userInfo);

        return response;
    }

    private void handleLoginFailure(SysUserEntity user) {
        int failedCount = (user.getFailedLoginCount() == null ? 0 : user.getFailedLoginCount()) + 1;
        user.setFailedLoginCount(failedCount);
        
        // 连续失败 5 次，锁定账户
        if (failedCount >= MAX_FAILED_ATTEMPTS) {
            user.setLockUntil(LocalDateTime.now().plusMinutes(LOCK_MINUTES));
            log.warn("用户 {} 连续失败 {} 次，锁定账户 {} 分钟", 
                    user.getUsername(), failedCount, LOCK_MINUTES);
        }
        
        commandUserRepository.update(user);
    }

    private void handleLoginSuccess(SysUserEntity user, String clientIp) {
        // 重置失败计数
        user.setFailedLoginCount(0);
        user.setLockUntil(null);
        
        // 更新登录信息
        user.setLastLoginTime(LocalDateTime.now());
        user.setLastLoginIp(clientIp);
        user.setLoginCount((user.getLoginCount() == null ? 0 : user.getLoginCount()) + 1);
        
        commandUserRepository.update(user);
    }

    @Transactional
    public void register(RegisterRequestVO requestVO) {
        // 1. 校验两次密码是否一致
        if (!requestVO.getPassword().equals(requestVO.getConfirmPassword())) {
            throw new IllegalArgumentException("两次密码输入不一致");
        }

        // 2. 检查用户名是否已存在
        SysUserEntity existUser = queryUserRepository.findByUsername(requestVO.getUsername());
        if (existUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 3. 创建新用户
        SysUserEntity user = new SysUserEntity();
        user.setUsername(requestVO.getUsername());
        user.setPassword(passwordEncoder.encode(requestVO.getPassword()));
        user.setStatusFlag(1);
        user.setDelFlag(0);
        user.setCreateTime(LocalDateTime.now());
        user.setLoginCount(0);
        user.setFailedLoginCount(0);

        commandUserRepository.save(user);

        // 4. 自动创建用户详细信息记录
        SysUserInfoEntity userInfo = new SysUserInfoEntity();
        userInfo.setUserId(user.getId());
        userInfo.setRealName(requestVO.getUsername());
        userInfo.setCreateTime(LocalDateTime.now());
        commandUserRepository.saveUserInfo(userInfo);
    }

    @Transactional
    public void deactivate(Long userId) {
        SysUserEntity user = queryUserRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        user.setDelFlag(1);
        user.setUpdateTime(LocalDateTime.now());
        commandUserRepository.update(user);
    }
}
