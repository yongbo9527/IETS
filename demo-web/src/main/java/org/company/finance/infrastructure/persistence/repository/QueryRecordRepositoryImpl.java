package org.company.finance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.domain.repository.QueryRecordRepository;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.company.finance.infrastructure.persistence.mapper.DailyExpenseRecordMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 16:58
 *
 */
@Repository
@RequiredArgsConstructor
public class QueryRecordRepositoryImpl implements QueryRecordRepository {

    private final DailyExpenseRecordMapper mapper;

    @Override
    public Page<DailyExpenseRecordEntity> findByUserIdAndDateRange(Long userId, QueryBalanceVO vo) {
        Page<QueryBalanceVO> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        Page<DailyExpenseRecordEntity> pageList = mapper.selectExpenseMetaData(page, vo);
        return pageList;
    }

    @Override
    public Page<DailyExpenseRecordEntity> findDistinctDates(QueryBalanceVO vo, Integer current, Integer size) {
        LambdaQueryWrapper<DailyExpenseRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DailyExpenseRecordEntity::getExpenseDate)
                .eq(DailyExpenseRecordEntity::getDelFlag, 0)
                .ge(vo.getStartDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getStartDate())
                .le(vo.getEndDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getEndDate())
                .groupBy(DailyExpenseRecordEntity::getExpenseDate)
                .orderByDesc(DailyExpenseRecordEntity::getExpenseDate);

        Page<DailyExpenseRecordEntity> result = mapper.selectPage(new Page<>(current, size), wrapper);
        return result;
    }

    @Override
    public List<DailyExpenseRecordEntity> findByDates(List<String> dates) {
        if (dates.isEmpty()) {
            return Collections.emptyList();
        }

        List<DailyExpenseRecordEntity> entities = mapper.selectList(
                new LambdaQueryWrapper<DailyExpenseRecordEntity>()
                        .eq(DailyExpenseRecordEntity::getDelFlag, 0)
                        .in(DailyExpenseRecordEntity::getExpenseDate, dates)
        );

        return entities;
    }

    @Override
    public Page<DailyExpenseRecordEntity> findPage(QueryBalanceVO vo, long current, long size) {
        LambdaQueryWrapper<DailyExpenseRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DailyExpenseRecordEntity::getExpenseDate)
                .eq(DailyExpenseRecordEntity::getDelFlag, 0)
                .groupBy(DailyExpenseRecordEntity::getExpenseDate)
                .ge(vo.getStartDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getStartDate())
                .le(vo.getEndDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getEndDate())
                .orderByDesc(DailyExpenseRecordEntity::getExpenseDate);
        Page<DailyExpenseRecordEntity> page = mapper.selectPage(new Page<>(current, size), wrapper);
        return page;
    }

    @Override
    public List<DailyExpenseRecordEntity> findList(List<String> expenseDates) {
        LambdaQueryWrapper<DailyExpenseRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DailyExpenseRecordEntity::getDelFlag, 0)
                .in(DailyExpenseRecordEntity::getExpenseDate, expenseDates);
        return mapper.selectList(wrapper);
    }

    @Override
    public void saveBatch(List<DailyExpenseRecordEntity> records) {
        for (DailyExpenseRecordEntity record : records) {
            mapper.insert(record);
        }
    }
}
