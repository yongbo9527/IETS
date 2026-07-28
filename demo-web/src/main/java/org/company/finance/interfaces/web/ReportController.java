package org.company.finance.interfaces.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.query.service.ReportQueryService;
import org.company.finance.application.vo.calendar.IncomeExpenseDataVO;
import org.company.finance.application.vo.request.ReportRequestVO;
import org.company.finance.application.vo.response.BigCategoryExpenseResponse;
import org.company.finance.application.vo.response.ExpenseDetailResponse;
import org.company.finance.application.vo.response.LineEchartsResponse;
import org.company.finance.application.vo.response.ReportDatasetResponse;
import org.company.finance.common.util.R;
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
@RequestMapping("/report")
@Tag(name = "报表")
@RequiredArgsConstructor
@Validated
public class  ReportController {

    private final ReportQueryService reportQueryService;

    @Operation(summary = "按日统计支出与收入")
    @GetMapping("/queryDailyExpense")
    public R<Map<String, IncomeExpenseDataVO>> queryDailyExpense(@NotBlank @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式应为 yyyy-MM-dd") @RequestParam String searchDate) {
        Map<String, IncomeExpenseDataVO> map = reportQueryService.queryDailyExpense(searchDate);
        return R.ok(map);
    }

    @Operation(summary = "折线图：按月支出与收入")
    @PostMapping("/queryMonthExpense")
    public R<LineEchartsResponse> queryMonthExpense(@Validated @RequestBody ReportRequestVO vo) {
        LineEchartsResponse lineEchartsResponse = reportQueryService.queryMonthExpense(vo);
        return R.ok(lineEchartsResponse);
    }

    @Operation(summary = "饼图：大类支出")
    @PostMapping("/queryBigCategoryExpense")
    public R<List<BigCategoryExpenseResponse>> queryBigCategoryExpense(@Validated @RequestBody ReportRequestVO vo) {
        List<BigCategoryExpenseResponse> pieEchartsResponse = reportQueryService.queryBigCategoryExpense(vo);

        return R.ok(pieEchartsResponse);
    }

    @Operation(summary = "查询大类支出明细")
    @PostMapping("/queryBigCategoryExpenseDetail")
    public R<List<BigCategoryExpenseResponse>> querySmallCategoryExpenseDetail(@Validated @RequestBody ReportRequestVO vo) {
        List<BigCategoryExpenseResponse> pieEchartsResponse = reportQueryService.querySmallCategoryExpenseDetail(vo);
        return R.ok(pieEchartsResponse);
    }

    @Operation(summary = "查询大类支出明细详情列表")
    @PostMapping("/queryExpenseDetailList")
    public R queryExpenseDetailList(@Validated @RequestBody ReportRequestVO vo) {
        List<ExpenseDetailResponse> list = reportQueryService.queryExpenseDetailList(vo);
        return R.ok(list);
    }

    @Operation(summary = "饼图-折线图支出数据集图表")
    @PostMapping("/queryReportDataset")
    public R queryReportDataset(@Validated @RequestBody ReportRequestVO vo) {
        ReportDatasetResponse response = reportQueryService.queryReportDataset(vo);
        return R.ok(response);
    }

    @Operation(summary = "月度支出数据集柱状图")
    @PostMapping("/queryMonthExpenseBar")
    public R queryMonthExpenseBar(@Validated @RequestBody ReportRequestVO vo) {
        LineEchartsResponse lineEchartsResponse = reportQueryService.queryMonthExpenseBar(vo);
        return R.ok(lineEchartsResponse);
    }

}
