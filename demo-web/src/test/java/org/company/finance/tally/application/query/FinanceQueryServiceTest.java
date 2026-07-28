package org.company.finance.tally.application.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.company.finance.category.domain.repository.QueryCategoryRepository;
import org.company.finance.tally.application.query.model.ExpenseRecordQueryModel;
import org.company.finance.tally.domain.repository.QueryRecordRepository;
import org.company.finance.tally.interfaces.rest.request.QueryBalanceVO;
import org.company.finance.tally.interfaces.rest.response.ExpenseRecordResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

class FinanceQueryServiceTest {

    @Mock
    private QueryRecordRepository queryRecordRepository;

    @Mock
    private QueryCategoryRepository queryCategoryRepository;

    private FinanceQueryService financeQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        financeQueryService = new FinanceQueryService(queryRecordRepository, queryCategoryRepository);
    }

    @Test
    void shouldMapQueryModelToResponsePage() {
        ExpenseRecordQueryModel queryModel = new ExpenseRecordQueryModel();
        queryModel.setId(1);
        queryModel.setCategoryId(2);
        queryModel.setCategoryName("午餐");
        queryModel.setExpenseAmount(new BigDecimal("25.50"));
        queryModel.setExpenseDate("2026-01-01");
        queryModel.setExpenseType(1);
        queryModel.setRemark("工作餐");

        Page<ExpenseRecordQueryModel> queryPage = new Page<>(1, 10);
        queryPage.setTotal(1);
        queryPage.setRecords(Collections.singletonList(queryModel));
        when(queryRecordRepository.findByUserIdAndDateRange(isNull(), any(QueryBalanceVO.class))).thenReturn(queryPage);

        Page<ExpenseRecordResponse> responsePage = financeQueryService.listDailyRecords(new QueryBalanceVO());

        assertEquals(1, responsePage.getTotal());
        assertEquals(1, responsePage.getRecords().size());
        assertEquals("午餐", responsePage.getRecords().get(0).getCategoryName());
        assertEquals(new BigDecimal("25.50"), responsePage.getRecords().get(0).getExpenseAmount());
    }
}
