package org.company.finance.tally.domain.model;

import org.company.finance.category.domain.model.Category;

/**
 * 收支记录聚合根
 *
 * @author Ron Yu
 * @date 2026-07-24
 */
public class ExpenseRecord {

    private Integer id;
    private final Integer userId;
    private Integer categoryId;
    private Money amount;
    private ExpenseType expenseType;
    private ExpenseDate expenseDate;
    private Remark remark;
    private boolean deleted;

    private ExpenseRecord(Integer userId, Integer categoryId, Money amount, ExpenseType expenseType,
                          ExpenseDate expenseDate, Remark remark) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.expenseType = expenseType;
        this.expenseDate = expenseDate;
        this.remark = remark;
        this.deleted = false;
    }

    public static ExpenseRecord create(Integer userId, Category category, Money amount,
                                       ExpenseType expenseType, ExpenseDate expenseDate, Remark remark) {
        category.validateFor(expenseType);
        return new ExpenseRecord(userId, category.getId(), amount, expenseType, expenseDate, remark);
    }

    public static ExpenseRecord restore(Integer id, Integer userId, Integer categoryId, Money amount,
                                        ExpenseType expenseType, ExpenseDate expenseDate, Remark remark,
                                        boolean deleted) {
        ExpenseRecord expenseRecord = new ExpenseRecord(userId, categoryId, amount, expenseType, expenseDate, remark);
        expenseRecord.id = id;
        expenseRecord.deleted = deleted;
        return expenseRecord;
    }

    public void update(Category category, Money amount, ExpenseType expenseType,
                       ExpenseDate expenseDate, Remark remark) {
        ensureActive();
        category.validateFor(expenseType);
        this.categoryId = category.getId();
        this.amount = amount;
        this.expenseType = expenseType;
        this.expenseDate = expenseDate;
        this.remark = remark;
    }

    public void delete() {
        ensureActive();
        this.deleted = true;
    }

    private void ensureActive() {
        if (deleted) {
            throw new IllegalStateException("已删除的收支记录不能修改");
        }
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Money getAmount() {
        return amount;
    }

    public ExpenseType getExpenseType() {
        return expenseType;
    }

    public ExpenseDate getExpenseDate() {
        return expenseDate;
    }

    public Remark getRemark() {
        return remark;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
