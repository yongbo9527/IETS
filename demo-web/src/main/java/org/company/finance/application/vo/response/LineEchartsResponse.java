package org.company.finance.application.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 21:20
 *
 */
@Data
@ApiModel("折线图数据返回")
public class LineEchartsResponse {
    @ApiModelProperty("x轴时间日期数据")
    private List<String> xAxisData;

    @ApiModelProperty("收入数据")
    private List<BigDecimal> incomeData;

    @ApiModelProperty("支出数据")
    private List<BigDecimal> expenseData;


}
