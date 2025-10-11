package org.company.finance.application.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-04-01 17:37
 *
 */
@ApiModel("支出明细")
@Data
public class ExpenseDetailResponse {

    @ApiModelProperty(value = "支出日期")
    private String expenseDate;

    @ApiModelProperty(value = "一级类目id")
    private Integer parentId;

    @ApiModelProperty(value = "一级类目名称")
    private String parentCategory;

    @ApiModelProperty(value = "二级类目id")
    private Integer childId;

    @ApiModelProperty(value = "二级类目名称")
    private String childCategory;

    @ApiModelProperty(value = "支出金额")
    private BigDecimal expenseTotal;

    @ApiModelProperty(value = "备注")
    private String remark;

    private static final Pattern TRANSFER_PATTERN =
            Pattern.compile("由 服务助手 转交给 .+，原因：【系统识别需干预，.+】");

    public static boolean shouldFilter(String message) {
        return TRANSFER_PATTERN.matcher(message).matches();
    }

    // 使用示例
    public static void main(String[] args) {

    }

}
