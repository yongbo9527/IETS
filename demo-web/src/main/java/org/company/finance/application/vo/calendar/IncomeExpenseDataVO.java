package org.company.finance.application.vo.calendar;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 18:16
 *
 */
@Data
@Schema(description = "日历支出收入数据")
@AllArgsConstructor
@NoArgsConstructor
public class IncomeExpenseDataVO {

    @Schema(description = "收入")
    private BigDecimal income;

    @Schema(description = "支出")
    private BigDecimal expense;
}
