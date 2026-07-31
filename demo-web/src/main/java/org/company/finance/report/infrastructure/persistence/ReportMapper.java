package org.company.finance.report.infrastructure.persistence;

import org.apache.ibatis.annotations.Mapper;
import org.company.finance.report.application.query.model.BigCategoryExpenseQueryModel;
import org.company.finance.report.application.query.model.ExpenseDetailQueryModel;
import org.company.finance.report.application.query.model.ReportDataQueryModel;
import org.company.finance.report.application.query.model.ReportDatasetQueryModel;
import org.company.finance.report.interfaces.rest.request.ReportRequestVO;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:19
 *
 */
@Mapper
public interface ReportMapper {
    /**
     * 根据开始和结束日期查询支出与收入日数据
     * @param vo
     * @return
     */
    List<ReportDataQueryModel> selectExpenseByDate(ReportRequestVO request);

    /**
     * 饼图根据开始和结束日期查询支出与收入日数据
     * @param vo
     * @return
     */
    List<BigCategoryExpenseQueryModel> selectBigCategoryExpense(ReportRequestVO request);

    /**
     * 支出数据集原始数据
     * @param vo
     * @return
     */
    List<ReportDatasetQueryModel> selectDataset(ReportRequestVO request);

    /**
     * 查询各个月度支出数据
     * @param vo
     * @return
     */
    List<ReportDataQueryModel> selectExpenseByMonth(ReportRequestVO request);

    List<ReportDataQueryModel> selectIncomeExpenseSummary(ReportRequestVO request);

    /**
     * 查询大类支出明细
     * @param vo
     * @return
     */
    List<BigCategoryExpenseQueryModel> selectBigCategoryExpenseDetail(ReportRequestVO request);

    /**
     * 查询大类支出明细详情列表
     * @param vo
     * @return
     */
    List<ExpenseDetailQueryModel> selectExpenseDetailList(ReportRequestVO request);
}
