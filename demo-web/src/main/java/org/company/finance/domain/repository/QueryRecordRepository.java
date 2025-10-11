package org.company.finance.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 16:55
 *
 */
public interface QueryRecordRepository {
    Page<DailyExpenseRecordEntity> findByUserIdAndDateRange(Long userId, QueryBalanceVO vo);

    Page<DailyExpenseRecordEntity> findDistinctDates(QueryBalanceVO vo, Integer current, Integer pageSize);

    List<DailyExpenseRecordEntity> findByDates(List<String> records);

    Page<DailyExpenseRecordEntity> findPage(QueryBalanceVO vo, long current, long size);

    List<DailyExpenseRecordEntity> findList(List<String> expenseDates);
}
