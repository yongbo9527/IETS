package org.company.finance.tally.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.company.finance.tally.application.query.model.ExpenseRecordQueryModel;
import org.company.finance.tally.interfaces.rest.request.QueryBalanceVO;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 16:55
 *
 */
public interface QueryRecordRepository {
    Page<ExpenseRecordQueryModel> findByUserIdAndDateRange(Long userId, QueryBalanceVO vo);

    Page<ExpenseRecordQueryModel> findDistinctDates(QueryBalanceVO vo, Integer current, Integer pageSize);

    List<ExpenseRecordQueryModel> findByDates(List<String> records);

    Page<ExpenseRecordQueryModel> findPage(QueryBalanceVO vo, long current, long size);

    List<ExpenseRecordQueryModel> findList(List<String> expenseDates);
}
