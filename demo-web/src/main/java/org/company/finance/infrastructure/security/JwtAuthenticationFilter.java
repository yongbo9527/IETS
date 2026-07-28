package org.company.finance.infrastructure.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.company.finance.common.util.JwtUtil;
import org.company.finance.infrastructure.persistence.entity.SysUserTokenEntity;
import org.company.finance.infrastructure.persistence.mapper.SysUserTokenMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 *  @Author: Ron Yu
 *  @Create: 2026-04-02
 *
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SysUserTokenMapper sysUserTokenMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = getTokenFromRequest(request);
            if (StringUtils.hasText(token)) {
                DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
                Long userId = decodedJWT.getClaim("userId").asLong();
                boolean accessToken = isAccessToken(decodedJWT);
                boolean validUserToken = isValidUserToken(userId, token);
                if (accessToken && validUserToken) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    log.info("JWT认证未通过, userId={}, isAccessToken={}, isValidUserToken={}",
                            userId, accessToken, validUserToken);
                }
            }
        } catch (Exception e) {
            log.warn("JWT认证失败: {}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isAccessToken(DecodedJWT decodedJWT) {
        String tokenType = decodedJWT.getClaim("tokenType").asString();
        boolean isAccessToken = "access".equals(tokenType);
        if (!isAccessToken) {
            log.info("JWT tokenType 不正确, expected=access, actual={}", tokenType);
        }
        return isAccessToken;
    }

    private boolean isValidUserToken(Long userId, String accessToken) {
        SysUserTokenEntity tokenEntity = sysUserTokenMapper.selectById(userId);
        if (tokenEntity == null) {
            log.info("JWT会话不存在, userId={}", userId);
            return false;
        }
        if (!Integer.valueOf(1).equals(tokenEntity.getStatusFlag())) {
            log.info("JWT会话状态无效, userId={}, statusFlag={}", userId, tokenEntity.getStatusFlag());
            return false;
        }
        if (!accessToken.equals(tokenEntity.getAccessToken())) {
            boolean matchesRefreshToken = accessToken.equals(tokenEntity.getRefreshToken());
            log.info("JWT accessToken 不匹配, userId={}, matchesRefreshToken={}", userId, matchesRefreshToken);
            return false;
        }
        if (tokenEntity.getAccessExpireTime() == null) {
            log.info("JWT accessToken 过期时间为空, userId={}", userId);
            return false;
        }
        boolean notExpired = tokenEntity.getAccessExpireTime().isAfter(LocalDateTime.now());
        if (!notExpired) {
            log.info("JWT accessToken 已过期, userId={}, accessExpireTime={}", userId, tokenEntity.getAccessExpireTime());
        }
        return notExpired;
    }
}
