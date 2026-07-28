package org.company.finance.category.interfaces.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.company.finance.category.application.command.CategoryCommandService;
import org.company.finance.category.application.query.CategoryQueryService;
import org.company.finance.category.interfaces.rest.request.CreateCategoryRequest;
import org.company.finance.category.interfaces.rest.request.CategoryRequestVO;
import org.company.finance.category.interfaces.rest.request.UpdateCategoryRequest;
import org.company.finance.category.interfaces.rest.response.CategoryResponse;
import org.company.finance.category.interfaces.rest.response.CategoryTreeNodeResponse;
import org.company.finance.common.util.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:01
 *
 */
@RestController
@RequestMapping("/categories")
@Tag(name = "类目")
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryQueryService queryCategoryService;
    private final CategoryCommandService commandCategoryService;

    @Operation(summary = "分页查询类目")
    @PostMapping("/search")
    public R<Page<CategoryResponse>> queryCategory(@Validated @RequestBody CategoryRequestVO requestVO) {
        Page<CategoryResponse> entityPage = queryCategoryService.queryCategory(requestVO);
        return R.ok(entityPage);
    }

    @Operation(summary = "查询所有类目")
    @PostMapping("/all")
    public R<List<CategoryResponse>> queryAllCategory(@Validated @RequestBody CategoryRequestVO requestVO) {
        List<CategoryResponse> entityPage = queryCategoryService.queryAllCategory(requestVO);
        return R.ok(entityPage);
    }

    @Operation(summary = "查询树形类目结构")
    @PostMapping("/tree")
    public R<List<CategoryTreeNodeResponse>> queryTreeCategoryList() {
        List<CategoryTreeNodeResponse> list = queryCategoryService.queryTreeCategoryList();
        return R.ok(list);
    }

    @Operation(summary = "新增类目")
    @PostMapping
    public R addCategory(@Validated @RequestBody CreateCategoryRequest request) {
        commandCategoryService.addCategory(request);
        return R.ok("新增成功！");
    }

    @Operation(summary = "删除类目")
    @DeleteMapping("/{id}")
    public R deleteCategory(@PathVariable("id") @NotNull @Min(value = 1, message = "ID不能小于1") Integer id) {
        commandCategoryService.deleteCategory(id);
        return R.ok("删除成功！");
    }

    @Operation(summary = "修改类目")
    @PutMapping("/{id}")
    public R updateCategory(@PathVariable("id") @NotNull @Min(value = 1, message = "ID不能小于1") Integer id,
                            @Validated @RequestBody UpdateCategoryRequest request) {
        request.setId(id);
        commandCategoryService.updateCategory(request);
        return R.ok("修改成功！");
    }
}
