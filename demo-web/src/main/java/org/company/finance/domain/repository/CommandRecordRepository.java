package org.company.finance.domain.repository;

import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 16:54
 *
 */
public interface CommandRecordRepository {
    void save(DailyExpenseRecordEntity entity);

    void updateRecord(DailyExpenseRecordEntity entity);

    void deleteById(Long id);
}
