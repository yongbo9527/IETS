package org.company.finance.category.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.company.finance.category.domain.model.Category;
import org.company.finance.category.domain.repository.CommandCategoryRepository;
import org.springframework.stereotype.Repository;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 18:15
 *
 */
@Repository
@RequiredArgsConstructor
public class CommandCategoryRepositoryImpl implements CommandCategoryRepository {

    private final CategoryMapper categoryMapper;

    @Override
    public void save(Category category) {
        categoryMapper.insert(toEntity(category));
    }

    @Override
    public void updateById(Integer id) {
        LambdaUpdateWrapper<CategoryDO> wrapper = new LambdaUpdateWrapper<CategoryDO>().eq(CategoryDO::getId, id);
        wrapper.set(CategoryDO::getDelFlag, 1);
        categoryMapper.update(null, wrapper);
    }

    @Override
    public void update(Category category) {
        CategoryDO entity = toEntity(category);
        LambdaUpdateWrapper<CategoryDO> updatedWrapper =  new LambdaUpdateWrapper<CategoryDO>().eq(CategoryDO::getId, entity.getId());
        categoryMapper.update(entity, updatedWrapper);
    }

    private CategoryDO toEntity(Category category) {
        CategoryDO entity = new CategoryDO();
        entity.setId(category.getId());
        entity.setCategoryName(category.getCategoryName());
        entity.setParentId(category.getParentId());
        entity.setExpenseType(category.getExpenseType().getCode());
        entity.setDelFlag(category.isDeleted() ? 1 : 0);
        return entity;
    }
}
