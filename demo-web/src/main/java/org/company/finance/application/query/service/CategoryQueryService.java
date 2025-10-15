package org.company.finance.application.query.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.request.CategoryRequestVO;
import org.company.finance.domain.repository.QueryCategoryRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 17:42
 *
 */
@Service
@RequiredArgsConstructor
public class CategoryQueryService {

    private final QueryCategoryRepository queryCategoryRepository;

    public Page<CategoryEntity> queryCategory(CategoryRequestVO requestVO) {
        Page<CategoryEntity> entityPage = queryCategoryRepository.queryCategoryWithPage(requestVO);
        return entityPage;
    }

    public List<CategoryEntity> queryAllCategory(CategoryRequestVO requestVO) {
        List<CategoryEntity> entityPage = queryCategoryRepository.queryAllCategory(requestVO);
        return entityPage;
    }

    public List<CategoryEntity> queryTreeCategoryList() {
        List<CategoryEntity> categoryEntities = queryCategoryRepository.findActiveOrderByAsc();
        List<CategoryEntity> parentCategory = categoryEntities.stream().filter(item -> item.getParentId() == 0).collect(Collectors.toList());
        List<CategoryEntity> childCategory = categoryEntities.stream().filter(item -> item.getParentId() != 0).collect(Collectors.toList());
        List<CategoryEntity> resultCategory = parentCategory.stream().map(item -> {
            item.setList(childCategory.stream().filter(childItem -> childItem.getParentId().equals(item.getId())).collect(Collectors.toList()));
            return item;
        }).collect(Collectors.toList());


        return resultCategory;
    }
}
