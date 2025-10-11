package org.company.finance.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.report.ReportDatasetVO;
import org.company.finance.application.vo.request.ReportRequestVO;
import org.company.finance.application.vo.response.BigCategoryExpenseResponse;
import org.company.finance.application.vo.response.ExpenseDetailResponse;
import org.company.finance.application.vo.response.ReportDataResponse;
import org.company.finance.domain.repository.QueryReportRepository;
import org.company.finance.infrastructure.persistence.mapper.ReportMapper;
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
    public List<ReportDataResponse> selectExpenseByDate(ReportRequestVO requestVO) {
        List<ReportDataResponse> reportDataResponseList = reportMapper.selectExpenseByDate(requestVO);
        return reportDataResponseList;
    }

    @Override
    public List<BigCategoryExpenseResponse> selectPieExpenseByCategoryId(ReportRequestVO vo) {
        List<BigCategoryExpenseResponse> bigCategoryExpenseResponses = reportMapper.selectPieExpenseByCategoryId(vo);
        return bigCategoryExpenseResponses;
    }

    @Override
    public List<BigCategoryExpenseResponse> selectBigCategoryExpenseDetail(ReportRequestVO vo) {
        return reportMapper.selectBigCategoryExpenseDetail(vo);
    }

    @Override
    public List<ReportDataResponse> selectExpenseByMonthDate(ReportRequestVO vo) {
        return reportMapper.selectExpenseByMonthDate(vo);
    }

    @Override
    public List<ExpenseDetailResponse> selectExpenseDetailList(ReportRequestVO vo) {
        return reportMapper.selectExpenseDetailList(vo);
    }

    @Override
    public List<ReportDatasetVO> selectDatasetVO(ReportRequestVO vo) {
        return reportMapper.selectDatasetVO(vo);
    }
}
