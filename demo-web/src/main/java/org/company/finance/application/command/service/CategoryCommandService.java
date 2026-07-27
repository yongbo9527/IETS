package org.company.finance.application.command.service;

import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.request.CreateCategoryRequest;
import org.company.finance.application.vo.request.UpdateCategoryRequest;
import org.company.finance.domain.model.Category;
import org.company.finance.domain.model.ExpenseType;
import org.company.finance.domain.repository.CommandCategoryRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
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
        Category.restore(null, request.getCategoryName(), normalizeParentId(request.getParentId()),
                ExpenseType.ofCode(request.getExpenseType()), false);
        commandCategoryRepository.save(toEntity(request));
    }

    public void deleteCategory(Integer id) {
        commandCategoryRepository.updateById(id);
    }

    public void updateCategory(UpdateCategoryRequest request) {
        Category.restore(request.getId(), request.getCategoryName(), normalizeParentId(request.getParentId()),
                ExpenseType.ofCode(request.getExpenseType()), false);
        commandCategoryRepository.update(toEntity(request));
    }

    private Integer normalizeParentId(Integer parentId) {
        return parentId == null ? 0 : parentId;
    }

    private CategoryEntity toEntity(CreateCategoryRequest request) {
        CategoryEntity entity = new CategoryEntity();
        entity.setCategoryName(request.getCategoryName());
        entity.setCategoryIcon(request.getCategoryIcon());
        entity.setParentId(normalizeParentId(request.getParentId()));
        entity.setExpenseType(request.getExpenseType());
        entity.setRemark(request.getRemark());
        return entity;
    }

    private CategoryEntity toEntity(UpdateCategoryRequest request) {
        CategoryEntity entity = toEntity((CreateCategoryRequest) request);
        entity.setId(request.getId());
        return entity;
    }
}
