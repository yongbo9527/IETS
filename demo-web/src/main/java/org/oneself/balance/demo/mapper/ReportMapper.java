package org.oneself.balance.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.oneself.balance.demo.vo.report.ReportDatasetVO;
import org.oneself.balance.demo.vo.request.ReportRequestVO;
import org.oneself.balance.demo.vo.response.BigCategoryExpenseResponse;
import org.oneself.balance.demo.vo.response.ReportDataResponse;

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
     * @param bigCategoryId
     * @return
     */
    List<BigCategoryExpenseResponse> selectBigCategoryExpenseDetail(Integer bigCategoryId);
}
