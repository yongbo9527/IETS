package org.company.finance.interfaces.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.command.service.UserCommandService;
import org.company.finance.application.query.service.UserQueryService;
import org.company.finance.application.vo.UserInfoVO;
import org.company.finance.application.vo.request.SysLoginRequestVO;
import org.company.finance.application.vo.response.LoginResponseVO;
import org.company.finance.common.util.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:56
 *
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "登录接口")
@RequiredArgsConstructor
@Validated
public class SysLoginController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    @PostMapping("/login")
    @ApiOperation("登录")
    public R<LoginResponseVO> login(@Validated @RequestBody SysLoginRequestVO requestVO, HttpServletRequest httpRequest) {
        String clientIp = getClientIp(httpRequest);
        LoginResponseVO login = userCommandService.login(requestVO, clientIp);
        return R.ok(login);
    }

    @GetMapping("/logout")
    @ApiOperation("退出")
    public R logout() {
        SecurityContextHolder.clearContext();
        return R.ok("退出成功");
    }

    @GetMapping("/userInfo")
    @ApiOperation("获取当前用户信息")
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
