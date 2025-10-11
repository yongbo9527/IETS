package org.company.finance.application.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.balance.RecordVO;
import org.company.finance.domain.repository.DailyExpenseRecordRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-10 18:10
 *
 */
@Service
@RequiredArgsConstructor
public class ExpenseQueryService {

    private final DailyExpenseRecordRepository dailyExpenseRecordRepository;

    public Page<LinkedHashMap<String, Object>> queryGroupedRecords(QueryBalanceVO vo, List<CategoryEntity> categories) {

        // 1. 先查出所有涉及的日期（分页）
        Page<String> datePage = dailyExpenseRecordRepository.findDistinctDates(vo, vo.getCurrent(), vo.getPageSize());
        if (datePage.getRecords().isEmpty()) {
            return new Page<>(datePage.getCurrent(), datePage.getSize());
        }

        // 2. 查出这些日期下的所有记录
        List<DailyExpenseRecordEntity> records = dailyExpenseRecordRepository.findByDates(datePage.getRecords());

        // 3. 按 categoryId + expenseDate 分组并合并金额
        Map<String, Map<String, RecordVO>> groupedData = records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getExpenseDate(),  // 外层：日期
                        Collectors.groupingBy(
                                r -> r.getCategoryId().toString(),  // 内层：分类ID
                                Collectors.reducing(
                                        null,
                                        record -> {
                                            RecordVO itemVo = new RecordVO();
                                            itemVo.setExpenseAmount(record.getExpenseAmount());
                                            itemVo.setRemark(record.getRemark());
                                            return itemVo;
                                        },
                                        (a, b) -> {
                                            if (a == null) return b;
                                            a.setExpenseAmount(a.getExpenseAmount().add(b.getExpenseAmount()));
                                            a.setRemark(a.getRemark() + " → " + b.getRemark());
                                            return a;
                                        }
                                )
                        )
                ));

        // 4. 构造表格行
        List<LinkedHashMap<String, Object>> tableRows = datePage.getRecords().stream()
                .map(date -> {
                    LinkedHashMap<String, Object> row = new LinkedHashMap<>();
                    row.put("expenseDate", date);
                    Map<String, RecordVO> categoryData = groupedData.getOrDefault(date, Collections.emptyMap());
                    categories.forEach(c -> row.put(c.getId().toString(), categoryData.get(c.getId().toString())));
                    return row;
                })
                .collect(Collectors.toList());

        Page<LinkedHashMap<String, Object>> result = new Page<>();
        result.setCurrent(datePage.getCurrent());
        result.setSize(datePage.getSize());
        result.setTotal(datePage.getTotal());
        result.setRecords(tableRows);
        return result;
    }
}