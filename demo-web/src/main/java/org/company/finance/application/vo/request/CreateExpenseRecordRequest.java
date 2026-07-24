package org.company.finance.application.vo.request;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新增收支记录请求
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
@Data
public class CreateExpenseRecordRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "类目ID不能为空")
    @Min(value = 1, message = "类目ID不能小于1")
    private Integer categoryId;

    @NotNull(message = "消费金额不能为空")
    @DecimalMin(value = "0.01", message = "消费金额必须大于0")
    @Digits(integer = 10, fraction = 2, message = "消费金额格式不正确")
    private BigDecimal expenseAmount;

    @NotBlank(message = "消费日期不能为空")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式应为 yyyy-MM-dd")
    private String expenseDate;

    @NotNull(message = "消费类型不能为空")
    @Range(min = 1, max = 2, message = "消费类型只能是1-支出或2-收入")
    private Integer expenseType;

    @Size(max = 500, message = "备注长度不能超过500")
    private String remark;
}
