package org.company.finance.category.application.command;

import org.company.finance.category.domain.repository.CommandCategoryRepository;
import org.company.finance.category.interfaces.rest.request.CreateCategoryRequest;
import org.company.finance.category.interfaces.rest.request.UpdateCategoryRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CategoryCommandServiceTest {

    @Mock
    private CommandCategoryRepository commandCategoryRepository;

    private CategoryCommandService categoryCommandService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryCommandService = new CategoryCommandService(commandCategoryRepository);
    }

    @Test
    void shouldSaveCategoryWhenRequestIsValid() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setCategoryName("餐饮");
        request.setParentId(0);
        request.setExpenseType(1);

        doNothing().when(commandCategoryRepository).save(any());

        categoryCommandService.addCategory(request);

        verify(commandCategoryRepository, times(1)).save(any());
    }

    @Test
    void shouldRejectInvalidCategoryType() {
        CreateCategoryRequest request = new CreateCategoryRequest();
        request.setCategoryName("餐饮");
        request.setParentId(0);
        request.setExpenseType(3);

        assertThrows(IllegalArgumentException.class, () -> categoryCommandService.addCategory(request));
    }

    @Test
    void shouldUpdateCategoryWhenRequestIsValid() {
        UpdateCategoryRequest request = new UpdateCategoryRequest();
        request.setId(1);
        request.setCategoryName("早餐");
        request.setParentId(1);
        request.setExpenseType(1);

        doNothing().when(commandCategoryRepository).update(any());

        categoryCommandService.updateCategory(request);

        verify(commandCategoryRepository, times(1)).update(any());
    }
}
