package org.company.finance.report.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.company.finance.report.application.query.model.BigCategoryExpenseQueryModel;
import org.company.finance.report.application.query.model.ExpenseDetailQueryModel;
import org.company.finance.report.application.query.model.ReportDataQueryModel;
import org.company.finance.report.application.query.model.ReportDatasetQueryModel;
import org.company.finance.report.domain.repository.QueryReportRepository;
import org.company.finance.report.interfaces.rest.request.ReportRequestVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 17:41
 *
 */
@Repository
@RequiredArgsConstructor
public class QueryReportRepositoryImpl implements QueryReportRepository {

    private final ReportMapper reportMapper;

    @Override
    public List<ReportDataQueryModel> findExpenseByDate(ReportRequestVO request) {
        return reportMapper.selectExpenseByDate(request);
    }

    @Override
    public List<BigCategoryExpenseQueryModel> findBigCategoryExpense(ReportRequestVO request) {
        return reportMapper.selectBigCategoryExpense(request);
    }

    @Override
    public List<BigCategoryExpenseQueryModel> findBigCategoryExpenseDetail(ReportRequestVO request) {
        return reportMapper.selectBigCategoryExpenseDetail(request);
    }

    @Override
    public List<ReportDataQueryModel> findExpenseByMonth(ReportRequestVO request) {
        return reportMapper.selectExpenseByMonth(request);
    }

    @Override
    public List<ReportDataQueryModel> findIncomeExpenseSummary(ReportRequestVO request) {
        return reportMapper.selectIncomeExpenseSummary(request);
    }

    @Override
    public List<ExpenseDetailQueryModel> findExpenseDetailList(ReportRequestVO request) {
        return reportMapper.selectExpenseDetailList(request);
    }

    @Override
    public List<ReportDatasetQueryModel> findDataset(ReportRequestVO request) {
        return reportMapper.selectDataset(request);
    }
}
