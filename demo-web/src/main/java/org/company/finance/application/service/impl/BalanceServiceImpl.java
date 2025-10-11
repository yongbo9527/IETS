package org.company.finance.application.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.company.finance.application.service.BalanceService;
import org.company.finance.application.vo.TableDetailHeader;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.balance.RecordVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.application.vo.response.DynamicTableResponse;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.company.finance.infrastructure.persistence.mapper.CategoryMapper;
import org.company.finance.infrastructure.persistence.mapper.DailyExpenseRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Ron Yu
 * @Create: 2024-08-30 10:36
 */
@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    private DailyExpenseRecordMapper dailyExpenseRecordMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void addBalance(DailyExpenseRecordEntity entity) {
        dailyExpenseRecordMapper.insert(entity);
    }

    @Override
    public R queryBalance(QueryBalanceVO vo) {
        DynamicTableResponse dynamicTable = new DynamicTableResponse();

        // 表格头信息
        LinkedList<TableDetailHeader> headers = new LinkedList<>();
        headers.add(new TableDetailHeader("expenseDate", "日期", null));

        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEntity::getDelFlag, 0)
                .orderByAsc(CategoryEntity::getId);
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(categoryEntities)) {
            return R.failed("请先添加分类");
        }
        List<CategoryEntity> resultList = buildCategory(categoryEntities);

        buildHeader(resultList, headers);
        dynamicTable.setHeaders(headers);

        // 2. 构建行数据
        R dynamicTable1 = getDynamicTableResponseR(vo, dynamicTable);

        return dynamicTable1;
    }

    private R getDynamicTableResponseR(QueryBalanceVO vo, DynamicTableResponse dynamicTable) {
        Page<LinkedHashMap<String, Object>> page = new Page<>();

        LambdaQueryWrapper<DailyExpenseRecordEntity> recordWrapper = new LambdaQueryWrapper<>();
        recordWrapper.select(DailyExpenseRecordEntity::getExpenseDate)
                .groupBy(DailyExpenseRecordEntity::getExpenseDate)
                .eq(DailyExpenseRecordEntity::getDelFlag, 0)
                .ge(vo.getStartDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getStartDate()) // 动态添加条件
                .le(vo.getEndDate() != null, DailyExpenseRecordEntity::getExpenseDate, vo.getEndDate())   // 动态添加条件
                .orderByDesc(DailyExpenseRecordEntity::getExpenseDate);
        Page dateGroupList = dailyExpenseRecordMapper.selectPage(new Page(vo.getCurrent(), vo.getPageSize()), recordWrapper);
        int size = dateGroupList.getRecords().size();
        long total = dateGroupList.getTotal();
        page.setTotal(total);
        if (size == 0) {
            dynamicTable.setRows(page);
            return R.ok(dynamicTable);
        }
        List<DailyExpenseRecordEntity> records = dateGroupList.getRecords();
        List<String> expenseDateStrList = records.stream().map(DailyExpenseRecordEntity::getExpenseDate).collect(Collectors.toList());

        LambdaQueryWrapper<DailyExpenseRecordEntity> resultQueryWrapper = new LambdaQueryWrapper<>();
        resultQueryWrapper.eq(DailyExpenseRecordEntity::getDelFlag, 0).
                in(DailyExpenseRecordEntity::getExpenseDate, expenseDateStrList);
        List<DailyExpenseRecordEntity> dateDataList = dailyExpenseRecordMapper.selectList(resultQueryWrapper);

        // 根据 categoryId, expenseDate, delFlag 分组，并将 expenseAmount 累加
        List<DailyExpenseRecordEntity> result = dateDataList.stream()
                .collect(Collectors.groupingBy(
                        // 分组依据：使用一个复合键
                        record -> Arrays.asList(record.getCategoryId(), record.getExpenseDate(), record.getDelFlag()),
                        // 累加 expenseAmount
                        Collectors.reducing(
                                null,
                                record -> record,
                                (a, b) -> {
                                    if (a == null) return b;
                                    BigDecimal expenseAmount = a.getExpenseAmount();
                                    a.setExpenseAmount(a.getExpenseAmount().add(b.getExpenseAmount()));
                                    a.setRemark(a.getRemark() + "(" + expenseAmount + ")" + "->" + b.getRemark() + "(" + b.getExpenseAmount() + ")");
                                    return a;
                                }
                        )
                ))
                .values().stream()
                .filter(Objects::nonNull) // 去除可能为 null 的结果
                .collect(Collectors.toList());
        List<LinkedHashMap<String, Object>> tableRowList = Lists.newArrayListWithCapacity(expenseDateStrList.size());
        for (String expenseDate : expenseDateStrList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("expenseDate", expenseDate);
            for (DailyExpenseRecordEntity dailyExpenseRecordEntity : result) {
                if (dailyExpenseRecordEntity.getExpenseDate().equals(expenseDate)) {
                    RecordVO recordVO = new RecordVO();
                    recordVO.setExpenseAmount(dailyExpenseRecordEntity.getExpenseAmount());
                    recordVO.setRemark(dailyExpenseRecordEntity.getRemark());
                    map.put(dailyExpenseRecordEntity.getCategoryId().toString(), recordVO);
                }
            }
            tableRowList.add(map);
        }
        page.setRecords(tableRowList);

        dynamicTable.setRows(page);
        return R.ok(dynamicTable);
    }

    @Override
    public R queryTreeCategoryList() {
        LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(CategoryEntity::getDelFlag, 0)
                .orderByAsc(CategoryEntity::getId);
        List<CategoryEntity> categoryEntities = categoryMapper.selectList(queryWrapper);
        List<CategoryEntity> parentCategory = categoryEntities.stream().filter(item -> item.getParentId() == 0).collect(Collectors.toList());
        List<CategoryEntity> childCategory = categoryEntities.stream().filter(item -> item.getParentId() != 0).collect(Collectors.toList());
        List<CategoryEntity> resultCategory = parentCategory.stream().map(item -> {
            item.setList(childCategory.stream().filter(childItem -> childItem.getParentId().equals(item.getId())).collect(Collectors.toList()));
            return item;
        }).collect(Collectors.toList());


        return R.ok(resultCategory);
    }

    @Override
    public R queryBalanceShortHead(QueryBalanceVO vo) {
        DynamicTableResponse dynamicTable = new DynamicTableResponse();

        // 表格头信息
        LinkedList<TableDetailHeader> headers = new LinkedList<>();
        headers.add(new TableDetailHeader("expenseDate", "日期", null));
        // 查询数据，根据数据的类目解析表格头
        List<DataCategoryVO> dataCategoryList = categoryMapper.selectDataCategory(vo);
        if (CollectionUtils.isEmpty(dataCategoryList)) {
            dynamicTable.setHeaders(null);
            Page<LinkedHashMap<String, Object>> page = new Page<>();
            page.setTotal(0);
            dynamicTable.setRows(page);
            return R.ok(dynamicTable);
        }
        List<DataCategoryVO> dataDistinctCategoryList = dataCategoryList.stream().distinct().collect(Collectors.toList());
        List<CategoryEntity> allCategoryList = new ArrayList<>();
        List<Integer> parentIds = dataDistinctCategoryList.stream().map(DataCategoryVO::getParentId).filter(item -> item != 0).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(parentIds)) {
            LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(CategoryEntity::getId, CategoryEntity::getCategoryName, CategoryEntity::getParentId).in(CategoryEntity::getId, parentIds);
            List<CategoryEntity> categoryEntities = categoryMapper.selectList(queryWrapper);
            allCategoryList.addAll(categoryEntities);
        }
        dataDistinctCategoryList.forEach(item -> {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(item.getCategoryId());
            categoryEntity.setCategoryName(item.getCategoryName());
            categoryEntity.setParentId(item.getParentId());
            allCategoryList.add(categoryEntity);
        });
        List<CategoryEntity> resultList = buildCategory(allCategoryList);

        buildHeader(resultList, headers);
        dynamicTable.setHeaders(headers);

        // 2. 构建行数据
        R<DynamicTableResponse> dynamicTable1 = getDynamicTableResponseR(vo, dynamicTable);

        return dynamicTable1;
    }

    @Override
    public R queryDailyExpenseRecordBalance(QueryBalanceVO vo) {
        Page<QueryBalanceVO> page = new Page<>(vo.getCurrent(), vo.getPageSize());
            Page<DailyExpenseRecordEntity> pageList = dailyExpenseRecordMapper.selectExpenseMetaData(page, vo);
        return R.ok(pageList);
    }

    @Override
    public void updateBalance(DailyExpenseRecordEntity entity) {

        LambdaUpdateWrapper<DailyExpenseRecordEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DailyExpenseRecordEntity::getExpenseAmount, entity.getExpenseAmount())
                .set(DailyExpenseRecordEntity::getRemark, entity.getRemark())
                .eq(DailyExpenseRecordEntity::getId, entity.getId());
        dailyExpenseRecordMapper.update(null, updateWrapper);

    }

    @Override
    public void deleteBalance(Integer id) {

        LambdaUpdateWrapper<DailyExpenseRecordEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(DailyExpenseRecordEntity::getDelFlag, 1)
                .eq(DailyExpenseRecordEntity::getId, id);
        dailyExpenseRecordMapper.update(null, updateWrapper);

    }

    /**
     * 构建表头
     * @param resultList
     * @param headers
     */
    private void buildHeader(List<CategoryEntity> resultList, LinkedList<TableDetailHeader> headers) {
        for (CategoryEntity categoryEntity : resultList) {
            List<CategoryEntity> childrenList = categoryEntity.getList();
            LinkedList<TableDetailHeader> linkedList = Lists.newLinkedList();
            TableDetailHeader parentTableDetailHeader = new TableDetailHeader();
            if (CollectionUtils.isNotEmpty(childrenList)) {
                for (CategoryEntity item : childrenList) {
                    TableDetailHeader childTableDetailHeader = new TableDetailHeader();
                    childTableDetailHeader.setDataIndex(item.getId().toString());
                    childTableDetailHeader.setTitle(item.getCategoryName());
                    linkedList.add(childTableDetailHeader);
                }
            }
            parentTableDetailHeader.setDataIndex(categoryEntity.getId().toString());
            parentTableDetailHeader.setTitle(categoryEntity.getCategoryName());
            parentTableDetailHeader.setChildren(linkedList);
            headers.add(parentTableDetailHeader);
        }
    }

    /**
     * 封装category数据
     * @param categoryEntities
     * @return
     */
    private List<CategoryEntity> buildCategory(List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> parentList = categoryEntities.stream()
                .filter(item -> item.getParentId() == 0)
                .collect(Collectors.toList());
        List<CategoryEntity> childList = categoryEntities.stream()
                .filter(item -> item.getParentId() != 0)
                .collect(Collectors.toList());
        for (CategoryEntity categoryEntity : parentList) {
            for (CategoryEntity child : childList) {
                if (categoryEntity.getId().equals(child.getParentId())) {
                    categoryEntity.getList().add(child);
                }
            }
        }
        return parentList;
    }


}
