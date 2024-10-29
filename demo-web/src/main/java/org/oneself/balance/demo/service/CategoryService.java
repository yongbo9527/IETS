package org.oneself.balance.demo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.oneself.balance.demo.entity.CategoryEntity;
import org.oneself.balance.demo.vo.CategoryRequestVO;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:03
 *
 */
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * 新增类目
     */
    void addCategory(CategoryEntity entity);

    /**
     * 删除类目
     */
    void deleteCategory(Integer id);

    /**
     * 分页查询类目
     */
    Page<CategoryEntity> queryCategory(CategoryRequestVO requestVO);

    /**
     * 修改类目
     */
    void updateCategory(CategoryEntity entity);
}
