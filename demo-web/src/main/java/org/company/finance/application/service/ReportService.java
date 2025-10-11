package org.company.finance.application.service;

import com.baomidou.mybatisplus.extension.api.R;
import org.company.finance.application.vo.request.ReportRequestVO;
import org.company.finance.application.vo.response.BigCategoryExpenseResponse;
import org.company.finance.application.vo.response.ExpenseDetailResponse;
import org.company.finance.application.vo.response.LineEchartsResponse;
import org.company.finance.application.vo.response.ReportDatasetResponse;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:19
 *
 */
public interface ReportService {
    /**
     * 按日统计支出与收入，用于显示日历
     * @param searchDate
     * @return
     */
    R queryDailyExpense(String searchDate);

    /**
     * 折线图：按月支出与收入
     * @param vo
     * @return
     */
    LineEchartsResponse queryMonthExpense(ReportRequestVO vo);

    /**
     * 饼图：大类支出
     * @param vo
     * @return
     */
    List<BigCategoryExpenseResponse> queryBigCategoryExpense(ReportRequestVO vo);

    /**
     * 饼图：小类支出
     * @param vo
     * @return
     */
    List<BigCategoryExpenseResponse> querySmallCategoryExpenseDetail(ReportRequestVO vo);

    /**
     * 饼图-折线图支出数据集图表
     * @param vo
     * @return
     */
    ReportDatasetResponse queryReportDataset(ReportRequestVO vo);

    /**
     * 折线图：按月支出柱状图
     * @param vo
     * @return
     */
    LineEchartsResponse queryMonthExpenseBar(ReportRequestVO vo);

    /**
     * 查询大类支出明细详情列表
     * @param vo
     * @return
     */
    List<ExpenseDetailResponse> queryExpenseDetailList(ReportRequestVO vo);
}
