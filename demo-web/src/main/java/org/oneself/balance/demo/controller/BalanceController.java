package org.oneself.balance.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oneself.balance.demo.entity.DailyExpenseRecordEntity;
import org.oneself.balance.demo.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public R addBalance(@RequestBody DailyExpenseRecordEntity entity) {
        balanceService.addBalance(entity);
        return R.ok("新增成功");
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
//        Page page = balanceService.queryBalance();
        return R.ok("");
    }


}
