package org.oneself.balance.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oneself.balance.demo.entity.DailyExpenseRecordEntity;
import org.oneself.balance.demo.service.BalanceService;
import org.oneself.balance.demo.vo.balance.QueryBalanceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Ron Yu
 * @Create: 2024-08-30 10:35
 */
@RestController
@RequestMapping("/tally")
@Api(tags = "收支记录")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;


    @ApiOperation(value = "直接查询收支数据")
    @PostMapping("/queryDailyExpenseRecordBalance")
    public R queryDailyExpenseRecordBalance(@RequestBody QueryBalanceVO vo) {
        R result = balanceService.queryDailyExpenseRecordBalance(vo);
        return result;
    }


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
    public R queryBalance(@RequestBody QueryBalanceVO vo) {
        R result = balanceService.queryBalance(vo);
        return result;
    }

    @ApiOperation(value = "查询收支记录结果集返回")
    @PostMapping("/queryBalanceShortHead")
    public R queryBalanceShortHead(@RequestBody QueryBalanceVO vo) {
        R result = balanceService.queryBalanceShortHead(vo);
        return result;
    }
    @ApiOperation(value = "查询类目结构")
    @PostMapping("/queryTreeCategoryList")
    public R queryTreeCategoryList() {
        R result = balanceService.queryTreeCategoryList();
        return result;
    }


    // TODO 模板导出
    @ApiOperation(value = "数据模板导出")
    @PostMapping("/exportTemplate")
    public R exportTemplate() {
        return R.ok("导出成功");
    }
    // TODO 数据导入
    @ApiOperation(value = "数据导入")
    @PostMapping("/importData")
    public R importData() {
        return R.ok("导入成功");
    }

    // TODO 数据导出
    @ApiOperation(value = "数据导出")
    @PostMapping("/exportData")
    public R exportData() {
        return R.ok("导出成功");
    }
}
