package org.company.finance.domain.repository;

import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;

import java.util.List;
import java.util.Set;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-10 18:18
 *
 */
public interface CategoryRepository {
    List<CategoryEntity> findActiveOrderByAsc();

    List<DataCategoryVO> findDataCategory(QueryBalanceVO vo);

    List<CategoryEntity> findByCategoryIds(Set<Integer> parentIds);
}
