package org.company.finance.application.query.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.company.finance.application.vo.TableDetailHeader;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.application.vo.balance.RecordVO;
import org.company.finance.application.vo.catagory.DataCategoryVO;
import org.company.finance.application.vo.response.DynamicTableResponse;
import org.company.finance.domain.repository.QueryCategoryRepository;
import org.company.finance.domain.repository.QueryRecordRepository;
import org.company.finance.infrastructure.persistence.entity.CategoryEntity;
import org.company.finance.infrastructure.persistence.entity.DailyExpenseRecordEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 15:09
 *
 */
@Service
@RequiredArgsConstructor
public class FinanceQueryService {

    private final QueryRecordRepository queryRecordRepository;

    private final QueryCategoryRepository queryCategoryRepository;
    public Page<DailyExpenseRecordEntity> listDailyRecords(QueryBalanceVO vo) {
        Page<DailyExpenseRecordEntity> expenseRecordEntityPage = queryRecordRepository.findByUserIdAndDateRange(null, vo);
        return expenseRecordEntityPage;
    }

    @Transactional(readOnly = true)
    public DynamicTableResponse getBalanceTable(QueryBalanceVO vo) {
        DynamicTableResponse response = new DynamicTableResponse();
        // 1. 校验分类是否存在
        List<CategoryEntity> categories = queryCategoryRepository.findActiveOrderByAsc();
        if (CollectionUtils.isEmpty(categories)) {
            return response;
        }

        // 2. 构建表头
        LinkedList<TableDetailHeader> headers = buildHeaders(categories);

        // 3. 查询并构建行数据
        Page<LinkedHashMap<String, Object>> rows = queryGroupedRecords(vo, categories);

        // 4. 组装响应
        response.setHeaders(headers);
        response.setRows(rows);

        return response;
    }

    public DynamicTableResponse getCompactBalanceTable(QueryBalanceVO vo) {
        // 初始化响应对象
        DynamicTableResponse dynamicTable = new DynamicTableResponse();

        // 1. 构建表头（基于类目结构）
        LinkedList<TableDetailHeader> headers = buildRecordTableHeaders(vo);
        dynamicTable.setHeaders(headers);

        // 2. 构建分页行数据
        Page<LinkedHashMap<String, Object>> rows = buildCompactBalanceTableRows(vo);
        dynamicTable.setRows(rows);

        return dynamicTable;
    }

    /**
     * 构建动态表头（支持父子类目嵌套）
     */
    private LinkedList<TableDetailHeader> buildRecordTableHeaders(QueryBalanceVO vo) {
        LinkedList<TableDetailHeader> headers = new LinkedList<>();
        headers.add(new TableDetailHeader("expenseDate", "日期", null));

        // 查询涉及的数据类目
        List<DataCategoryVO> dataCategoryList = queryCategoryRepository.findDataCategory(vo);
        if (CollectionUtils.isEmpty(dataCategoryList)) {
            return headers; // 无类目时只保留“日期”列
        }

        // 去重并补全父类目信息
        List<CategoryEntity> allCategories = enrichCategoryHierarchy(dataCategoryList);
        List<CategoryEntity> tree = buildCategoryTree(allCategories);

        // 转换为前端所需的表头结构
        buildHeader(headers, tree);
        return headers;
    }

    /**
     * 将类目树转换为 TableDetailHeader 结构
     */
    private void buildHeader(LinkedList<TableDetailHeader> headers, List<CategoryEntity> tree) {
        for (CategoryEntity parent : tree) {
            List<CategoryEntity> children = parent.getList();
            LinkedList<TableDetailHeader> childHeaders = new LinkedList<>();

            if (!children.isEmpty()) {
                for (CategoryEntity child : children) {
                    childHeaders.add(new TableDetailHeader(
                            child.getId().toString(),
                            child.getCategoryName(),
                            null
                    ));
                }
            }

            TableDetailHeader parentHeader = new TableDetailHeader(
                    parent.getId().toString(),
                    parent.getCategoryName(),
                    childHeaders.isEmpty() ? null : childHeaders
            );
            headers.add(parentHeader);
        }
    }


    /**
     * 构建类目树结构（父 -> 子）
     */
    private List<CategoryEntity> buildCategoryTree(List<CategoryEntity> categories) {
        Map<Integer, CategoryEntity> map = new HashMap<>();
        List<CategoryEntity> parents = new ArrayList<>();

        // 构建 ID -> Entity 映射
        for (CategoryEntity category : categories) {
            map.put(category.getId(), category);
            category.setList(new ArrayList<>()); // 确保 list 已初始化
        }

        // 构建父子关系
        for (CategoryEntity category : categories) {
            Integer parentId = category.getParentId();
            if (parentId == 0) {
                parents.add(category); // 根节点
            } else {
                CategoryEntity parent = map.get(parentId);
                if (parent != null) {
                    parent.getList().add(category);
                }
            }
        }

        return parents;
    }
    private LinkedList<TableDetailHeader> buildHeaders(List<CategoryEntity> categories) {
        LinkedList<TableDetailHeader> headers = new LinkedList<>();
        headers.add(new TableDetailHeader("expenseDate", "日期", null));
        categories.forEach(c -> headers.add(new TableDetailHeader(c.getId().toString(), c.getCategoryName(), null)));
        return headers;
    }

