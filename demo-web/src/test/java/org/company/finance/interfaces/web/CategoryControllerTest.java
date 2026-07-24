package org.company.finance.interfaces.web;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.company.finance.application.command.service.CategoryCommandService;
import org.company.finance.application.command.service.FinanceCommandService;
import org.company.finance.application.query.service.CategoryQueryService;
import org.company.finance.application.vo.request.CategoryRequestVO;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.company.finance.test.MapperExclusionFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.filter.TypeExcludeFilters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-13 10:03
 *
 */
class CategoryControllerTest {

    @Mock
    private CategoryQueryService queryCategoryService;

    @Mock
    private CategoryCommandService commandCategoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;


    // 用于生成 JSON 字符串
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // 测试数据
    private CategoryEntity parentCategory;
    private CategoryEntity childCategory;
    private CategoryRequestVO requestVO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 初始化 MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        // 初始化父类目
        parentCategory = new CategoryEntity();
        parentCategory.setId(1);
        parentCategory.setCategoryName("电子产品");
        parentCategory.setCategoryIcon("icon-electronics");
        parentCategory.setParentId(0);
        parentCategory.setExpenseType(1); // 支出
        parentCategory.setRemark("电子产品类目");
        parentCategory.setCreateName("admin");
        parentCategory.setCreateTime("2025-01-01 10:00:00");
        parentCategory.setUpdateName("admin");
        parentCategory.setUpdateTime("2025-01-01 10:00:00");
        parentCategory.setDelFlag(0);
        parentCategory.setList(new ArrayList<>()); // 初始化空列表

        // 初始化子类目
        childCategory = new CategoryEntity();
        childCategory.setId(2);
        childCategory.setCategoryName("手机");
        childCategory.setCategoryIcon("icon-phone");
        childCategory.setParentId(1);
        childCategory.setExpenseType(1);
        childCategory.setRemark("手机类目");
        childCategory.setCreateName("admin");
        childCategory.setCreateTime("2025-01-02 11:00:00");
        childCategory.setUpdateName("admin");
        childCategory.setUpdateTime("2025-01-02 11:00:00");
        childCategory.setDelFlag(0);
        childCategory.setList(new ArrayList<>());

