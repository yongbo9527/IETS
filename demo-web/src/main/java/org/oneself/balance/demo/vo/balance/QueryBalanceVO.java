package org.oneself.balance.demo.vo.balance;

import lombok.Data;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-10 14:01
 *
 */
@Data
public class QueryBalanceVO {
    private Integer categoryId;
    private String startDate;
    private String endDate;
    private Integer current = 1;
    private Integer pageSize = 10;
}
