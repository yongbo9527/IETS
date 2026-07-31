package org.company.finance.report.application.query;

import org.company.finance.report.application.query.model.BigCategoryExpenseQueryModel;
import org.company.finance.report.application.query.model.ReportDataQueryModel;
import org.company.finance.report.domain.repository.QueryReportRepository;
import org.company.finance.report.interfaces.rest.response.BigCategoryExpenseResponse;
import org.company.finance.report.interfaces.rest.response.IncomeExpenseDataVO;
import org.company.finance.report.interfaces.rest.response.IncomeExpenseSummaryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReportQueryServiceTest {

    @Mock
    private QueryReportRepository queryReportRepository;

    private ReportQueryService reportQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reportQueryService = new ReportQueryService(queryReportRepository);
    }

    @Test
    void shouldAggregateDailyExpenseForMonth() {
        when(queryReportRepository.findExpenseByDate(any())).thenReturn(Arrays.asList(
                buildReportData("2026-01-01", "100.00", 2),
                buildReportData("2026-01-01", "20.00", 1),
                buildReportData("2026-01-03", "30.00", 1)
        ));

        Map<String, IncomeExpenseDataVO> result = reportQueryService.getDailyExpense("2026-01");

        assertEquals(31, result.size());
        assertNotNull(result.get("2026-01-02"));
        assertEquals(new BigDecimal("100.00"), result.get("2026-01-01").getIncome());
        assertEquals(new BigDecimal("20.00"), result.get("2026-01-01").getExpense());
        assertEquals(BigDecimal.ZERO, result.get("2026-01-02").getIncome());
        assertEquals(BigDecimal.ZERO, result.get("2026-01-02").getExpense());
        assertEquals(new BigDecimal("30.00"), result.get("2026-01-03").getExpense());
    }

    @Test
    void shouldCalculateExpensePercentForBigCategoryExpense() {
        when(queryReportRepository.findBigCategoryExpense(any())).thenReturn(Arrays.asList(
                buildBigCategoryExpense(1, "餐饮", "30.00"),
                buildBigCategoryExpense(2, "交通", "70.00"),
                buildBigCategoryExpense(3, "其他", "0.00")
        ));

        List<BigCategoryExpenseResponse> result = reportQueryService.getBigCategoryExpense(new org.company.finance.report.interfaces.rest.request.ReportRequestVO());

        assertEquals(2, result.size());
        assertEquals(new BigDecimal("30.00"), result.get(0).getExpensePercent());
        assertEquals(new BigDecimal("70.00"), result.get(1).getExpensePercent());
    }

    @Test
    void shouldSummarizeIncomeExpenseTotals() {
        when(queryReportRepository.findIncomeExpenseSummary(any())).thenReturn(Arrays.asList(
                buildReportData("2026-01", "100.00", 2),
                buildReportData("2026-01", "20.00", 1),
                buildReportData("2026-02", "50.00", 2),
                buildReportData("2026-02", "10.00", 1)
        ));

        IncomeExpenseSummaryResponse result = reportQueryService.getIncomeExpenseSummary(new org.company.finance.report.interfaces.rest.request.ReportRequestVO());

        assertEquals(new BigDecimal("30.00"), result.getTotalExpense());
        assertEquals(new BigDecimal("150.00"), result.getTotalIncome());
        assertEquals(new BigDecimal("120.00"), result.getNetIncome());
    }

    private ReportDataQueryModel buildReportData(String date, String amount, Integer expenseType) {
        ReportDataQueryModel model = new ReportDataQueryModel();
        model.setDate(date);
        model.setAmount(new BigDecimal(amount));
        model.setExpenseType(expenseType);
        return model;
    }

    private BigCategoryExpenseQueryModel buildBigCategoryExpense(Integer parentId, String parentCategory, String amount) {
        BigCategoryExpenseQueryModel model = new BigCategoryExpenseQueryModel();
        model.setParentId(parentId);
        model.setParentCategory(parentCategory);
        model.setExpenseTotal(new BigDecimal(amount));
        model.setExpenseCount(1);
        return model;
    }
}
