package org.company.finance.interfaces.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.command.service.UserCommandService;
import org.company.finance.application.query.service.UserQueryService;
import org.company.finance.application.vo.UserInfoVO;
import org.company.finance.application.vo.request.RefreshTokenRequestVO;
import org.company.finance.application.vo.request.SysLoginRequestVO;
import org.company.finance.application.vo.response.LoginResponseVO;
import org.company.finance.common.util.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:56
 *
 */
@RestController
@RequestMapping("/sys")
@Tag(name = "登录接口")
@RequiredArgsConstructor
@Validated
public class SysLoginController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    public R<LoginResponseVO> login(@Validated @RequestBody SysLoginRequestVO requestVO, HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        LoginResponseVO login = userCommandService.login(requestVO, clientIp);
        return R.ok(login);
    }

    @PostMapping("/refreshToken")
    @Operation(summary = "刷新 Token")
    public R<LoginResponseVO> refreshToken(@Validated @RequestBody RefreshTokenRequestVO requestVO) {
        return R.ok(userCommandService.refreshToken(requestVO.getRefreshToken()));
    }

    @GetMapping("/logout")
    @Operation(summary = "退出")
    public R logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long) {
            Long userId = (Long) authentication.getPrincipal();
            userCommandService.logout(userId);
        }
        SecurityContextHolder.clearContext();
        return R.ok("退出成功");
    }

    @GetMapping("/userInfo")
    @Operation(summary = "获取当前用户信息")
    public R<UserInfoVO> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        
        UserInfoVO userInfo = userQueryService.getUserInfo(userId);
        return R.ok(userInfo);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