        // 请求 VO
        requestVO = new CategoryRequestVO();
        requestVO.setPageNum(1);
        requestVO.setPageSize(10);
        requestVO.setCategoryName("手机");
    }

    // ========== 分页查询类目 ==========
    @Test
    void testQueryCategory() throws Exception {
        Page<CategoryEntity> page = new Page<>(1, 10);
        page.setRecords(Arrays.asList(childCategory));
        page.setTotal(1);

        when(queryCategoryService.queryCategory(any(CategoryRequestVO.class))).thenReturn(page);

        mockMvc.perform(post("/category/queryCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"pageNum\":1,\"pageSize\":10,\"categoryName\":\"手机\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.records[0].categoryName").value("手机"))
                .andExpect(jsonPath("$.data.records[0].parentId").value(1));

        verify(queryCategoryService, times(1)).queryCategory(any(CategoryRequestVO.class));
    }

    // ========== 查询所有类目 ==========
    /**
     * 测试用例：验证查询所有分类接口的正常执行流程
     * 场景：Controller 正常调用 Service，返回有效的分类列表数据
     */
    @Test
    void testQueryAllCategory() throws Exception {
        // ========================================================================
        // 1. Arrange（准备阶段）：构建测试环境、模拟依赖行为
        // ========================================================================

        // 模拟 Service 层方法 queryAllCategory 的返回值
        // 当 Controller 调用 queryCategoryService.queryAllCategory(any(CategoryRequestVO.class)) 时，
        // 不真正执行业务逻辑，而是“伪造”返回两个预设的分类对象（parentCategory 和 childCategory）
        // 这样可以隔离外部依赖（如数据库），确保测试只关注 Controller 本身的逻辑
        when(queryCategoryService.queryAllCategory(any(CategoryRequestVO.class)))
                .thenReturn(Arrays.asList(parentCategory, childCategory));

        // ========================================================================
        // 2. Act & Assert（执行与断言阶段）：发起请求并验证响应
        // ========================================================================

        // 使用 MockMvc 模拟发送一个 POST 请求到 "/category/queryAllCategory"
        // 设置请求头 Content-Type 为 application/json，表示发送的是 JSON 数据
        // 使用 ObjectMapper 将 requestVO 对象序列化为 JSON 字符串作为请求体
        // 这比手写 JSON 字符串更安全、不易出错（避免拼写错误或格式问题）
        ResultActions result = mockMvc.perform(post("/category/queryAllCategory")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestVO)));

        // ========================================================================
        // 3. 断言（Assert）：验证响应是否符合预期
        // ========================================================================

        // 打印整个 HTTP 响应（包括状态码、头信息、响应体等）
        // 在调试失败时非常有用，可以直接看到返回了什么
        result.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())

                // 验证 HTTP 状态码是否为 200 OK（表示请求被成功接收）
                .andExpect(status().isOk())

                // 验证响应 JSON 中的 "code" 字段是否等于 0
                // 表示业务处理成功（根据 R<T> 统一封装的设计）
                .andExpect(jsonPath("$.code").value(0))

                // 验证响应 JSON 中的 "msg" 字段是否为 "执行成功"
                // 与 ApiErrorCode.SUCCESS.msg 保持一致，确保提示信息正确
                .andExpect(jsonPath("$.msg").value("执行成功"))

                // 验证 "data" 字段是一个数组（而不是 null 或对象）
                .andExpect(jsonPath("$.data").isArray())

                // 验证 data 数组的长度为 2，表示返回了两个分类
                .andExpect(jsonPath("$.data.length()").value(2))

                // 验证第一个分类的 categoryName 是否为 "电子产品"
                .andExpect(jsonPath("$.data[0].categoryName").value("电子产品"))

                // 验证第二个分类的 categoryName 是否为 "手机"
                .andExpect(jsonPath("$.data[1].categoryName").value("手机"))

                // 验证第一个分类的 id 是否为 1
                .andExpect(jsonPath("$.data[0].id").value(1))

                // 验证第二个分类的 id 是否为 2
                .andExpect(jsonPath("$.data[1].id").value(2));

        // ========================================================================
        // 4. Verify（行为验证）：验证 Controller 是否正确调用了 Service
        // ========================================================================

        // 验证 queryCategoryService 的 queryAllCategory 方法是否被调用了一次
        // 并且传入的参数是任意 CategoryRequestVO 实例（any(...)）
        // 这确保了 Controller 确实委托了业务逻辑给 Service，而不是跳过或硬编码返回
        verify(queryCategoryService, times(1)).queryAllCategory(any(CategoryRequestVO.class));
    }

    // ========== 查询树形结构 ==========
    @Test
    void testQueryTreeCategoryList() throws Exception {
        // 构造树形结构：parent -> child
        childCategory.setParentId(1);
        parentCategory.setList(Collections.singletonList(childCategory));

        when(queryCategoryService.queryTreeCategoryList())
                .thenReturn(Collections.singletonList(parentCategory));

        mockMvc.perform(post("/category/queryTreeCategoryList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data[0].categoryName").value("电子产品"))
                .andExpect(jsonPath("$.data[0].list[0].categoryName").value("手机"))
                .andExpect(jsonPath("$.data[0].list[0].parentId").value(1));

        verify(queryCategoryService, times(1)).queryTreeCategoryList();
    }

    // ========== 新增类目 ==========
    @Test
    void testAddCategory() throws Exception {
        doNothing().when(commandCategoryService).addCategory(any(CategoryEntity.class));

        String jsonContent = "{"
                + "\"categoryName\": \"新服饰\","
                + "\"categoryIcon\": \"icon-clothes\","
                + "\"parentId\": 0,"
                + "\"expenseType\": 1,"
                + "\"remark\": \"新增服饰类目\""
                + "}";

        mockMvc.perform(post("/category/addCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("执行成功"))
                .andExpect(jsonPath("$.data").value("新增成功！"));

        verify(commandCategoryService, times(1)).addCategory(any(CategoryEntity.class));
    }

    // ========== 删除类目 ==========
    @Test
    void testDeleteCategory() throws Exception {
        doNothing().when(commandCategoryService).deleteCategory(any(Integer.class));

        mockMvc.perform(post("/category/deleteCategory")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("执行成功"))
                .andExpect(jsonPath("$.data").value("删除成功！"));

        verify(commandCategoryService, times(1)).deleteCategory(eq(1));
    }

    // ========== 修改类目 ==========
    @Test
    void testUpdateCategory() throws Exception {
        doNothing().when(commandCategoryService).updateCategory(any(CategoryEntity.class));

        String jsonContent = "{"
                + "\"id\": 2,"
                + "\"categoryName\": \"高端手机\","
                + "\"categoryIcon\": \"icon-phone-premium\","
                + "\"parentId\": 1,"
                + "\"expenseType\": 1,"
                + "\"remark\": \"更新为高端手机类目\""
                + "}";

        mockMvc.perform(post("/category/updateCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("执行成功"))
                .andExpect(jsonPath("$.data").value("修改成功！"));

        verify(commandCategoryService, times(1)).updateCategory(any(CategoryEntity.class));
    }
}
