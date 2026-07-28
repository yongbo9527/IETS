package org.company.finance.category.application.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.company.finance.category.domain.repository.QueryCategoryRepository;
import org.company.finance.category.interfaces.rest.request.CategoryRequestVO;
import org.company.finance.category.interfaces.rest.response.CategoryResponse;
import org.company.finance.category.interfaces.rest.response.CategoryTreeNodeResponse;
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

    public Page<CategoryResponse> queryCategory(CategoryRequestVO requestVO) {
        return queryCategoryRepository.queryCategoryWithPage(requestVO);
    }

    public List<CategoryResponse> queryAllCategory(CategoryRequestVO requestVO) {
        return queryCategoryRepository.queryAllCategory(requestVO);
    }

    public List<CategoryTreeNodeResponse> queryTreeCategoryList() {
        List<CategoryResponse> categoryEntities = queryCategoryRepository.findActiveOrderByAsc();
        List<CategoryResponse> parentCategory = categoryEntities.stream().filter(item -> item.getParentId() == 0).collect(Collectors.toList());
        List<CategoryResponse> childCategory = categoryEntities.stream().filter(item -> item.getParentId() != 0).collect(Collectors.toList());
        return parentCategory.stream().map(item -> buildTreeNode(item, childCategory)).collect(Collectors.toList());
    }

    private CategoryTreeNodeResponse buildTreeNode(CategoryResponse parent, List<CategoryResponse> children) {
        CategoryTreeNodeResponse parentNode = toTreeNode(parent);
        List<CategoryTreeNodeResponse> childNodes = children.stream()
                .filter(childItem -> childItem.getParentId().equals(parent.getId()))
                .map(this::toTreeNode)
                .collect(Collectors.toList());
        parentNode.setChildren(childNodes);
        return parentNode;
    }

    private CategoryTreeNodeResponse toTreeNode(CategoryResponse entity) {
        CategoryTreeNodeResponse response = new CategoryTreeNodeResponse();
        response.setId(entity.getId());
        response.setCategoryName(entity.getCategoryName());
        response.setCategoryIcon(entity.getCategoryIcon());
        response.setParentId(entity.getParentId());
        response.setExpenseType(entity.getExpenseType());
        response.setRemark(entity.getRemark());
        return response;
    }
}
