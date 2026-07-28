package org.company.finance.category.domain.model;

import org.company.finance.tally.domain.model.ExpenseType;

/**
 * 类目领域模型
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
public class Category {

    private final Integer id;
    private final String categoryName;
    private final Integer parentId;
    private final ExpenseType expenseType;
    private final boolean deleted;

    private Category(Integer id, String categoryName, Integer parentId, ExpenseType expenseType, boolean deleted) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            throw new IllegalArgumentException("类目名称不能为空");
        }
        if (parentId == null || parentId < 0) {
            throw new IllegalArgumentException("父级类目ID不能小于0");
        }
        this.id = id;
        this.categoryName = categoryName;
        this.parentId = parentId;
        this.expenseType = expenseType;
        this.deleted = deleted;
    }

    public static Category restore(Integer id, String categoryName, Integer parentId, ExpenseType expenseType, boolean deleted) {
        return new Category(id, categoryName, parentId, expenseType, deleted);
    }

    public void validateFor(ExpenseType actualExpenseType) {
        if (deleted) {
            throw new IllegalArgumentException("类目已删除，不能用于记账");
        }
        if (expenseType != actualExpenseType) {
            throw new IllegalArgumentException("类目与收支类型不匹配");
        }
    }

    public Integer getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
