package org.company.finance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.application.vo.request.CategoryRequestVO;
import org.company.finance.domain.repository.QueryCategoryRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.company.finance.infrastructure.persistence.mapper.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 17:13
 *
 */
@Repository
@RequiredArgsConstructor
public class QueryCategoryRepositoryImpl implements QueryCategoryRepository {

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

    @Override
    public Page<CategoryEntity> queryCategoryWithPage(CategoryRequestVO requestVO) {
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(requestVO.getCategoryName()),
                        CategoryEntity::getCategoryName, requestVO.getCategoryName())
                .eq(CategoryEntity::getDelFlag, 0)
                .orderByDesc(CategoryEntity::getCreateTime);

        Page<CategoryEntity> categoryEntityPage = new Page<>(requestVO.getPageNum(), requestVO.getPageSize());
        Page<CategoryEntity> page = categoryMapper.selectPage(categoryEntityPage, queryWrapper);

        return page;
    }

    @Override
    public List<CategoryEntity> queryAllCategory(CategoryRequestVO requestVO) {
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEntity::getDelFlag, 0);
        return categoryMapper.selectList(queryWrapper);
    }
}
