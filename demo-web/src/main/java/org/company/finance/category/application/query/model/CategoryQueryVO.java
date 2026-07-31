package org.company.finance.category.application.query.model;

import lombok.Data;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-10 17:13
 *
 */
@Data
public class CategoryQueryVO {

    private Integer id;

    private String categoryName;

    private String categoryIcon;

    private Integer parentId;

    private Integer expenseType;

    private String remark;

}
