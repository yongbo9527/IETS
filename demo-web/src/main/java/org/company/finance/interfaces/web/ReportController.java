package org.company.finance.interfaces.web;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.command.service.ReportQueryService;
import org.company.finance.application.vo.calendar.IncomeExpenseDataVO;
import org.company.finance.application.vo.request.ReportRequestVO;
import org.company.finance.application.vo.response.BigCategoryExpenseResponse;
import org.company.finance.application.vo.response.ExpenseDetailResponse;
import org.company.finance.application.vo.response.LineEchartsResponse;
import org.company.finance.application.vo.response.ReportDatasetResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:05
 *
 */
@RestController
@RequestMapping("/report")
@Api(value = "报表")
@RequiredArgsConstructor
public class  ReportController {

    private final ReportQueryService reportQueryService;

    @ApiOperation(value = "按日统计支出与收入")
    @GetMapping("/queryDailyExpense")
    public R<Map<String, IncomeExpenseDataVO>> queryDailyExpense(@RequestParam String searchDate) {
        Map<String, IncomeExpenseDataVO> map = reportQueryService.queryDailyExpense(searchDate);
        return R.ok(map);
    }

    @ApiOperation(value = "折线图：按月支出与收入")
    @PostMapping("/queryMonthExpense")
    public R<LineEchartsResponse> queryMonthExpense(@RequestBody ReportRequestVO vo) {
        LineEchartsResponse lineEchartsResponse = reportQueryService.queryMonthExpense(vo);
        return R.ok(lineEchartsResponse);
    }

    @ApiOperation(value = "饼图：大类支出")
    @PostMapping("/queryBigCategoryExpense")
    public R<List<BigCategoryExpenseResponse>> queryBigCategoryExpense(@RequestBody ReportRequestVO vo) {
        List<BigCategoryExpenseResponse> pieEchartsResponse = reportQueryService.queryBigCategoryExpense(vo);

        return R.ok(pieEchartsResponse);
    }

    @ApiOperation(value = "查询大类支出明细")
    @PostMapping("/queryBigCategoryExpenseDetail")
    public R<List<BigCategoryExpenseResponse>> querySmallCategoryExpenseDetail(@RequestBody ReportRequestVO vo) {
        List<BigCategoryExpenseResponse> pieEchartsResponse = reportQueryService.querySmallCategoryExpenseDetail(vo);
        return R.ok(pieEchartsResponse);
    }

    @ApiOperation(value = "查询大类支出明细详情列表")
    @PostMapping("/queryExpenseDetailList")
    public R queryExpenseDetailList(@RequestBody ReportRequestVO vo) {
        List<ExpenseDetailResponse> list = reportQueryService.queryExpenseDetailList(vo);
        return R.ok(list);
    }

    @ApiOperation(value = "饼图-折线图支出数据集图表")
    @PostMapping("/queryReportDataset")
    public R queryReportDataset(@RequestBody ReportRequestVO vo) {
        ReportDatasetResponse response = reportQueryService.queryReportDataset(vo);
        return R.ok(response);
    }

    @ApiOperation(value = "月度支出数据集柱状图")
    @PostMapping("/queryMonthExpenseBar")
    public R queryMonthExpenseBar(@RequestBody ReportRequestVO vo) {
        LineEchartsResponse lineEchartsResponse = reportQueryService.queryMonthExpenseBar(vo);
        return R.ok(lineEchartsResponse);
    }

}
