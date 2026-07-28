package org.company.finance.tally.application.command;

import org.company.finance.category.application.query.model.CategorySnapshot;
import org.company.finance.category.domain.repository.QueryCategoryRepository;
import org.company.finance.tally.domain.repository.CommandRecordRepository;
import org.company.finance.tally.interfaces.rest.request.CreateExpenseRecordRequest;
import org.company.finance.tally.interfaces.rest.request.UpdateExpenseRecordRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinanceCommandServiceTest {

    @Mock
    private CommandRecordRepository commandRecordRepository;

    @Mock
    private QueryCategoryRepository queryCategoryRepository;

    private FinanceCommandService financeCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        financeCommandService = new FinanceCommandService(commandRecordRepository, queryCategoryRepository);
    }

    @Test
    void shouldSaveRecordWhenCategoryMatchesExpenseType() {
        CreateExpenseRecordRequest request = buildCreateRequest();
        when(queryCategoryRepository.findById(1)).thenReturn(buildCategorySnapshot(1));
        doNothing().when(commandRecordRepository).save(any());

        financeCommandService.saveRecord(request);

        verify(commandRecordRepository, times(1)).save(any());
    }

    @Test
    void shouldRejectRecordWhenCategoryTypeDoesNotMatch() {
        CreateExpenseRecordRequest request = buildCreateRequest();
        CategorySnapshot categorySnapshot = buildCategorySnapshot(2);
        when(queryCategoryRepository.findById(1)).thenReturn(categorySnapshot);

        assertThrows(IllegalArgumentException.class, () -> financeCommandService.saveRecord(request));
    }

    @Test
    void shouldUpdateRecordWhenRequestIsValid() {
        UpdateExpenseRecordRequest request = new UpdateExpenseRecordRequest();
        request.setId(1);
        request.setCategoryId(1);
        request.setExpenseAmount(new BigDecimal("21.00"));
        request.setExpenseDate("2026-01-02");
        request.setExpenseType(1);
        request.setRemark("晚饭");
        when(queryCategoryRepository.findById(1)).thenReturn(buildCategorySnapshot(1));
        doNothing().when(commandRecordRepository).update(any());

        financeCommandService.updateRecord(request);

        verify(commandRecordRepository, times(1)).update(any());
    }

    private CreateExpenseRecordRequest buildCreateRequest() {
        CreateExpenseRecordRequest request = new CreateExpenseRecordRequest();
        request.setCategoryId(1);
        request.setExpenseAmount(new BigDecimal("12.50"));
        request.setExpenseDate("2026-01-01");
        request.setExpenseType(1);
        request.setRemark("午饭");
        return request;
    }

    private CategorySnapshot buildCategorySnapshot(Integer expenseType) {
        CategorySnapshot categorySnapshot = new CategorySnapshot();
        categorySnapshot.setId(1);
        categorySnapshot.setCategoryName("餐饮");
        categorySnapshot.setParentId(0);
        categorySnapshot.setExpenseType(expenseType);
        categorySnapshot.setDelFlag(0);
        return categorySnapshot;
    }
}
