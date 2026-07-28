package org.company.finance.auth.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.company.finance.auth.application.command.UserCommandService;
import org.company.finance.auth.interfaces.rest.request.RegisterRequestVO;
import org.company.finance.common.util.R;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:54
 *
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口")
@RequiredArgsConstructor
@Validated
public class SysUserController {

    private final UserCommandService userCommandService;

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public R register(@Validated @RequestBody RegisterRequestVO requestVO) {
        userCommandService.register(requestVO);
        return R.ok("注册成功");
    }

    @PostMapping("/deactivate")
    @Operation(summary = "账号注销")
    public R deactivate() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        userCommandService.deactivate(userId);
        SecurityContextHolder.clearContext();
        return R.ok("注销成功");
    }
}
