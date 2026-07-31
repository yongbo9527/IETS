package org.company.finance.report.application.query.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-01-10 10:27
 *
 */
@Data
public class ReportDatasetQueryModel {

    private Integer categoryId;

    private String categoryName;

    private Integer parentId;

    private String parentName;

    private BigDecimal expenseAmount;

    private String dailyDate;
}
