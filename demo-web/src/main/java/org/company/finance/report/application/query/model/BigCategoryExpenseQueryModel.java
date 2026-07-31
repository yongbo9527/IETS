package org.company.finance.report.application.query.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2026-07-28 17:00
 *
 */
@Data
public class BigCategoryExpenseQueryModel {

    private Integer parentId;

    private String parentCategory;

    private BigDecimal expenseTotal;

    private Integer expenseCount;

    private Integer childId;

    private String childCategory;
}
