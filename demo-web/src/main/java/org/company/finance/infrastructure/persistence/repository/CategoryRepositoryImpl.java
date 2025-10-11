package org.company.finance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.domain.repository.CategoryRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.company.finance.infrastructure.persistence.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-10 18:19
 *
 */
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryEntity> findActiveOrderByAsc() {
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEntity::getDelFlag, 0)
                .orderByAsc(CategoryEntity::getId);
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(queryWrapper);
        return categoryEntities;
    }

    @Override
    public List<DataCategoryVO> findDataCategory(QueryBalanceVO vo) {
        List<DataCategoryVO> dataCategoryList = categoryMapper.selectDataCategory(vo);
        return dataCategoryList;
    }

    @Override
    public List<CategoryEntity> findByCategoryIds(Set<Integer> parentIds) {
        LambdaQueryWrapper<CategoryEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CategoryEntity::getId, CategoryEntity::getCategoryName, CategoryEntity::getParentId)
                .in(CategoryEntity::getId, parentIds);
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(wrapper);

        return categoryEntities;
    }

}
