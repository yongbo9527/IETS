package org.company.finance.application.command.service;

import org.company.finance.application.vo.request.SysLoginRequestVO;
import org.company.finance.application.vo.response.LoginResponseVO;
import org.company.finance.domain.repository.CommandUserRepository;
import org.company.finance.domain.repository.QueryCaptchaRepository;
import org.company.finance.domain.repository.QueryUserRepository;
import org.company.finance.infrastructure.persistence.entity.SysUserEntity;
import org.company.finance.infrastructure.persistence.entity.SysUserTokenEntity;
import org.company.finance.infrastructure.persistence.mapper.SysUserTokenMapper;
import org.company.finance.common.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserCommandServiceTest {

    @Mock
    private QueryUserRepository queryUserRepository;

    @Mock
    private CommandUserRepository commandUserRepository;

    @Mock
    private QueryCaptchaRepository queryCaptchaRepository;

    @Mock
    private SysUserTokenMapper sysUserTokenMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserCommandService userCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userCommandService = new UserCommandService(
                queryUserRepository,
                commandUserRepository,
                queryCaptchaRepository,
                sysUserTokenMapper,
                jwtUtil,
                passwordEncoder
        );
        ReflectionTestUtils.setField(userCommandService, "expireMinutes", 120);
        ReflectionTestUtils.setField(userCommandService, "rememberMeMinutes", 10080);
        ReflectionTestUtils.setField(userCommandService, "refreshExpireDays", 7);
    }

    @Test
    void shouldPersistRefreshTokenOnLogin() {
        SysLoginRequestVO requestVO = new SysLoginRequestVO();
        requestVO.setUsername("ron");
        requestVO.setPassword("123456");

        SysUserEntity user = new SysUserEntity();
        user.setId(1L);
        user.setUsername("ron");
        user.setPassword("encoded");
        user.setStatusFlag(1);
        user.setDelFlag(0);
        user.setLoginCount(0);
        user.setFailedLoginCount(0);

        when(queryUserRepository.findByUsername("ron")).thenReturn(user);
        when(passwordEncoder.matches("123456", "encoded")).thenReturn(true);
        when(jwtUtil.generateAccessToken(1L, "ron")).thenReturn("access-token");
        when(jwtUtil.generateRefreshToken(1L, "ron")).thenReturn("refresh-token");
        when(sysUserTokenMapper.selectById(1L)).thenReturn(null);

        userCommandService.login(requestVO, "127.0.0.1");

        verify(sysUserTokenMapper, times(1)).insert(any(SysUserTokenEntity.class));
    }

    @Test
    void shouldInvalidateRefreshTokenOnLogout() {
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(1L);
        tokenEntity.setRefreshToken("refresh-token");
        tokenEntity.setRefreshExpireTime(LocalDateTime.now().plusDays(7));
        when(sysUserTokenMapper.selectById(1L)).thenReturn(tokenEntity);

        userCommandService.logout(1L);

        verify(sysUserTokenMapper, times(1)).updateById(any(SysUserTokenEntity.class));
    }

    @Test
    void shouldRefreshTokenAndRotateRefreshToken() {
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(1L);
        tokenEntity.setRefreshToken("old-refresh-token");
        tokenEntity.setRefreshExpireTime(LocalDateTime.now().plusDays(7));
        tokenEntity.setStatusFlag(1);
        com.auth0.jwt.interfaces.DecodedJWT decodedJWT = mockDecodedJwt(1L, "ron", "refresh");
        when(sysUserTokenMapper.selectById(1L)).thenReturn(tokenEntity);
        when(jwtUtil.verifyToken("old-refresh-token")).thenReturn(decodedJWT);
        when(jwtUtil.getUserIdFromToken("old-refresh-token")).thenReturn(1L);
        when(jwtUtil.getUsernameFromToken("old-refresh-token")).thenReturn("ron");
        when(jwtUtil.getTokenType("old-refresh-token")).thenReturn("refresh");
        when(jwtUtil.generateAccessToken(1L, "ron")).thenReturn("new-access-token");
        when(jwtUtil.generateRefreshToken(1L, "ron")).thenReturn("new-refresh-token");

        LoginResponseVO response = userCommandService.refreshToken("old-refresh-token");

        assertEquals("new-access-token", response.getToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        verify(sysUserTokenMapper, times(1)).updateById(any(SysUserTokenEntity.class));
    }

    @Test
    void shouldRejectExpiredRefreshToken() {
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(1L);
        tokenEntity.setRefreshToken("old-refresh-token");
        tokenEntity.setRefreshExpireTime(LocalDateTime.now().minusMinutes(1));
        tokenEntity.setStatusFlag(1);
        com.auth0.jwt.interfaces.DecodedJWT decodedJWT = mockDecodedJwt(1L, "ron", "refresh");
        when(sysUserTokenMapper.selectById(1L)).thenReturn(tokenEntity);
        when(jwtUtil.verifyToken("old-refresh-token")).thenReturn(decodedJWT);
        when(jwtUtil.getUserIdFromToken("old-refresh-token")).thenReturn(1L);
        when(jwtUtil.getUsernameFromToken("old-refresh-token")).thenReturn("ron");
        when(jwtUtil.getTokenType("old-refresh-token")).thenReturn("refresh");

        assertThrows(IllegalArgumentException.class, () -> userCommandService.refreshToken("old-refresh-token"));
    }

    @Test
    void shouldNotRollbackFailedLoginCountWhenPasswordIsWrong() throws Exception {
        Method loginMethod = UserCommandService.class.getMethod("login", SysLoginRequestVO.class, String.class);
        Transactional transactional = loginMethod.getAnnotation(Transactional.class);

        org.junit.jupiter.api.Assertions.assertTrue(
                Arrays.asList(transactional.noRollbackFor()).contains(IllegalArgumentException.class)
        );
    }

    private com.auth0.jwt.interfaces.DecodedJWT mockDecodedJwt(Long userId, String username, String tokenType) {
        com.auth0.jwt.interfaces.DecodedJWT decodedJWT = org.mockito.Mockito.mock(com.auth0.jwt.interfaces.DecodedJWT.class);
        com.auth0.jwt.interfaces.Claim userIdClaim = org.mockito.Mockito.mock(com.auth0.jwt.interfaces.Claim.class);
        com.auth0.jwt.interfaces.Claim usernameClaim = org.mockito.Mockito.mock(com.auth0.jwt.interfaces.Claim.class);
        com.auth0.jwt.interfaces.Claim tokenTypeClaim = org.mockito.Mockito.mock(com.auth0.jwt.interfaces.Claim.class);
        when(userIdClaim.asLong()).thenReturn(userId);
        when(usernameClaim.asString()).thenReturn(username);
        when(tokenTypeClaim.asString()).thenReturn(tokenType);
        when(decodedJWT.getClaim("userId")).thenReturn(userIdClaim);
        when(decodedJWT.getClaim("username")).thenReturn(usernameClaim);
        when(decodedJWT.getClaim("tokenType")).thenReturn(tokenTypeClaim);
        return decodedJWT;
    }
}
