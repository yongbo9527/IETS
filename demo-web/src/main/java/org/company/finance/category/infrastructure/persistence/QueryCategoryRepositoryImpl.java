package org.company.finance.category.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.company.finance.category.application.query.model.CategorySnapshot;
import org.company.finance.category.application.query.model.DataCategoryVO;
import org.company.finance.category.domain.repository.QueryCategoryRepository;
import org.company.finance.category.interfaces.rest.request.CategoryRequestVO;
import org.company.finance.category.interfaces.rest.response.CategoryResponse;
import org.company.finance.tally.interfaces.rest.request.QueryBalanceVO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public CategorySnapshot findById(Integer id) {
        CategoryDO entity = categoryMapper.selectById(id);
        if (entity == null) {
            return null;
        }
        CategorySnapshot snapshot = new CategorySnapshot();
        snapshot.setId(entity.getId());
        snapshot.setCategoryName(entity.getCategoryName());
        snapshot.setParentId(entity.getParentId());
        snapshot.setExpenseType(entity.getExpenseType());
        snapshot.setDelFlag(entity.getDelFlag());
        return snapshot;
    }

    @Override
    public List<CategoryResponse> findActiveOrderByAsc() {
        LambdaQueryWrapper<CategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryDO::getDelFlag, 0)
                .orderByAsc(CategoryDO::getId);
        return categoryMapper.selectList(queryWrapper).stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DataCategoryVO> findDataCategory(QueryBalanceVO vo) {
        List<DataCategoryVO> dataCategoryList = categoryMapper.selectDataCategory(vo);
        return dataCategoryList;
    }

    @Override
    public List<CategoryResponse> findByCategoryIds(Set<Integer> parentIds) {
        LambdaQueryWrapper<CategoryDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(CategoryDO::getId, CategoryDO::getCategoryName, CategoryDO::getParentId)
                .in(CategoryDO::getId, parentIds);
        return categoryMapper.selectList(wrapper).stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<CategoryResponse> queryCategoryWithPage(CategoryRequestVO requestVO) {
        LambdaQueryWrapper<CategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(requestVO.getCategoryName()),
                        CategoryDO::getCategoryName, requestVO.getCategoryName())
                .eq(CategoryDO::getDelFlag, 0)
                .orderByDesc(CategoryDO::getCreateTime);

        Page<CategoryDO> categoryEntityPage = new Page<>(requestVO.getPageNum(), requestVO.getPageSize());
        Page<CategoryDO> page = categoryMapper.selectPage(categoryEntityPage, queryWrapper);
        Page<CategoryResponse> responsePage = new Page<>(page.getCurrent(), page.getSize());
        responsePage.setTotal(page.getTotal());
        responsePage.setRecords(page.getRecords().stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList()));
        return responsePage;
    }

    @Override
    public List<CategoryResponse> queryAllCategory(CategoryRequestVO requestVO) {
        LambdaQueryWrapper<CategoryDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryDO::getDelFlag, 0);
        return categoryMapper.selectList(queryWrapper).stream()
                .map(this::toCategoryResponse)
                .collect(Collectors.toList());
    }

    private CategoryResponse toCategoryResponse(CategoryDO entity) {
        CategoryResponse response = new CategoryResponse();
        response.setId(entity.getId());
        response.setCategoryName(entity.getCategoryName());
        response.setCategoryIcon(entity.getCategoryIcon());
        response.setParentId(entity.getParentId());
        response.setExpenseType(entity.getExpenseType());
        response.setRemark(entity.getRemark());
        return response;
    }
}
