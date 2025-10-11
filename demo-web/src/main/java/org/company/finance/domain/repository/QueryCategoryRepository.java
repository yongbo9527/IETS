package org.company.finance.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.application.vo.request.CategoryRequestVO;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;

import java.util.List;
import java.util.Set;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 17:12
 *
 */
public interface QueryCategoryRepository {
    List<CategoryEntity> findActiveOrderByAsc();

    List<DataCategoryVO> findDataCategory(QueryBalanceVO vo);

    List<CategoryEntity> findByCategoryIds(Set<Integer> parentIds);

    Page<CategoryEntity> queryCategoryWithPage(CategoryRequestVO requestVO);

    List<CategoryEntity> queryAllCategory(CategoryRequestVO requestVO);
}
