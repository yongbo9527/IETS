package org.company.finance.domain.repository;

import org.company.finance.infrastructure.persistence.entity.CategoryEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 18:14
 *
 */
public interface CommandCategoryRepository {
    void addCategory(CategoryEntity entity);

    void updateById(Integer id);

    void updateCategory(CategoryEntity entity);
}
