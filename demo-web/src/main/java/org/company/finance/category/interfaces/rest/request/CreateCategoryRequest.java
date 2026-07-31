package org.company.finance.category.interfaces.rest.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 新增类目请求
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
@Data
public class CreateCategoryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "类目名称不能为空")
    @Length(max = 50, message = "类目名称长度不能超过50")
    private String categoryName;

    @Length(max = 200, message = "类目图标长度不能超过200")
    private String categoryIcon;

    @Min(value = 0, message = "父级类目ID不能小于0")
    private Integer parentId;

    @Range(min = 1, max = 2, message = "消费类型只能是1-支出或2-收入")
    private Integer expenseType;

    @Length(max = 500, message = "备注长度不能超过500")
    private String remark;
}
