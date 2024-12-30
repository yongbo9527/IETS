package org.oneself.balance.demo.service;

import com.baomidou.mybatisplus.extension.api.R;
import org.oneself.balance.demo.vo.request.ReportRequestVO;
import org.oneself.balance.demo.vo.response.BigCategoryExpenseResponse;
import org.oneself.balance.demo.vo.response.LineEchartsResponse;
import org.oneself.balance.demo.vo.response.ReportDataResponse;

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
    BigCategoryExpenseResponse querySmallCategoryExpense(ReportRequestVO vo);
}
