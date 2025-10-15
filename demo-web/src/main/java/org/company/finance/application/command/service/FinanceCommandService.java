package org.company.finance.application.command.service;

import lombok.RequiredArgsConstructor;
import org.company.finance.domain.repository.CommandRecordRepository;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.springframework.stereotype.Service;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 15:06
 *
 */
@Service
@RequiredArgsConstructor
public class FinanceCommandService {

    private final CommandRecordRepository commandRecordRepository;

    public void saveRecord(DailyExpenseRecordEntity entity) {
        commandRecordRepository.save(entity);
    }

    public void updateRecord(DailyExpenseRecordEntity entity) {
        commandRecordRepository.updateRecord(entity);
    }

    public void deleteRecord(Long id) {
        commandRecordRepository.deleteById(id);
    }
}
