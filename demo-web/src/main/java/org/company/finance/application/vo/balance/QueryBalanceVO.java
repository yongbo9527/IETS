package org.company.finance.application.vo.balance;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-10 14:01
 *
 */
@Data
public class QueryBalanceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Min(value = 1, message = "类目ID不能小于1")
    private Integer categoryId;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式应为 yyyy-MM-dd")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "日期格式应为 yyyy-MM-dd")
    private String endDate;

    @Min(value = 1, message = "页码不能小于1")
    private Integer current = 1;

    @Min(value = 1, message = "每页条数不能小于1")
    @Max(value = 100, message = "每页条数不能超过100")
    private Integer pageSize = 10;
}
