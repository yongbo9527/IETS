package org.company.finance.tally.interfaces.rest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-10 13:48
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDetailHeader {

    private String dataIndex; // 字段属性，用于映射行数据
    private String title; // 显示名
    private LinkedList<TableDetailHeader> children;

}
