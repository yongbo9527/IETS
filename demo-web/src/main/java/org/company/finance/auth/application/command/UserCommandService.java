package org.company.finance.auth.application.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.finance.auth.domain.repository.CommandUserRepository;
import org.company.finance.auth.domain.repository.QueryCaptchaRepository;
import org.company.finance.auth.domain.repository.QueryUserRepository;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserInfoEntity;
import org.company.finance.auth.infrastructure.persistence.entity.SysUserTokenEntity;
import org.company.finance.auth.infrastructure.persistence.mapper.SysUserTokenMapper;
import org.company.finance.auth.interfaces.rest.request.RegisterRequestVO;
import org.company.finance.auth.interfaces.rest.request.SysLoginRequestVO;
import org.company.finance.auth.interfaces.rest.response.LoginResponseVO;
import org.company.finance.auth.interfaces.rest.response.UserInfoVO;
import org.company.finance.common.util.JwtUtil;
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
    private final SysUserTokenMapper sysUserTokenMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${jwt.expire-minutes:120}")
    private int expireMinutes;

    @org.springframework.beans.factory.annotation.Value("${jwt.access-expire-minutes:120}")
    private int accessExpireMinutes;

    @org.springframework.beans.factory.annotation.Value("${jwt.remember-me-minutes:10080}")
    private int rememberMeMinutes;

    @org.springframework.beans.factory.annotation.Value("${jwt.refresh-expire-days:7}")
    private int refreshExpireDays;

    @Transactional(noRollbackFor = IllegalArgumentException.class)
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
                ? rememberMeMinutes : accessExpireMinutes;

        String token = jwtUtil.generateAccessToken(user.getId(), user.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getUsername());
        persistUserToken(user.getId(), token, refreshToken, expireMinute);

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

    @Transactional
    public LoginResponseVO refreshToken(String refreshToken) {
        Long userId = jwtUtil.getUserIdFromToken(refreshToken);
        String username = jwtUtil.getUsernameFromToken(refreshToken);
        String tokenType = jwtUtil.getTokenType(refreshToken);
        if (!"refresh".equals(tokenType)) {
            throw new IllegalArgumentException("refreshToken 类型不正确");
        }

        SysUserTokenEntity tokenEntity = sysUserTokenMapper.selectById(userId);
        if (tokenEntity == null || !Integer.valueOf(1).equals(tokenEntity.getStatusFlag())) {
            throw new IllegalArgumentException("refreshToken 已失效");
        }
        if (!refreshToken.equals(tokenEntity.getRefreshToken())) {
            throw new IllegalArgumentException("refreshToken 不匹配");
        }
        if (tokenEntity.getRefreshExpireTime() == null || tokenEntity.getRefreshExpireTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("refreshToken 已过期");
        }

        String newAccessToken = jwtUtil.generateAccessToken(userId, username);
        String newRefreshToken = jwtUtil.generateRefreshToken(userId, username);
        persistUserToken(userId, newAccessToken, newRefreshToken, accessExpireMinutes);

        LoginResponseVO response = new LoginResponseVO();
        response.setToken(newAccessToken);
        response.setRefreshToken(newRefreshToken);
        response.setExpiresIn(accessExpireMinutes * 60);
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

    @Transactional
    public void logout(Long userId) {
        SysUserTokenEntity tokenEntity = sysUserTokenMapper.selectById(userId);
        if (tokenEntity == null) {
            return;
        }
        tokenEntity.setAccessToken(null);
        tokenEntity.setAccessExpireTime(LocalDateTime.now());
        tokenEntity.setRefreshToken(null);
        tokenEntity.setRefreshExpireTime(LocalDateTime.now());
        tokenEntity.setStatusFlag(0);
        tokenEntity.setUpdateTime(LocalDateTime.now());
        tokenEntity.setTokenVersion((tokenEntity.getTokenVersion() == null ? 0 : tokenEntity.getTokenVersion()) + 1);
        sysUserTokenMapper.updateById(tokenEntity);
    }

    /**
     * 持久化当前用户的 token 会话状态。
     *
     * <p>登录和 refresh 成功后都会调用这里，把当前唯一有效的 accessToken 和 refreshToken
     * 写入 {@code sys_user_token}。如果用户第一次登录就插入记录；如果已经存在会话记录，
     * 则直接覆盖旧 token，实现旧 token 失效、当前 token 生效。</p>
     *
     * <p>这里同时更新 access/refresh 过期时间、状态位和更新时间，供
     * {@code JwtAuthenticationFilter} 以及 refreshToken 流程做服务端校验。</p>
     *
     * @param userId              用户ID
     * @param accessToken         新签发的 access token
     * @param refreshToken        新签发的 refresh token
     * @param accessExpireMinutes access token 过期分钟数
     */
    private void persistUserToken(Long userId, String accessToken, String refreshToken, int accessExpireMinutes) {
        LocalDateTime now = LocalDateTime.now();
        SysUserTokenEntity tokenEntity = sysUserTokenMapper.selectById(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setTokenVersion(1);
        }
        tokenEntity.setAccessToken(accessToken);
        tokenEntity.setAccessExpireTime(now.plusMinutes(accessExpireMinutes));
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setRefreshExpireTime(now.plusDays(refreshExpireDays));
        tokenEntity.setStatusFlag(1);
        tokenEntity.setUpdateTime(now);

        if (sysUserTokenMapper.selectById(userId) == null) {
            sysUserTokenMapper.insert(tokenEntity);
            return;
        }
        sysUserTokenMapper.updateById(tokenEntity);
    }
}
