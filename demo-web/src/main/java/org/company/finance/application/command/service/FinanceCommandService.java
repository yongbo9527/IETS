package org.company.finance.application.command.service;

import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.request.CreateExpenseRecordRequest;
import org.company.finance.application.vo.request.UpdateExpenseRecordRequest;
import org.company.finance.domain.model.Category;
import org.company.finance.domain.model.ExpenseDate;
import org.company.finance.domain.model.ExpenseRecord;
import org.company.finance.domain.model.ExpenseType;
import org.company.finance.domain.model.Money;
import org.company.finance.domain.model.Remark;
import org.company.finance.domain.repository.QueryCategoryRepository;
import org.company.finance.domain.repository.CommandRecordRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
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
    private final QueryCategoryRepository queryCategoryRepository;

    public void saveRecord(CreateExpenseRecordRequest request) {
        Category category = loadCategory(request.getCategoryId());
        ExpenseRecord.create(
                null,
                category,
                Money.of(request.getExpenseAmount()),
                ExpenseType.ofCode(request.getExpenseType()),
                ExpenseDate.of(request.getExpenseDate()),
                Remark.of(request.getRemark())
        );

        commandRecordRepository.save(toEntity(request));
    }

    public void updateRecord(UpdateExpenseRecordRequest request) {
        Category category = loadCategory(request.getCategoryId());
        ExpenseRecord expenseRecord = ExpenseRecord.create(
                null,
                category,
                Money.of(request.getExpenseAmount()),
                ExpenseType.ofCode(request.getExpenseType()),
                ExpenseDate.of(request.getExpenseDate()),
                Remark.of(request.getRemark())
        );
        expenseRecord.update(
                category,
                Money.of(request.getExpenseAmount()),
                ExpenseType.ofCode(request.getExpenseType()),
                ExpenseDate.of(request.getExpenseDate()),
                Remark.of(request.getRemark())
        );

        commandRecordRepository.update(toEntity(request));
    }

    public void deleteRecord(Long id) {
        commandRecordRepository.deleteById(id);
    }

    private Category loadCategory(Integer categoryId) {
        CategoryEntity categoryEntity = queryCategoryRepository.findById(categoryId);
        if (categoryEntity == null) {
            throw new IllegalArgumentException("类目不存在");
        }
        return Category.restore(
                categoryEntity.getId(),
                categoryEntity.getCategoryName(),
                categoryEntity.getParentId(),
                ExpenseType.ofCode(categoryEntity.getExpenseType()),
                Integer.valueOf(1).equals(categoryEntity.getDelFlag())
        );
    }

    private DailyExpenseRecordEntity toEntity(CreateExpenseRecordRequest request) {
        DailyExpenseRecordEntity entity = new DailyExpenseRecordEntity();
        entity.setCategoryId(request.getCategoryId());
        entity.setExpenseAmount(request.getExpenseAmount());
        entity.setExpenseDate(request.getExpenseDate());
        entity.setExpenseType(request.getExpenseType());
        entity.setRemark(request.getRemark());
        return entity;
    }

    private DailyExpenseRecordEntity toEntity(UpdateExpenseRecordRequest request) {
        DailyExpenseRecordEntity entity = toEntity((CreateExpenseRecordRequest) request);
        entity.setId(request.getId());
        return entity;
    }
}
