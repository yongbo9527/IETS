package org.company.finance.category.application.command;

import lombok.RequiredArgsConstructor;
import org.company.finance.category.domain.model.Category;
import org.company.finance.category.domain.repository.CommandCategoryRepository;
import org.company.finance.category.interfaces.rest.request.CreateCategoryRequest;
import org.company.finance.category.interfaces.rest.request.UpdateCategoryRequest;
import org.company.finance.tally.domain.model.ExpenseType;
import org.springframework.stereotype.Service;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 18:13
 *
 */
@Service
@RequiredArgsConstructor
public class CategoryCommandService {
    private final CommandCategoryRepository commandCategoryRepository;

    public void addCategory(CreateCategoryRequest request) {
        Category category = Category.restore(null, request.getCategoryName(), normalizeParentId(request.getParentId()),
                ExpenseType.ofCode(request.getExpenseType()), false);
        commandCategoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        commandCategoryRepository.updateById(id);
    }

    public void updateCategory(UpdateCategoryRequest request) {
        Category category = Category.restore(request.getId(), request.getCategoryName(), normalizeParentId(request.getParentId()),
                ExpenseType.ofCode(request.getExpenseType()), false);
        commandCategoryRepository.update(category);
    }

    private Integer normalizeParentId(Integer parentId) {
        return parentId == null ? 0 : parentId;
    }
}