    /**
     * 构建分页表格行数据
     */
    private Page<LinkedHashMap<String, Object>> buildCompactBalanceTableRows(QueryBalanceVO vo) {
        Page<LinkedHashMap<String, Object>> page = new Page<>(vo.getCurrent(), vo.getPageSize());

        // 1. 查询所有不重复的 expenseDate（用于分页）
        Page<DailyExpenseRecordEntity> datePage = queryRecordRepository.findPage(vo, page.getCurrent(), page.getSize());
        List<String> expenseDates = datePage.getRecords().stream()
                .map(DailyExpenseRecordEntity::getExpenseDate)
                .collect(Collectors.toList());

        long total = datePage.getTotal();
        page.setTotal(total);

        if (expenseDates.isEmpty()) {
            page.setRecords(Collections.emptyList());
            return page;
        }

        // 2. 查询这些日期下的所有明细记录
        List<DailyExpenseRecordEntity> allRecords = queryRecordRepository.findList(expenseDates);

        // 3. 按 categoryId + expenseDate 分组并累加金额
        Map<String, DailyExpenseRecordEntity> groupedMap = allRecords.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getCategoryId() + "_" + r.getExpenseDate(),
                        Collectors.reducing(
                                null,
                                record -> record,
                                (a, b) -> {
                                    if (a == null) return b;
                                    a.setExpenseAmount(a.getExpenseAmount().add(b.getExpenseAmount()));
                                    a.setRemark(a.getRemark() + " -> " + b.getRemark());
                                    return a;
                                }
                        )
                ));

        // 4. 构造表格行
        List<LinkedHashMap<String, Object>> tableRows = expenseDates.stream()
                .map(expenseDate -> {
                    LinkedHashMap<String, Object> row = new LinkedHashMap<>();
                    row.put("expenseDate", expenseDate);

                    // 填充每个类目的金额
                    groupedMap.forEach((key, record) -> {
                        if (record.getExpenseDate().equals(expenseDate)) {
                            RecordVO recordVO = new RecordVO();
                            recordVO.setExpenseAmount(record.getExpenseAmount());
                            recordVO.setRemark(record.getRemark());
                            row.put(record.getCategoryId().toString(), recordVO);
                        }
                    });

                    return row;
                })
                .collect(Collectors.toList());

        page.setRecords(tableRows);
        return page;
    }

    public Page<LinkedHashMap<String, Object>> queryGroupedRecords(QueryBalanceVO vo, List<CategoryEntity> categories) {

        // 1. 先查出所有涉及的日期（分页）
        Page<String> datePage = new Page<>(vo.getCurrent(), vo.getPageSize());
        Page<DailyExpenseRecordEntity> pageResult = queryRecordRepository.findDistinctDates(vo, vo.getCurrent(), vo.getPageSize());
        datePage.setRecords(pageResult.getRecords().stream()
                .map(DailyExpenseRecordEntity::getExpenseDate)
                .collect(Collectors.toList()));
        datePage.setTotal(pageResult.getTotal());
        if (datePage.getRecords().isEmpty()) {
            return new Page<>(datePage.getCurrent(), datePage.getSize());
        }

        // 2. 查出这些日期下的所有记录
        List<DailyExpenseRecordEntity> records = queryRecordRepository.findByDates(datePage.getRecords());

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

    /**
     * 补全类目层级信息：将父类目从数据库加载，并合并子类目
     */
    private List<CategoryEntity> enrichCategoryHierarchy(List<DataCategoryVO> dataCategoryList) {
        List<DataCategoryVO> distinctCategories = dataCategoryList.stream()
                .distinct()
                .collect(Collectors.toList());

        List<CategoryEntity> result = new ArrayList<>();

        // 提取所有非根类目的父类目ID
        Set<Integer> parentIds = distinctCategories.stream()
                .map(DataCategoryVO::getParentId)
                .filter(id -> id != 0)
                .collect(Collectors.toSet());

        // 批量查询父类目
        if (!parentIds.isEmpty()) {
            List<CategoryEntity> allCategories = queryCategoryRepository.findByCategoryIds(parentIds);
            result.addAll(allCategories);
        }

        // 添加当前类目（子类目）
        for (DataCategoryVO item : distinctCategories) {
            CategoryEntity entity = new CategoryEntity();
            entity.setId(item.getCategoryId());
            entity.setCategoryName(item.getCategoryName());
            entity.setParentId(item.getParentId());
            entity.setList(new ArrayList<>()); // 初始化子类目列表
            result.add(entity);
        }

        return result;
    }

}
