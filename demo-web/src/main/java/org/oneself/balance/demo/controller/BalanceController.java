package org.oneself.balance.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oneself.balance.demo.service.BalanceService;
import org.oneself.balance.demo.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Ron Yu
 * @Create: 2024-08-30 10:35
 */
@RestController
@RequestMapping("/balance")
@Api(tags = "收支记录")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @ApiOperation(value = "新增收支记录")
    @PutMapping("/addBalance")
    public void addBalance() {

    }

    @ApiOperation(value = "修改收支记录")
    @PutMapping("/updateBalance")
    public void updateBalance() {

    }

    @ApiOperation(value = "删除收支记录")
    @PutMapping("/deleteBalance")
    public void deleteBalance() {

    }

    @ApiOperation(value = "查询收支记录")
    @PostMapping("/queryBalance")
    public R queryBalance() {

        return R.ok();
    }


}
