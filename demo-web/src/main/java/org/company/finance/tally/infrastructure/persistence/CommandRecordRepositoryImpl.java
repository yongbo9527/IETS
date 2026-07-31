package org.company.finance.tally.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.company.finance.tally.domain.model.ExpenseRecord;
import org.company.finance.tally.domain.repository.CommandRecordRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 16:56
 *
 */
@Repository
@RequiredArgsConstructor
public class CommandRecordRepositoryImpl implements CommandRecordRepository {

    private final ExpenseRecordMapper mapper;
    @Override
    public void save(ExpenseRecord expenseRecord) {
        mapper.insert(toEntity(expenseRecord));
    }

    @Override
    public void update(ExpenseRecord expenseRecord) {
        ExpenseRecordDO entity = toEntity(expenseRecord);
        LambdaUpdateWrapper<ExpenseRecordDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ExpenseRecordDO::getExpenseAmount, entity.getExpenseAmount())
                .set(ExpenseRecordDO::getRemark, entity.getRemark())
                .eq(ExpenseRecordDO::getId, entity.getId());
        mapper.update(null, updateWrapper);
    }

    @Override
    public void saveBatch(List<ExpenseRecord> expenseRecords) {
        for (ExpenseRecord expenseRecord : expenseRecords) {
            mapper.insert(toEntity(expenseRecord));
        }
    }

    @Override
    public void deleteById(Long id) {
        LambdaUpdateWrapper<ExpenseRecordDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(ExpenseRecordDO::getDelFlag, 1)
                .eq(ExpenseRecordDO::getId, id);
        mapper.update(null, updateWrapper);
    }

    private ExpenseRecordDO toEntity(ExpenseRecord expenseRecord) {
        ExpenseRecordDO entity = new ExpenseRecordDO();
        entity.setId(expenseRecord.getId());
        entity.setCategoryId(expenseRecord.getCategoryId());
        entity.setExpenseAmount(expenseRecord.getAmount().toBigDecimal());
        entity.setExpenseDate(expenseRecord.getExpenseDate().getValue());
        entity.setExpenseType(expenseRecord.getExpenseType().getCode());
        entity.setRemark(expenseRecord.getRemark().getValue());
        entity.setDelFlag(expenseRecord.isDeleted() ? 1 : 0);
        return entity;
    }
}
