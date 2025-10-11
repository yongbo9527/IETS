package org.company.finance.application.query.service;

import lombok.RequiredArgsConstructor;
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

    public void addCategory(CategoryEntity entity) {
        commandCategoryRepository.addCategory(entity);
    }

    public void deleteCategory(Integer id) {
        commandCategoryRepository.updateById(id);
    }

    public void updateCategory(CategoryEntity entity) {
        commandCategoryRepository.updateCategory(entity);
    }
}
