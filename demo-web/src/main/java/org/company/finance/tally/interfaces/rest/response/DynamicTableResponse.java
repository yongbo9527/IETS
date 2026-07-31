package org.company.finance.tally.interfaces.rest.response;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-10 13:47
 *
 */
@Data
public class DynamicTableResponse {
    private LinkedList<TableDetailHeader> headers; // 表头
    private Page<LinkedHashMap<String, Object>> rows; // 行数据
}
