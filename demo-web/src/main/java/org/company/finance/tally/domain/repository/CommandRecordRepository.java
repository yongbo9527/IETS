package org.company.finance.tally.domain.repository;

import org.company.finance.tally.domain.model.ExpenseRecord;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 16:54
 *
 */
public interface CommandRecordRepository {
    void save(ExpenseRecord expenseRecord);

    void update(ExpenseRecord expenseRecord);

    void saveBatch(List<ExpenseRecord> expenseRecords);

    void deleteById(Long id);
}
