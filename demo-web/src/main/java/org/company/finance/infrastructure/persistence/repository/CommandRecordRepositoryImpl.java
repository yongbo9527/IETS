package org.company.finance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.company.finance.domain.repository.CommandRecordRepository;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.company.finance.infrastructure.persistence.mapper.DailyExpenseRecordMapper;
import org.springframework.stereotype.Repository;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 16:56
 *
 */
@Repository
@RequiredArgsConstructor
public class CommandRecordRepositoryImpl implements CommandRecordRepository {

    private final DailyExpenseRecordMapper mapper;
    @Override
    public void save(DailyExpenseRecordEntity entity) {
        mapper.insert(entity);
    }

    @Override
    public void updateRecord(DailyExpenseRecordEntity entity) {
        LambdaUpdateWrapper<DailyExpenseRecordEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DailyExpenseRecordEntity::getExpenseAmount, entity.getExpenseAmount())
                .set(DailyExpenseRecordEntity::getRemark, entity.getRemark())
                .eq(DailyExpenseRecordEntity::getId, entity.getId());
        mapper.update(null, updateWrapper);
    }

    @Override
    public void deleteById(Long id) {
        LambdaUpdateWrapper<DailyExpenseRecordEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DailyExpenseRecordEntity::getDelFlag, 1)
                .eq(DailyExpenseRecordEntity::getId, id);
        mapper.update(null, updateWrapper);
    }
}
