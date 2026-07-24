package org.company.finance.application.vo.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改类目请求
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
@Data
public class UpdateCategoryRequest extends CreateCategoryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "ID不能为空")
    @Min(value = 1, message = "ID不能小于1")
    private Integer id;
}
