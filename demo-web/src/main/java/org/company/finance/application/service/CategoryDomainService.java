package org.company.finance.application.service;

import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.domain.repository.CategoryRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-10 18:10
 *
 */
@Service
@RequiredArgsConstructor
public class CategoryDomainService {

    private final CategoryRepository categoryRepository;

    public List<CategoryEntity> findActiveCategories() {
        return categoryRepository.findActiveOrderByAsc();
    }

    public List<DataCategoryVO> findDataCategories(QueryBalanceVO vo) {
        return categoryRepository.findDataCategory(vo);
    }

    public List<CategoryEntity> findByIds(Set<Integer> parentIds) {
        return categoryRepository.findByCategoryIds(parentIds);
    }
}
