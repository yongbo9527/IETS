package org.company.finance.application.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 21:05
 *
 */
@Data
@ApiModel("报表查询参数")
public class ReportRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式应为 yyyy-MM-dd")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式应为 yyyy-MM-dd")
    private String endDate;

    @Min(value = 1, message = "大类ID不能小于1")
    private Integer bigCategoryId;

    @Min(value = 1, message = "小类ID不能小于1")
    private Integer smallCategoryId;
}
