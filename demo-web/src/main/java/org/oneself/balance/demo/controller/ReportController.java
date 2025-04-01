package org.oneself.balance.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oneself.balance.demo.service.ReportService;
import org.oneself.balance.demo.vo.request.ReportRequestVO;
import org.oneself.balance.demo.vo.response.BigCategoryExpenseResponse;
import org.oneself.balance.demo.vo.response.LineEchartsResponse;
import org.oneself.balance.demo.vo.response.ReportDatasetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:05
 *
 */
@RestController
@RequestMapping("/report")
@Api(value = "报表")
public class  ReportController {

    @Autowired
    private ReportService reportService;

    @ApiOperation(value = "按日统计支出与收入")
    @GetMapping("/queryDailyExpense")
    public R queryDailyExpense(@RequestParam String searchDate) {

        R result = reportService.queryDailyExpense(searchDate);
        return R.ok(result);
    }

    @ApiOperation(value = "折线图：按月支出与收入")
    @PostMapping("/queryMonthExpense")
    public R queryMonthExpense(@RequestBody ReportRequestVO vo) {
        LineEchartsResponse lineEchartsResponse = reportService.queryMonthExpense(vo);
        return R.ok(lineEchartsResponse);
    }

    @ApiOperation(value = "饼图：大类支出")
    @PostMapping("/queryBigCategoryExpense")
    public R queryBigCategoryExpense(@RequestBody ReportRequestVO vo) {
        List<BigCategoryExpenseResponse> pieEchartsResponse = reportService.queryBigCategoryExpense(vo);

        return R.ok(pieEchartsResponse);
    }

    @ApiOperation(value = "查询大类支出明细")
    @PostMapping("/queryBigCategoryExpenseDetail")
    public R querySmallCategoryExpenseDetail(@RequestBody ReportRequestVO vo) {

        List<BigCategoryExpenseResponse> pieEchartsResponse = reportService.querySmallCategoryExpenseDetail(vo);
        return R.ok(pieEchartsResponse);
    }

    @ApiOperation(value = "饼图-折线图支出数据集图表")
    @PostMapping("/queryReportDataset")
    public R queryReportDataset(@RequestBody ReportRequestVO vo) {
        ReportDatasetResponse response = reportService.queryReportDataset(vo);
        return R.ok(response);
    }

    @ApiOperation(value = "月度支出数据集柱状图")
    @PostMapping("/queryMonthExpenseBar")
    public R queryMonthExpenseBar(@RequestBody ReportRequestVO vo) {
        LineEchartsResponse lineEchartsResponse = reportService.queryMonthExpenseBar(vo);
        return R.ok(lineEchartsResponse);
    }

}
