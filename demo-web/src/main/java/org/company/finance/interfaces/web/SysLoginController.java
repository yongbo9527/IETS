package org.company.finance.interfaces.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.command.service.UserCommandService;
import org.company.finance.application.vo.UserInfoVO;
import org.company.finance.application.query.service.UserQueryService;
import org.company.finance.application.vo.request.SysLoginRequestVO;
import org.company.finance.application.vo.response.LoginResponseVO;
import org.company.finance.common.util.R;
import org.springframework.web.bind.annotation.*;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:56
 *
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "登录接口")
@RequiredArgsConstructor
public class SysLoginController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    /**
     * 登录
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public R<LoginResponseVO> login(@RequestBody SysLoginRequestVO requestVO) {
        LoginResponseVO  login = userCommandService.login(requestVO);
        return R.ok(login);
    }

    /**
     * 退出
     */
//    @PostMapping("/sys/logout")
//    @ApiOperation("退出")
//    public R logout(@RequestParam Long userId) {
////        LogoutCommand command = new LogoutCommand(userId);
//        return userCommandService.logout(command);
//    }

    /**
     * 查询用户信息（新增）
     */
//    @GetMapping("/userInfo")
//    @ApiOperation("获取当前用户信息")
//    public R getUserInfo(@RequestParam Long userId) {
//        UserInfoVO userInfo = userQueryService.getUserInfo(userId);
//        return R.ok(userInfo);
//    }

    /**
     * 查询用户权限（新增）
     */
//    @GetMapping("/userPermissions")
//    @ApiOperation("获取用户权限")
//    public R getUserPermissions(@RequestParam Long userId) {
//        UserPermissionDTO permissions = userQueryService.getUserPermissions(userId);
//        return R.ok(permissions);
//    }


}
