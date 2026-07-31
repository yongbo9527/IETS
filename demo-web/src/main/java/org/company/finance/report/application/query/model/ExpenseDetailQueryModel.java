package org.company.finance.report.application.query.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2026-07-28 17:00
 *
 */
@Data
public class ExpenseDetailQueryModel {

    private String expenseDate;

    private Integer parentId;

    private String parentCategory;

    private Integer childId;

    private String childCategory;

    private BigDecimal expenseTotal;

    private String remark;
}
