package org.company.finance.domain.repository;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;

import java.util.List;
import java.util.Optional;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-09 17:42
 *
 */
public interface DailyExpenseRecordRepository {
    void save(DailyExpenseRecordEntity record);
    void update(DailyExpenseRecordEntity record);
    void deleteById(Long id, Long userId); // 必须带 userId 防越权
    Optional<DailyExpenseRecordEntity> findByIdAndUserId(Long id, Long userId);

    // 查询：按日期范围查用户的所有记录（用于聚合）
    Page<DailyExpenseRecordEntity> findByUserIdAndDateRange(Long userId, QueryBalanceVO vo);

    Page<String> findDistinctDates(QueryBalanceVO vo, long current, long size);
    List<DailyExpenseRecordEntity> findByDates(List<String> dates);

    Page<DailyExpenseRecordEntity> findPage(QueryBalanceVO vo, long current, long size);

    List<DailyExpenseRecordEntity> findList(List<String> expenseDates);
}
