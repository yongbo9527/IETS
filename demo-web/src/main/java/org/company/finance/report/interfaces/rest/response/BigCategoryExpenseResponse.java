package org.company.finance.report.interfaces.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-24 11:27
 *
 */
@Data
@Schema(description = "饼图列表：大类支出统计数据")
public class BigCategoryExpenseResponse {

    @Schema(description = "父级类目id")
    private Integer parentId;

    @Schema(description = "父级类目名称")
    private String parentCategory;

    @Schema(description = "支出金额")
    private BigDecimal expenseTotal;

    @Schema(description = "支出占比")
    private BigDecimal expensePercent = BigDecimal.ZERO;

    @Schema(description = "支出笔数")
    private Integer expenseCount;

    @Schema(description = "子类目id")
    private Integer childId;

    @Schema(description = "子类目名称")
    private String childCategory;

}
