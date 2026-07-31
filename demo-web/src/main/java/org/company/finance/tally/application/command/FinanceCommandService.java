package org.company.finance.tally.application.command;

import lombok.RequiredArgsConstructor;
import org.company.finance.category.application.query.model.CategorySnapshot;
import org.company.finance.category.domain.model.Category;
import org.company.finance.tally.domain.model.ExpenseDate;
import org.company.finance.tally.domain.model.ExpenseRecord;
import org.company.finance.tally.domain.model.ExpenseType;
import org.company.finance.tally.domain.model.Money;
import org.company.finance.tally.domain.model.Remark;
import org.company.finance.category.domain.repository.QueryCategoryRepository;
import org.company.finance.tally.domain.repository.CommandRecordRepository;
import org.company.finance.tally.interfaces.rest.request.CreateExpenseRecordRequest;
import org.company.finance.tally.interfaces.rest.request.UpdateExpenseRecordRequest;
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
        ExpenseRecord expenseRecord = ExpenseRecord.create(
                null,
                category,
                Money.of(request.getExpenseAmount()),
                ExpenseType.ofCode(request.getExpenseType()),
                ExpenseDate.of(request.getExpenseDate()),
                Remark.of(request.getRemark())
        );

        commandRecordRepository.save(expenseRecord);
    }

    public void updateRecord(UpdateExpenseRecordRequest request) {
        Category category = loadCategory(request.getCategoryId());
        ExpenseRecord expenseRecord = ExpenseRecord.restore(
                request.getId(),
                null,
                request.getCategoryId(),
                Money.of(request.getExpenseAmount()),
                ExpenseType.ofCode(request.getExpenseType()),
                ExpenseDate.of(request.getExpenseDate()),
                Remark.of(request.getRemark()),
                false
        );
        expenseRecord.update(
                category,
                Money.of(request.getExpenseAmount()),
                ExpenseType.ofCode(request.getExpenseType()),
                ExpenseDate.of(request.getExpenseDate()),
                Remark.of(request.getRemark())
        );

        commandRecordRepository.update(expenseRecord);
    }

    public void deleteRecord(Long id) {
        commandRecordRepository.deleteById(id);
    }

    private Category loadCategory(Integer categoryId) {
        CategorySnapshot categorySnapshot = queryCategoryRepository.findById(categoryId);
        if (categorySnapshot == null) {
            throw new IllegalArgumentException("类目不存在");
        }
        return Category.restore(
                categorySnapshot.getId(),
                categorySnapshot.getCategoryName(),
                categorySnapshot.getParentId(),
                ExpenseType.ofCode(categorySnapshot.getExpenseType()),
                Integer.valueOf(1).equals(categorySnapshot.getDelFlag())
        );
    }
}
