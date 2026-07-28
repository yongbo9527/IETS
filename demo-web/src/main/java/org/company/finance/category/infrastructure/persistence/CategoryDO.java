package org.company.finance.category.infrastructure.persistence;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-10-29 11:19
 *
 */
@Data
@TableName("base_tally_category")
public class CategoryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    @Min(value = 1, message = "ID不能小于1")
    private Integer id;

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

    @Length(max = 50, message = "创建人名称长度不能超过50")
    private String createName;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "时间格式应为 yyyy-MM-dd HH:mm:ss")
    private String createTime;

    @Length(max = 50, message = "修改人名称长度不能超过50")
    private String updateName;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$", message = "时间格式应为 yyyy-MM-dd HH:mm:ss")
    private String updateTime;

    @Range(min = 0, max = 1, message = "删除标识只能是0或1")
    private Integer delFlag;

}
