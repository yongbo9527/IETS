package org.company.finance.application.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:23
 *
 */
@Data
@ApiModel("日历支出收入数据返回")
public class ReportDataResponse {

    @ApiModelProperty("日期")
    private String date;

    @ApiModelProperty("收入|支出金额")
    private BigDecimal amount;

    @ApiModelProperty("消费类型，1-支出，2-收入")
    private Integer expenseType;
}
