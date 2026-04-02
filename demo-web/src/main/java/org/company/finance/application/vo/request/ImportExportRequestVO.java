package org.company.finance.application.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11
 *
 */
@Data
@ApiModel("导入导出请求参数")
public class ImportExportRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "日期格式不能为空")
    @ApiModelProperty("开始日期 yyyy-MM-dd")
    private String startDate;

    @NotBlank(message = "结束日期不能为空")
    @ApiModelProperty("结束日期 yyyy-MM-dd")
    private String endDate;
}
