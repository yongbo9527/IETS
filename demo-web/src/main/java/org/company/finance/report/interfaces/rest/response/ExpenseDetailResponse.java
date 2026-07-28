package org.company.finance.report.interfaces.rest.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-04-01 17:37
 *
 */
@Schema(description = "支出明细")
@Data
public class ExpenseDetailResponse {

    @Schema(description = "支出日期")
    private String expenseDate;

    @Schema(description = "一级类目id")
    private Integer parentId;

    @Schema(description = "一级类目名称")
    private String parentCategory;

    @Schema(description = "二级类目id")
    private Integer childId;

    @Schema(description = "二级类目名称")
    private String childCategory;

    @Schema(description = "支出金额")
    private BigDecimal expenseTotal;

    @Schema(description = "备注")
    private String remark;

    private static final Pattern TRANSFER_PATTERN =
            Pattern.compile("由 服务助手 转交给 .+，原因：【系统识别需干预，.+】");

    public static boolean shouldFilter(String message) {
        return TRANSFER_PATTERN.matcher(message).matches();
    }

}
