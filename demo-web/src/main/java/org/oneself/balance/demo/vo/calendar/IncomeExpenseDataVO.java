package org.oneself.balance.demo.vo.calendar;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("日历支出收入数据")
@AllArgsConstructor
@NoArgsConstructor
public class IncomeExpenseDataVO {

    @ApiModelProperty("收入")
    private BigDecimal income;

    @ApiModelProperty("支出")
    private BigDecimal expense;
}
