package org.company.finance.tally.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-11-19 15:41
 *
 */
@Data
@TableName("daily_expense_record")
public class ExpenseRecordDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId
    @Min(value = 1, message = "ID不能小于1")
    private Integer id;

    @NotNull(message = "类目ID不能为空")
    @Min(value = 1, message = "类目ID不能小于1")
    private Integer categoryId;

    @TableField(exist = false)
    private String categoryName;

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

    @Size(max = 50, message = "创建者名称长度不能超过50")
    private String createName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Size(max = 50, message = "更新者名称长度不能超过50")
    private String updateName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @Range(min = 0, max = 1, message = "删除标记只能是0或1")
    private Integer delFlag;
}
