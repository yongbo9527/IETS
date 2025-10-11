package org.company.finance.application.vo.report;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-01-10 10:27
 *
 */
@Data
public class ReportDatasetVO {

    private Integer categoryId;

    private String categoryName;

    private Integer parentId;

    private String parentName;

    private BigDecimal expenseAmount;

    private String dailyDate;



//    private LinkedMultiValueMap<String, String> datasetMap;
}
