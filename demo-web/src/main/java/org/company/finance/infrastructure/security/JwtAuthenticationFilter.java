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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                if (isAccessToken(decodedJWT) && isValidUserToken(userId, token)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
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
        return "access".equals(tokenType);
    }

    private boolean isValidUserToken(Long userId, String accessToken) {
        SysUserTokenEntity tokenEntity = sysUserTokenMapper.selectById(userId);
        if (tokenEntity == null) {
            return false;
        }
        if (!Integer.valueOf(1).equals(tokenEntity.getStatusFlag())) {
            return false;
        }
        if (!accessToken.equals(tokenEntity.getAccessToken())) {
            return false;
        }
        return tokenEntity.getAccessExpireTime() != null
                && tokenEntity.getAccessExpireTime().isAfter(LocalDateTime.now());
    }
}
