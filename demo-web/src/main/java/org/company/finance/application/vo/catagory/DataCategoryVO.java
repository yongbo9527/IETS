package org.company.finance.application.vo.catagory;

import lombok.Data;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataCategoryVO that = (DataCategoryVO) o;
        return Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }
}
