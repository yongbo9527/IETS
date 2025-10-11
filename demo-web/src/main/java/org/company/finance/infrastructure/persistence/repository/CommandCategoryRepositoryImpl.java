package org.company.finance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.company.finance.domain.repository.CommandCategoryRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.company.finance.infrastructure.persistence.mapper.CategoryMapper;
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
    public void addCategory(CategoryEntity entity) {
        categoryMapper.insert(entity);
    }

    @Override
    public void updateById(Integer id) {
        LambdaUpdateWrapper<CategoryEntity> wrapper = new LambdaUpdateWrapper<CategoryEntity>().eq(CategoryEntity::getId, id);
        wrapper.set(CategoryEntity::getDelFlag, 1);
        categoryMapper.update(null, wrapper);
    }

    @Override
    public void updateCategory(CategoryEntity entity) {
        LambdaUpdateWrapper<CategoryEntity> updatedWrapper =  new LambdaUpdateWrapper<CategoryEntity>().eq(CategoryEntity::getId, entity.getId());
        categoryMapper.update(entity, updatedWrapper);
    }
}
