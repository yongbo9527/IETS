package org.company.finance.infrastructure.persistence.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.company.finance.application.vo.report.ReportDatasetVO;
import org.company.finance.application.vo.request.ReportRequestVO;
import org.company.finance.application.vo.response.BigCategoryExpenseResponse;
import org.company.finance.application.vo.response.ExpenseDetailResponse;
import org.company.finance.application.vo.response.ReportDataResponse;

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
    List<ReportDataResponse> selectExpenseByDate(ReportRequestVO vo);

    /**
     * 饼图根据开始和结束日期查询支出与收入日数据
     * @param vo
     * @return
     */
    List<BigCategoryExpenseResponse> selectPieExpenseByCategoryId(ReportRequestVO vo);

    /**
     * 支出数据集原始数据
     * @param vo
     * @return
     */
    List<ReportDatasetVO> selectDatasetVO(ReportRequestVO vo);

    /**
     * 查询各个月度支出数据
     * @param vo
     * @return
     */
    List<ReportDataResponse> selectExpenseByMonthDate(ReportRequestVO vo);

    /**
     * 查询大类支出明细
     * @param vo
     * @return
     */
    List<BigCategoryExpenseResponse> selectBigCategoryExpenseDetail(ReportRequestVO vo);

    /**
     * 查询大类支出明细详情列表
     * @param vo
     * @return
     */
    List<ExpenseDetailResponse> selectExpenseDetailList(ReportRequestVO vo);
}
