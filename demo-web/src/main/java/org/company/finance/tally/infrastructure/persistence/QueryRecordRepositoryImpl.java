package org.company.finance.tally.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.company.finance.tally.application.query.model.ExpenseRecordQueryModel;
import org.company.finance.tally.domain.repository.QueryRecordRepository;
import org.company.finance.tally.interfaces.rest.request.QueryBalanceVO;
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

    private final ExpenseRecordMapper mapper;

    @Override
    public Page<ExpenseRecordQueryModel> findByUserIdAndDateRange(Long userId, QueryBalanceVO vo) {
        Page<QueryBalanceVO> page = new Page<>(vo.getCurrent(), vo.getPageSize());
        Page<ExpenseRecordDO> pageList = mapper.selectExpenseMetaData(page, vo);
        return toQueryPage(pageList);
    }

    @Override
    public Page<ExpenseRecordQueryModel> findDistinctDates(QueryBalanceVO vo, Integer current, Integer size) {
        LambdaQueryWrapper<ExpenseRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ExpenseRecordDO::getExpenseDate)
                .eq(ExpenseRecordDO::getDelFlag, 0)
                .ge(vo.getStartDate() != null, ExpenseRecordDO::getExpenseDate, vo.getStartDate())
                .le(vo.getEndDate() != null, ExpenseRecordDO::getExpenseDate, vo.getEndDate())
                .groupBy(ExpenseRecordDO::getExpenseDate)
                .orderByDesc(ExpenseRecordDO::getExpenseDate);

        Page<ExpenseRecordDO> result = mapper.selectPage(new Page<>(current, size), wrapper);
        return toQueryPage(result);
    }

    @Override
    public List<ExpenseRecordQueryModel> findByDates(List<String> dates) {
        if (dates.isEmpty()) {
            return Collections.emptyList();
        }

        List<ExpenseRecordDO> entities = mapper.selectList(
                new LambdaQueryWrapper<ExpenseRecordDO>()
                        .eq(ExpenseRecordDO::getDelFlag, 0)
                        .in(ExpenseRecordDO::getExpenseDate, dates)
        );

        return entities.stream().map(this::toQueryModel).collect(java.util.stream.Collectors.toList());
    }

    @Override
    public Page<ExpenseRecordQueryModel> findPage(QueryBalanceVO vo, long current, long size) {
        LambdaQueryWrapper<ExpenseRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(ExpenseRecordDO::getExpenseDate)
                .eq(ExpenseRecordDO::getDelFlag, 0)
                .groupBy(ExpenseRecordDO::getExpenseDate)
                .ge(vo.getStartDate() != null, ExpenseRecordDO::getExpenseDate, vo.getStartDate())
                .le(vo.getEndDate() != null, ExpenseRecordDO::getExpenseDate, vo.getEndDate())
                .orderByDesc(ExpenseRecordDO::getExpenseDate);
        Page<ExpenseRecordDO> page = mapper.selectPage(new Page<>(current, size), wrapper);
        return toQueryPage(page);
    }

    @Override
    public List<ExpenseRecordQueryModel> findList(List<String> expenseDates) {
        LambdaQueryWrapper<ExpenseRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ExpenseRecordDO::getDelFlag, 0)
                .in(ExpenseRecordDO::getExpenseDate, expenseDates);
        return mapper.selectList(wrapper).stream().map(this::toQueryModel).collect(java.util.stream.Collectors.toList());
    }

    private Page<ExpenseRecordQueryModel> toQueryPage(Page<ExpenseRecordDO> page) {
        Page<ExpenseRecordQueryModel> responsePage = new Page<>(page.getCurrent(), page.getSize());
        responsePage.setTotal(page.getTotal());
        responsePage.setRecords(page.getRecords().stream().map(this::toQueryModel).collect(java.util.stream.Collectors.toList()));
        return responsePage;
    }

    private ExpenseRecordQueryModel toQueryModel(ExpenseRecordDO entity) {
        ExpenseRecordQueryModel model = new ExpenseRecordQueryModel();
        model.setId(entity.getId());
        model.setCategoryId(entity.getCategoryId());
        model.setCategoryName(entity.getCategoryName());
        model.setExpenseAmount(entity.getExpenseAmount());
        model.setExpenseDate(entity.getExpenseDate());
        model.setExpenseType(entity.getExpenseType());
        model.setRemark(entity.getRemark());
        model.setCreateTime(entity.getCreateTime());
        model.setUpdateTime(entity.getUpdateTime());
        return model;
    }
}
