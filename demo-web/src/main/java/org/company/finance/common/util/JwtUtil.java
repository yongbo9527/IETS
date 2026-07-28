package org.company.finance.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-29 19:40
 *  @Description:
 *
 */
@Component
public class JwtUtil {

    // 签名密钥（建议从配置文件中读取）
    @Value("${jwt.secret:mySecretKey}")
    private String secret;

    // Token过期时间（小时）
    @Value("${jwt.expire-hours:24}")
    private long expireHours;

    // 创建JWT Token
    public String generateToken(Long userId, String username) {
        return generateAccessToken(userId, username);
    }

    public String generateAccessToken(Long userId, String username) {
        return generateToken(userId, username, "access", null);
    }

    public String generateRefreshToken(Long userId, String username) {
        return generateToken(userId, username, "refresh", null);
    }

    public String generateToken(Long userId, String username, Map<String, Object> extraClaims) {
        return generateToken(userId, username, "access", extraClaims);
    }

    public String generateToken(Long userId, String username, String tokenType, Map<String, Object> extraClaims) {
        // 计算过期时间
        Date now = new Date();
        Date expireTime = new Date(now.getTime() + expireHours * 60 * 60 * 1000);

        // 创建JWT Builder
        return JWT.create()
                .withSubject(userId.toString())  // 主题，通常放用户ID
                .withClaim("username", username) // 自定义声明：用户名
                .withClaim("userId", userId)     // 自定义声明：用户ID
                .withClaim("tokenType", tokenType)
                .withIssuedAt(now)               // 签发时间
                .withExpiresAt(expireTime)       // 过期时间
                .withIssuer("your-app-name")     // 签发者
                .withJWTId(UUID.randomUUID().toString()) // JWT ID
                // 添加额外声明
                .withClaim("loginTime", System.currentTimeMillis())
                .sign(Algorithm.HMAC256(secret)); // 使用HMAC256签名
    }

    // 验证并解析Token
    public DecodedJWT verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("your-app-name")
                    .build();
            return verifier.verify(token);
        } catch (Exception e) {
            throw new RuntimeException("Token验证失败", e);
        }
    }

    // 从Token中获取用户ID
    public Long getUserIdFromToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim("userId").asLong();
    }

    // 从Token中获取用户名
    public String getUsernameFromToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim("username").asString();
    }

    public String getTokenType(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaim("tokenType").asString();
    }

    // 检查Token是否即将过期（用于刷新Token）
    public boolean isTokenExpiringSoon(String token) {
        try {
            DecodedJWT decodedJWT = verifyToken(token);
            Date expiresAt = decodedJWT.getExpiresAt();
            // 如果剩余时间小于30分钟，则认为即将过期
            return expiresAt.getTime() - System.currentTimeMillis() < 30 * 60 * 1000;
        } catch (Exception e) {
            return true;
        }
    }
}
