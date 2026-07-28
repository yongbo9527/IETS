package org.company.finance.category.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.category.application.query.model.CategorySnapshot;
import org.company.finance.category.application.query.model.DataCategoryVO;
import org.company.finance.category.interfaces.rest.request.CategoryRequestVO;
import org.company.finance.category.interfaces.rest.response.CategoryResponse;

import java.util.List;
import java.util.Set;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 17:12
 *
 */
public interface QueryCategoryRepository {
    CategorySnapshot findById(Integer id);

    List<CategoryResponse> findActiveOrderByAsc();

    List<DataCategoryVO> findDataCategory(QueryBalanceVO vo);

    List<CategoryResponse> findByCategoryIds(Set<Integer> parentIds);

    Page<CategoryResponse> queryCategoryWithPage(CategoryRequestVO requestVO);

    List<CategoryResponse> queryAllCategory(CategoryRequestVO requestVO);
}
