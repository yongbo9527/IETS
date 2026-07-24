package org.company.finance.application.vo.response;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 类目树节点响应
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
@Data
public class CategoryTreeNodeResponse {

    private Integer id;

    private String categoryName;

    private String categoryIcon;

    private Integer parentId;

    private Integer expenseType;

    private String remark;

    private List<CategoryTreeNodeResponse> children = new ArrayList<>();
}
