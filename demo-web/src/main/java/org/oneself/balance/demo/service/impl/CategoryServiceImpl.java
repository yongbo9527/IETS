package org.oneself.balance.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.oneself.balance.demo.entity.CategoryEntity;
import org.oneself.balance.demo.mapper.CategoryMapper;
import org.oneself.balance.demo.service.CategoryService;
import org.oneself.balance.demo.vo.CategoryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:03
 *
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;


    @Override
    public void addCategory(CategoryEntity entity) {
        this.save(entity);
    }

    @Override
    public void deleteCategory(Integer id) {
        LambdaUpdateWrapper<CategoryEntity> wrapper = new LambdaUpdateWrapper<CategoryEntity>().eq(CategoryEntity::getId, id);
        wrapper.set(CategoryEntity::getDelFlag, 1);
        this.update(wrapper);

    }

    @Override
    public Page<CategoryEntity> queryCategory(CategoryRequestVO requestVO) {
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(CategoryEntity::getCategoryName, requestVO.getCategoryName())
                .eq(CategoryEntity::getDelFlag, 0)
                .orderByDesc(CategoryEntity::getCreateTime);

        Page<CategoryEntity> categoryEntityPage = new Page<>(requestVO.getPageNum(), requestVO.getPageSize());
        Page<CategoryEntity> page = this.page(categoryEntityPage, queryWrapper);

        return page;
    }


    @Override
    public void updateCategory(CategoryEntity entity) {
        LambdaUpdateWrapper<CategoryEntity> updatedWrapper =  new LambdaUpdateWrapper<CategoryEntity>().eq(CategoryEntity::getId, entity.getId());
        this.update(entity, updatedWrapper);
    }
}
