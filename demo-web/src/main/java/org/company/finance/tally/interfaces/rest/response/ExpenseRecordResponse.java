package org.company.finance.tally.interfaces.rest.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收支记录查询响应
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
@Data
public class ExpenseRecordResponse {

    private Integer id;

    private Integer categoryId;

    private String categoryName;

    private BigDecimal expenseAmount;

    private String expenseDate;

    private Integer expenseType;

    private String remark;

    private Date createTime;

    private Date updateTime;
}
