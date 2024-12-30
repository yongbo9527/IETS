package org.oneself.balance.demo.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 21:05
 *
 */
@Data
@ApiModel("报表查询参数")
public class ReportRequestVO {

    @ApiModelProperty("开始日期")
    private String startDate;

    @ApiModelProperty("结束日期")
    private String endDate;
}
