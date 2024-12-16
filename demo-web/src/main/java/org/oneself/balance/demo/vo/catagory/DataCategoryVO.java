package org.oneself.balance.demo.vo.catagory;

import lombok.Data;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-16 17:11
 *
 */
@Data
public class DataCategoryVO {

    private Integer categoryId;

    private String categoryName;

    private Integer parentId;
}
