package org.company.finance.category.interfaces.rest.response;

import lombok.Data;

/**
 * 类目查询响应
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
@Data
public class CategoryResponse {

    private Integer id;

    private String categoryName;

    private String categoryIcon;

    private Integer parentId;

    private Integer expenseType;

    private String remark;
}
