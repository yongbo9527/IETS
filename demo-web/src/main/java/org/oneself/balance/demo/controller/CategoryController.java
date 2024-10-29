package org.oneself.balance.demo.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.oneself.balance.demo.entity.CategoryEntity;
import org.oneself.balance.demo.service.CategoryService;
import org.oneself.balance.demo.vo.CategoryRequestVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:01
 *
 */
@RestController
@RequestMapping("/category")
@Api(value = "类目")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @ApiOperation(value = "分页查询类目")
    @PostMapping("/queryCategory")
    public R<Page<CategoryEntity>> queryCategory(@RequestBody CategoryRequestVO requestVO) {
        Page<CategoryEntity> entityPage = categoryService.queryCategory(requestVO);
        return R.ok(entityPage);
    }

    @ApiOperation(value = "新增类目")
    @PostMapping("/addCategory")
    public R addCategory(@RequestBody CategoryEntity entity) {
        categoryService.addCategory(entity);
        return R.ok("新增成功！");
    }

    @ApiOperation(value = "删除类目")
    @PostMapping("/deleteCategory")
    public R deleteCategory(Integer id) {
        categoryService.deleteCategory(id);
        return R.ok("删除成功！");
    }

    @ApiOperation(value = "修改类目")
    @PostMapping("/updateCategory")
    public R updateCategory(@RequestBody CategoryEntity entity) {
        categoryService.updateCategory(entity);
        return R.ok("修改成功！");
    }


}
