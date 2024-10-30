package org.oneself.balance.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oneself.balance.demo.service.SysUserService;
import org.oneself.balance.demo.service.SysUserTokenService;
import org.oneself.balance.demo.vo.SysLoginRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 20:56
 *
 */
@RestController
@RequestMapping("/sys")
@Api(tags = "登录接口")
public class SysLoginController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserTokenService sysUserTokenService;


    @PostMapping("/login")
    @ApiOperation("登录")
    public R login(@RequestBody SysLoginRequestVO requestVO) {

        R login = sysUserService.login(requestVO);
        return login;
    }

    @PostMapping("/sys/logout")
    @ApiOperation("退出")
    public R logout(Long userId) {
        sysUserTokenService.logout(userId);
        return R.ok("退出成功！");
    }


}
