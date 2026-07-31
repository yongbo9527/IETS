package org.company.finance.category.application.query.model;

import lombok.Data;

/**
 * 写侧校验使用的类目快照
 *
 * @author Ron Yu
 * @date 2026-07-27
 */
@Data
public class CategorySnapshot {

    private Integer id;

    private String categoryName;

    private Integer parentId;

    private Integer expenseType;

    private Integer delFlag;
}
