package org.company.finance.infrastructure.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.company.finance.common.util.JwtUtil;
import org.company.finance.infrastructure.persistence.entity.SysUserTokenEntity;
import org.company.finance.infrastructure.persistence.mapper.SysUserTokenMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private SysUserTokenMapper sysUserTokenMapper;

    @Mock
    private DecodedJWT decodedJWT;

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtil, sysUserTokenMapper);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldRejectBlacklistedAccessToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer access-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.verifyToken("access-token")).thenReturn(decodedJWT);
        com.auth0.jwt.interfaces.Claim claim = mock(com.auth0.jwt.interfaces.Claim.class);
        when(decodedJWT.getClaim("userId")).thenReturn(claim);
        when(claim.asLong()).thenReturn(1L);
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(1L);
        tokenEntity.setAccessToken("other-token");
        tokenEntity.setAccessExpireTime(LocalDateTime.now().plusMinutes(10));
        tokenEntity.setStatusFlag(1);
        when(sysUserTokenMapper.selectById(1L)).thenReturn(tokenEntity);

        jwtAuthenticationFilter.doFilter(request, response, new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void shouldRejectRefreshTokenAsAccessToken() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer refresh-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        when(jwtUtil.verifyToken("refresh-token")).thenReturn(decodedJWT);
        com.auth0.jwt.interfaces.Claim userIdClaim = mock(com.auth0.jwt.interfaces.Claim.class);
        com.auth0.jwt.interfaces.Claim usernameClaim = mock(com.auth0.jwt.interfaces.Claim.class);
        com.auth0.jwt.interfaces.Claim tokenTypeClaim = mock(com.auth0.jwt.interfaces.Claim.class);
        when(decodedJWT.getClaim("userId")).thenReturn(userIdClaim);
        when(decodedJWT.getClaim("username")).thenReturn(usernameClaim);
        when(decodedJWT.getClaim("tokenType")).thenReturn(tokenTypeClaim);
        when(userIdClaim.asLong()).thenReturn(1L);
        when(usernameClaim.asString()).thenReturn("ron");
        when(tokenTypeClaim.asString()).thenReturn("refresh");

        jwtAuthenticationFilter.doFilter(request, response, new MockFilterChain());

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
