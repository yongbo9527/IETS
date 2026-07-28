package org.company.finance.category.domain.repository;

import org.company.finance.category.domain.model.Category;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 18:14
 *
 */
public interface CommandCategoryRepository {
    void save(Category category);

    void updateById(Integer id);

    void update(Category category);
}
