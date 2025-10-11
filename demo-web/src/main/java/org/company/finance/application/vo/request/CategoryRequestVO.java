package org.company.finance.application.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 14:24
 *
 */
@Data
@ApiModel("类目请求体")
public class CategoryRequestVO {

    @ApiModelProperty(value = "类目名称")
    private String categoryName;

    @ApiModelProperty(value = "每页条数")
    private Integer pageSize = 10;

    @ApiModelProperty(value = "页码")
    private Integer pageNum = 1;
}
