package org.company.finance.application.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:23
 *
 */
@Data
@Schema(description = "日历支出收入数据返回")
public class ReportDataResponse {

    @Schema(description = "日期")
    private String date;

    @Schema(description = "收入|支出金额")
    private BigDecimal amount;

    @Schema(description = "消费类型，1-支出，2-收入")
    private Integer expenseType;
}
