package org.company.finance.application.query.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.request.CategoryRequestVO;
import org.company.finance.application.vo.response.CategoryResponse;
import org.company.finance.application.vo.response.CategoryTreeNodeResponse;
import org.company.finance.domain.repository.QueryCategoryRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Page<CategoryResponse> queryCategory(CategoryRequestVO requestVO) {
        Page<CategoryEntity> entityPage = queryCategoryRepository.queryCategoryWithPage(requestVO);
        Page<CategoryResponse> responsePage = new Page<>(entityPage.getCurrent(), entityPage.getSize());
        responsePage.setTotal(entityPage.getTotal());
        responsePage.setRecords(entityPage.getRecords().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList()));
        return responsePage;
    }

    public List<CategoryResponse> queryAllCategory(CategoryRequestVO requestVO) {
        return queryCategoryRepository.queryAllCategory(requestVO).stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    public List<CategoryTreeNodeResponse> queryTreeCategoryList() {
        List<CategoryEntity> categoryEntities = queryCategoryRepository.findActiveOrderByAsc();
        List<CategoryEntity> parentCategory = categoryEntities.stream().filter(item -> item.getParentId() == 0).collect(Collectors.toList());
        List<CategoryEntity> childCategory = categoryEntities.stream().filter(item -> item.getParentId() != 0).collect(Collectors.toList());
        return parentCategory.stream().map(item -> buildTreeNode(item, childCategory)).collect(Collectors.toList());
    }

    private CategoryTreeNodeResponse buildTreeNode(CategoryEntity parent, List<CategoryEntity> children) {
        CategoryTreeNodeResponse parentNode = toTreeNode(parent);
        List<CategoryTreeNodeResponse> childNodes = children.stream()
                .filter(childItem -> childItem.getParentId().equals(parent.getId()))
                .map(this::toTreeNode)
                .collect(Collectors.toList());
        parentNode.setChildren(childNodes);
        return parentNode;
    }

    private CategoryResponse toCategoryResponse(CategoryEntity entity) {
        CategoryResponse response = new CategoryResponse();
        response.setId(entity.getId());
        response.setCategoryName(entity.getCategoryName());
        response.setCategoryIcon(entity.getCategoryIcon());
        response.setParentId(entity.getParentId());
        response.setExpenseType(entity.getExpenseType());
        response.setRemark(entity.getRemark());
        return response;
    }

    private CategoryTreeNodeResponse toTreeNode(CategoryEntity entity) {
        CategoryTreeNodeResponse response = new CategoryTreeNodeResponse();
        response.setId(entity.getId());
        response.setCategoryName(entity.getCategoryName());
        response.setCategoryIcon(entity.getCategoryIcon());
        response.setParentId(entity.getParentId());
        response.setExpenseType(entity.getExpenseType());
        response.setRemark(entity.getRemark());
        response.setChildren(new ArrayList<>());
        return response;
    }
}
