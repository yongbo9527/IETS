package org.company.finance.category.interfaces.rest.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 14:24
 *
 */
@Data
@ApiModel("类目请求体")
public class CategoryRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Size(max = 50, message = "类目名称长度不能超过50")
    private String categoryName;

    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 10;

    @Min(value = 1, message = "页码不能小于1")
    private Integer pageNum = 1;
}
