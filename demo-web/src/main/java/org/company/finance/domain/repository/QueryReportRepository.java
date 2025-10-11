package org.company.finance.domain.repository;

import org.company.finance.application.vo.report.ReportDatasetVO;
import org.company.finance.application.vo.request.ReportRequestVO;
import org.company.finance.application.vo.response.BigCategoryExpenseResponse;
import org.company.finance.application.vo.response.ExpenseDetailResponse;
import org.company.finance.application.vo.response.ReportDataResponse;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-09 18:11
 *
 */
public interface QueryReportRepository {

    List<ReportDataResponse> selectExpenseByDate(ReportRequestVO requestVO);

    List<BigCategoryExpenseResponse> selectPieExpenseByCategoryId(ReportRequestVO vo);

    List<BigCategoryExpenseResponse> selectBigCategoryExpenseDetail(ReportRequestVO vo);

    List<ReportDataResponse> selectExpenseByMonthDate(ReportRequestVO vo);

    List<ExpenseDetailResponse> selectExpenseDetailList(ReportRequestVO vo);

    List<ReportDatasetVO> selectDatasetVO(ReportRequestVO vo);
}
