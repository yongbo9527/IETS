package org.company.finance.report.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.company.finance.common.util.R;
import org.company.finance.report.application.query.ReportQueryService;
import org.company.finance.report.interfaces.rest.request.ReportRequestVO;
import org.company.finance.report.interfaces.rest.response.BigCategoryExpenseResponse;
import org.company.finance.report.interfaces.rest.response.ExpenseDetailResponse;
import org.company.finance.report.interfaces.rest.response.IncomeExpenseDataVO;
import org.company.finance.report.interfaces.rest.response.LineEchartsResponse;
import org.company.finance.report.interfaces.rest.response.ReportDatasetResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:05
 *
 */
@RestController
@RequestMapping("/reports")
@Tag(name = "报表")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportQueryService reportQueryService;

    @Operation(summary = "按日统计支出与收入")
    @GetMapping("/daily-expense")
    public R<Map<String, IncomeExpenseDataVO>> getDailyExpense(@NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}$", message = "日期格式应为 yyyy-MM") @RequestParam("searchDate") String searchDate) {
        Map<String, IncomeExpenseDataVO> map = reportQueryService.getDailyExpense(searchDate);
        return R.ok(map);
    }

    @Operation(summary = "折线图：按月支出与收入")
    @PostMapping("/month-expense")
    public R<LineEchartsResponse> getMonthExpense(@Validated @RequestBody ReportRequestVO request) {
        LineEchartsResponse lineEchartsResponse = reportQueryService.getMonthExpense(request);
        return R.ok(lineEchartsResponse);
    }

    @Operation(summary = "饼图：大类支出")
    @PostMapping("/big-category-expense")
    public R<List<BigCategoryExpenseResponse>> getBigCategoryExpense(@Validated @RequestBody ReportRequestVO request) {
        List<BigCategoryExpenseResponse> pieEchartsResponse = reportQueryService.getBigCategoryExpense(request);
        return R.ok(pieEchartsResponse);
    }

    @Operation(summary = "查询大类支出明细")
    @PostMapping("/big-category-expense-detail")
    public R<List<BigCategoryExpenseResponse>> getBigCategoryExpenseDetail(@Validated @RequestBody ReportRequestVO request) {
        List<BigCategoryExpenseResponse> pieEchartsResponse = reportQueryService.getBigCategoryExpenseDetail(request);
        return R.ok(pieEchartsResponse);
    }

    @Operation(summary = "查询大类支出明细详情列表")
    @PostMapping("/expense-details")
    public R<List<ExpenseDetailResponse>> getExpenseDetailList(@Validated @RequestBody ReportRequestVO request) {
        List<ExpenseDetailResponse> list = reportQueryService.getExpenseDetailList(request);
        return R.ok(list);
    }

    @Operation(summary = "饼图-折线图支出数据集图表")
    @PostMapping("/dataset")
    public R<ReportDatasetResponse> getReportDataset(@Validated @RequestBody ReportRequestVO request) {
        ReportDatasetResponse response = reportQueryService.getReportDataset(request);
        return R.ok(response);
    }

    @Operation(summary = "月度支出数据集柱状图")
    @PostMapping("/month-expense-bar")
    public R<LineEchartsResponse> getMonthExpenseBar(@Validated @RequestBody ReportRequestVO request) {
        LineEchartsResponse lineEchartsResponse = reportQueryService.getMonthExpenseBar(request);
        return R.ok(lineEchartsResponse);
    }
}
