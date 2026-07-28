package org.company.finance.application.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 21:20
 *
 */
@Data
@Schema(description = "折线图数据返回")
public class LineEchartsResponse {
    @Schema(description = "x轴时间日期数据")
    private List<String> xAxisData;

    @Schema(description = "收入数据")
    private List<BigDecimal> incomeData;

    @Schema(description = "支出数据")
    private List<BigDecimal> expenseData;


}
