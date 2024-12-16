package org.oneself.balance.demo.vo.balance;

import lombok.Data;

import java.math.BigDecimal;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-11 16:52
 *
 */
@Data
public class RecordVO {

    private BigDecimal expenseAmount;

    private String remark;


}
