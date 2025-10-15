package org.company.finance.interfaces.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.query.service.FinanceQueryService;
import org.company.finance.application.command.service.FinanceCommandService;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.response.DynamicTableResponse;
import org.company.finance.common.util.R;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Ron Yu
 * @Create: 2024-08-30 10:35
 */
@RestController
@RequestMapping("/tally")
@Api(tags = "收支记录")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceCommandService commandService;
    private final FinanceQueryService queryService;

    @ApiOperation(value = "直接查询收支数据")
    @PostMapping("/listDailyRecords")
    public R<Page<DailyExpenseRecordEntity>> listDailyRecords(@RequestBody QueryBalanceVO vo) {
        return R.ok(queryService.listDailyRecords(vo));
    }


    @ApiOperation(value = "新增收支记录")
    @PutMapping("/saveRecord")
    public R saveRecord(@RequestBody DailyExpenseRecordEntity entity) {
        commandService.saveRecord(entity);
        return R.ok("新增成功");
    }

    @ApiOperation(value = "修改收支记录")
    @PutMapping("/updateRecord")
    public R updateRecord(@RequestBody DailyExpenseRecordEntity entity) {
        commandService.updateRecord(entity);
        return R.ok("修改成功");
    }

    @ApiOperation(value = "删除收支记录")
    @DeleteMapping("/deleteRecord")
    public R deleteRecord(Long id) {
        commandService.deleteRecord(id);
        return R.ok("删除成功");
    }

    @ApiOperation("查询收支余额表格")
    @PostMapping("/getBalanceTable")
    public R<DynamicTableResponse> getBalanceTable(@RequestBody QueryBalanceVO vo) {
        return R.ok(queryService.getBalanceTable(vo));
    }

    @ApiOperation(value = "查询收支记录结果集返回")
    @PostMapping("/getCompactBalanceTable")
    public R<DynamicTableResponse> getCompactBalanceTable(@RequestBody QueryBalanceVO vo) {
        return R.ok(queryService.getCompactBalanceTable(vo));
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
