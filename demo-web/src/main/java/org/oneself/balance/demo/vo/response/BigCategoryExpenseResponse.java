package org.oneself.balance.demo.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-24 11:27
 *
 */
@Data
@ApiModel("饼图列表：大类支出统计数据")
public class BigCategoryExpenseResponse {

    @ApiModelProperty("父级类目id")
    private Integer parentId;

    @ApiModelProperty("父级类目名称")
    private String parentCategory;

    @ApiModelProperty("支出金额")
    private BigDecimal expenseTotal;

    @ApiModelProperty("支出占比")
    private BigDecimal expensePercent = BigDecimal.ZERO;

    @ApiModelProperty("支出笔数")
    private Integer expenseCount;

    @ApiModelProperty("子类目id")
    private Integer childId;

    @ApiModelProperty("子类目名称")
    private String childCategory;

}
