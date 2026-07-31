package org.company.finance.report.interfaces.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2026-07-30 18:16
 *  @Description:
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "总额计算")
public class IncomeExpenseSummaryResponse {

    @Schema(description = "支出总额")
    private BigDecimal totalExpense;

    @Schema(description = "收入总额")
    private BigDecimal totalIncome;

    @Schema(description = "净收入总额（收入 - 支出）")
    private BigDecimal netIncome;
}
