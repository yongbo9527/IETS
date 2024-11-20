package org.oneself.balance.demo.service;

import org.oneself.balance.demo.entity.DailyExpenseRecordEntity;

/**
 * @Author: Ron Yu
 * @Create: 2024-08-30 10:36
 */
public interface BalanceService {
    /**
     * 新增收支记录
     * @param entity
     */
    void addBalance(DailyExpenseRecordEntity entity);
}
