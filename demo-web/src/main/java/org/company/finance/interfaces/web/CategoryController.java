package org.company.finance.interfaces.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.query.service.CategoryQueryService;
import org.company.finance.application.command.service.CategoryCommandService;
import org.company.finance.application.vo.request.CategoryRequestVO;
import org.company.finance.common.util.R;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:01
 *
 */
@RestController
@RequestMapping("/category")
@Api(value = "类目")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService queryCategoryService;
    private final CategoryCommandService commandCategoryService;

    @ApiOperation(value = "分页查询类目")
    @PostMapping("/queryCategory")
    public R<Page<CategoryEntity>> queryCategory(@RequestBody CategoryRequestVO requestVO) {
        Page<CategoryEntity> entityPage = queryCategoryService.queryCategory(requestVO);
        return R.ok(entityPage);
    }

    @ApiOperation(value = "查询所有类目")
    @PostMapping("/queryAllCategory")
    public R<List<CategoryEntity>> queryAllCategory(@RequestBody CategoryRequestVO requestVO) {
        List<CategoryEntity> entityPage = queryCategoryService.queryAllCategory(requestVO);
        return R.ok(entityPage);
    }

    @ApiOperation(value = "查询树形类目结构")
    @PostMapping("/queryTreeCategoryList")
    public R<List<CategoryEntity>> queryTreeCategoryList() {
        List<CategoryEntity> list = queryCategoryService.queryTreeCategoryList();
        return R.ok(list);
    }

    @ApiOperation(value = "新增类目")
    @PostMapping("/addCategory")
    public R addCategory(@RequestBody CategoryEntity entity) {
        commandCategoryService.addCategory(entity);
        return R.ok("新增成功！");
    }

    @ApiOperation(value = "删除类目")
    @PostMapping("/deleteCategory")
    public R deleteCategory(Integer id) {
        commandCategoryService.deleteCategory(id);
        return R.ok("删除成功！");
    }

    @ApiOperation(value = "修改类目")
    @PostMapping("/updateCategory")
    public R updateCategory(@RequestBody CategoryEntity entity) {
        commandCategoryService.updateCategory(entity);
        return R.ok("修改成功！");
    }


}
