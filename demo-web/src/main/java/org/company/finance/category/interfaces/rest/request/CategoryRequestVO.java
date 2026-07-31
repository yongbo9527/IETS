package org.company.finance.category.interfaces.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 14:24
 *
 */
@Data
@Schema(description = "类目请求体")
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
