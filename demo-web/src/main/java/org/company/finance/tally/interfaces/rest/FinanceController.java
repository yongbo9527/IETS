package org.company.finance.tally.interfaces.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.company.finance.tally.application.command.FinanceCommandService;
import org.company.finance.tally.application.query.FinanceQueryService;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.tally.application.ImportExportService;
import org.company.finance.application.vo.response.DynamicTableResponse;
import org.company.finance.tally.interfaces.rest.request.ImportExportRequestVO;
import org.company.finance.common.util.R;
import org.company.finance.tally.interfaces.rest.request.CreateExpenseRecordRequest;
import org.company.finance.tally.interfaces.rest.request.UpdateExpenseRecordRequest;
import org.company.finance.tally.interfaces.rest.response.ExpenseRecordResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import java.io.IOException;

/**
 * @Author: Ron Yu
 * @Create: 2024-08-30 10:35
 */
@RestController
@RequestMapping("/records")
@Api(tags = "收支记录")
@RequiredArgsConstructor
@Validated
public class FinanceController {

    private final FinanceCommandService commandService;
    private final FinanceQueryService queryService;
    private final ImportExportService importExportService;

    @ApiOperation(value = "直接查询收支数据")
    @PostMapping("/search")
    public R<Page<ExpenseRecordResponse>> listDailyRecords(@Validated @RequestBody QueryBalanceVO vo) {
        return R.ok(queryService.listDailyRecords(vo));
    }


    @ApiOperation(value = "新增收支记录")
    @PostMapping
    public R saveRecord(@Validated @RequestBody CreateExpenseRecordRequest request) {
        commandService.saveRecord(request);
        return R.ok("新增成功");
    }

    @ApiOperation(value = "修改收支记录")
    @PutMapping("/{id}")
    public R updateRecord(@PathVariable @Min(value = 1, message = "ID不能小于1") Integer id,
                          @Validated @RequestBody UpdateExpenseRecordRequest request) {
        request.setId(id);
        commandService.updateRecord(request);
        return R.ok("修改成功");
    }

    @ApiOperation(value = "删除收支记录")
    @DeleteMapping("/{id}")
    public R deleteRecord(@PathVariable @Min(value = 1, message = "ID不能小于1") Long id) {
        commandService.deleteRecord(id);
        return R.ok("删除成功");
    }

    @ApiOperation("查询收支余额表格")
    @PostMapping("/balance-table")
    public R<DynamicTableResponse> getBalanceTable(@Validated @RequestBody QueryBalanceVO vo) {
        return R.ok(queryService.getBalanceTable(vo));
    }

    @ApiOperation(value = "查询收支记录结果集返回")
    @PostMapping("/compact-balance-table")
    public R<DynamicTableResponse> getCompactBalanceTable(@Validated @RequestBody QueryBalanceVO vo) {
        return R.ok(queryService.getCompactBalanceTable(vo));
    }

    @ApiOperation(value = "数据模板导出")
    @PostMapping("/template/export")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        importExportService.exportTemplate(response);
    }

    @ApiOperation(value = "数据导入")
    @PostMapping("/import")
    public R importData(@RequestParam("file") MultipartFile file) throws Exception {
        String result = importExportService.importData(file);
        return R.ok(result);
    }

    @ApiOperation(value = "数据导出")
    @PostMapping("/export")
    public void exportData(@Validated @RequestBody ImportExportRequestVO vo, HttpServletResponse response) throws IOException {
        importExportService.exportData(vo, response);
    }
}
