package org.company.finance.report.domain.repository;

import org.company.finance.report.application.query.model.BigCategoryExpenseQueryModel;
import org.company.finance.report.application.query.model.ExpenseDetailQueryModel;
import org.company.finance.report.application.query.model.ReportDataQueryModel;
import org.company.finance.report.application.query.model.ReportDatasetQueryModel;
import org.company.finance.report.interfaces.rest.request.ReportRequestVO;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-09 18:11
 *
 */
public interface QueryReportRepository {

    List<ReportDataQueryModel> findExpenseByDate(ReportRequestVO request);

    List<BigCategoryExpenseQueryModel> findBigCategoryExpense(ReportRequestVO request);

    List<BigCategoryExpenseQueryModel> findBigCategoryExpenseDetail(ReportRequestVO request);

    List<ReportDataQueryModel> findExpenseByMonth(ReportRequestVO request);

    List<ReportDataQueryModel> findIncomeExpenseSummary(ReportRequestVO request);

    List<ExpenseDetailQueryModel> findExpenseDetailList(ReportRequestVO request);

    List<ReportDatasetQueryModel> findDataset(ReportRequestVO request);
}
