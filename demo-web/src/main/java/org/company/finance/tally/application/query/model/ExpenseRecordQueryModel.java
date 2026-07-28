package org.company.finance.tally.application.query.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 收支记录查询模型
 *
 * @author Ron Yu
 * @date 2026-07-27
 */
@Data
public class ExpenseRecordQueryModel {

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
