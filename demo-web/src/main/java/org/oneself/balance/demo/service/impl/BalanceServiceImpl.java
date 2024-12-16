package org.oneself.balance.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.oneself.balance.demo.entity.CategoryEntity;
import org.oneself.balance.demo.entity.DailyExpenseRecordEntity;
import org.oneself.balance.demo.mapper.CategoryMapper;
import org.oneself.balance.demo.mapper.DailyExpenseRecordMapper;
import org.oneself.balance.demo.service.BalanceService;
import org.oneself.balance.demo.vo.TableDetailHeader;
import org.oneself.balance.demo.vo.balance.QueryBalanceVO;
import org.oneself.balance.demo.vo.balance.RecordVO;
import org.oneself.balance.demo.vo.catagory.DataCategoryVO;
import org.oneself.balance.demo.vo.response.DynamicTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        buildHeader(resultList,headers);
        dynamicTable.setHeaders(headers);

        // 2. 构建行数据
        Page<LinkedHashMap<String, Object>> page = new Page<>();

        LambdaQueryWrapper<DailyExpenseRecordEntity> recordWrapper = new LambdaQueryWrapper<>();
        recordWrapper.select(DailyExpenseRecordEntity::getExpenseDate)
                        .groupBy(DailyExpenseRecordEntity::getExpenseDate)
                        .eq(DailyExpenseRecordEntity::getDelFlag, 0)
                .orderByAsc(DailyExpenseRecordEntity::getExpenseDate);
        Page dateGroupList = dailyExpenseRecordMapper.selectPage(new Page(vo.getCurrent(), vo.getPageSize()), recordWrapper);
        long total = dateGroupList.getTotal();
        page.setTotal(total);
        if (total == 0) {
            dynamicTable.setRows(page);
            return R.ok(dynamicTable);
        }
        List<DailyExpenseRecordEntity> records = dateGroupList.getRecords();
        List<String> expenseDateStrList = records.stream().map(DailyExpenseRecordEntity::getExpenseDate).collect(Collectors.toList());

        LambdaQueryWrapper<DailyExpenseRecordEntity> resultQueryWrapper = new LambdaQueryWrapper<>();
        resultQueryWrapper.eq(DailyExpenseRecordEntity::getDelFlag, 0).
                in(DailyExpenseRecordEntity::getExpenseDate, expenseDateStrList);
        List<DailyExpenseRecordEntity> dateDataList = dailyExpenseRecordMapper.selectList(resultQueryWrapper);

        List<LinkedHashMap<String, Object>> tableRowList = Lists.newArrayListWithCapacity(expenseDateStrList.size());
        for (String expenseDate : expenseDateStrList) {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("expenseDate", expenseDate);
            for (DailyExpenseRecordEntity dailyExpenseRecordEntity : dateDataList) {
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
            return R.failed("请先添加分类");
        }
        List<CategoryEntity> allCategoryList = new ArrayList<>();
        List<Integer> parentIds = dataCategoryList.stream().map(DataCategoryVO::getParentId).filter(item -> item != 0).distinct().collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(parentIds)) {
            LambdaQueryWrapper<CategoryEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(CategoryEntity::getId, CategoryEntity::getCategoryName, CategoryEntity::getParentId).in(CategoryEntity::getId, parentIds);
            List<CategoryEntity> categoryEntities = categoryMapper.selectList(queryWrapper);
            allCategoryList.addAll(categoryEntities);
        }
        dataCategoryList.forEach(item -> {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(item.getCategoryId());
            categoryEntity.setCategoryName(item.getCategoryName());
            categoryEntity.setParentId(item.getParentId());
            allCategoryList.add(categoryEntity);
        });
        List<CategoryEntity> resultList = buildCategory(allCategoryList);

        buildHeader(resultList,headers);
        dynamicTable.setHeaders(headers);


        return R.ok(dynamicTable);
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
