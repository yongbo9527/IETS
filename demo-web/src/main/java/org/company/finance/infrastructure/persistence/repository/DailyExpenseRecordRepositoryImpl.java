package org.company.finance.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.domain.repository.DailyExpenseRecordRepository;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.company.finance.infrastructure.persistence.mapper.DailyExpenseRecordMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-09 17:42
 *
 */
@Repository
@RequiredArgsConstructor
public class DailyExpenseRecordRepositoryImpl implements DailyExpenseRecordRepository {

    private final DailyExpenseRecordMapper mapper;

    @Override
    public void save(DailyExpenseRecordEntity entity) {
        mapper.insert(entity);
    }

    @Override
    public void update(DailyExpenseRecordEntity entity) {
        LambdaUpdateWrapper<DailyExpenseRecordEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DailyExpenseRecordEntity::getExpenseAmount, entity.getExpenseAmount())
                .set(DailyExpenseRecordEntity::getRemark, entity.getRemark())
                .eq(DailyExpenseRecordEntity::getId, entity.getId());
        mapper.update(null, updateWrapper);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        LambdaUpdateWrapper<DailyExpenseRecordEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DailyExpenseRecordEntity::getDelFlag, 1)
                .eq(DailyExpenseRecordEntity::getId, id);
        mapper.update(null, updateWrapper);
    }

    @Override
    public Optional<DailyExpenseRecordEntity> findByIdAndUserId(Long id, Long userId) {
        return null;
    }

    @Override
    public Page<DailyExpenseRecordEntity> findByUserIdAndDateRange(Long userId, QueryBalanceVO vo) {
        Page<QueryBalanceVO> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        Page<DailyExpenseRecordEntity> pageList = mapper.selectExpenseMetaData(page, vo);
        return pageList;
    }

    @Override
    public Page<String> findDistinctDates(QueryBalanceVO vo, long current, long size) {
        Page<String> page = new Page<>(current, size);
        LambdaQueryWrapper<DailyExpenseRecordEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(DailyExpenseRecordEntity::getExpenseDate)
                .eq(DailyExpenseRecordEntity::getDelFlag, 0)
                .ge(vo.getStartDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getStartDate())
                .le(vo.getEndDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getEndDate())
                .groupBy(DailyExpenseRecordEntity::getExpenseDate)
                .orderByDesc(DailyExpenseRecordEntity::getExpenseDate);

        Page<DailyExpenseRecordEntity> result = mapper.selectPage(new Page<>(current, size), wrapper);
        page.setRecords(result.getRecords().stream()
                .map(DailyExpenseRecordEntity::getExpenseDate)
                .collect(Collectors.toList()));
        page.setTotal(result.getTotal());
        return page;
    }

    @Override
    public List<DailyExpenseRecordEntity> findByDates(List<String> dates) {
        if (dates.isEmpty()) return Collections.emptyList();

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

}
