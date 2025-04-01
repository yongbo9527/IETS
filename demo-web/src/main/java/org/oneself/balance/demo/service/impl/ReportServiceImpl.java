package org.oneself.balance.demo.service.impl;

import com.baomidou.mybatisplus.extension.api.R;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.oneself.balance.demo.mapper.ReportMapper;
import org.oneself.balance.demo.service.ReportService;
import org.oneself.balance.demo.utils.DateUtils;
import org.oneself.balance.demo.vo.calendar.IncomeExpenseDataVO;
import org.oneself.balance.demo.vo.report.ReportDatasetVO;
import org.oneself.balance.demo.vo.request.ReportRequestVO;
import org.oneself.balance.demo.vo.response.BigCategoryExpenseResponse;
import org.oneself.balance.demo.vo.response.LineEchartsResponse;
import org.oneself.balance.demo.vo.response.ReportDataResponse;
import org.oneself.balance.demo.vo.response.ReportDatasetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 16:19
 *
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public R queryDailyExpense(String searchDate) {
        // 使用DateTimeFormatter解析年份和月份
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(searchDate, formatter);

        // 获取当月开始日期和结束日期
        LocalDate startDate = yearMonth.atDay(1); // 当月第一天
        LocalDate endDate = yearMonth.atEndOfMonth(); // 当月最后一天

        // 格式化为字符串类型
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = startDate.format(dateFormatter);
        String endDateStr = endDate.format(dateFormatter);
        // 日期列表
        List<String> dateList = DateUtils.getDateList(startDateStr, endDateStr);
        ReportRequestVO requestVO = new ReportRequestVO();
        requestVO.setStartDate(startDateStr);
        requestVO.setEndDate(endDateStr);
        List<ReportDataResponse> reportDataResponseList = reportMapper.selectExpenseByDate(requestVO);
        // 收入list
        List<ReportDataResponse> incomeLists = reportDataResponseList.stream().filter(item -> item.getExpenseType() == 2).collect(Collectors.toList());
        // 支出list
        List<ReportDataResponse> expenseLists = reportDataResponseList.stream().filter(item -> item.getExpenseType() == 1).collect(Collectors.toList());

        // 封装结果数据
        Map<String, IncomeExpenseDataVO> expenseMap = new ConcurrentHashMap<>();
        Map<String, List<ReportDataResponse>> incomeDataMap = incomeLists.stream().collect(Collectors.groupingBy(ReportDataResponse::getDate));
        Map<String, List<ReportDataResponse>> expenseDataMap = expenseLists.stream().collect(Collectors.groupingBy(ReportDataResponse::getDate));

        for (String str : dateList) {
            // 简化代码：通过 getOrDefault 方法简化了对空列表的处理，减少了冗余代码。
            List<ReportDataResponse> incomeDayList = incomeDataMap.getOrDefault(str, Collections.emptyList());
            List<ReportDataResponse> expenseDayList = expenseDataMap.getOrDefault(str, Collections.emptyList());

            // 提前计算 incomeSum 和 expenseSum，避免在后续逻辑中重复调用流操作。
            BigDecimal incomeSum = incomeDayList.stream().map(ReportDataResponse::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal expenseSum = expenseDayList.stream().map(ReportDataResponse::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 统一处理逻辑：使用 compute 方法来统一处理 IncomeExpenseDataVO 对象的创建和更新逻辑。
            expenseMap.compute(str, (key, vo) -> {
                if (vo == null) {
                    return new IncomeExpenseDataVO(incomeSum, expenseSum);
                } else {
                    vo.setIncome(vo.getIncome().add(incomeSum));
                    vo.setExpense(vo.getExpense().add(expenseSum));
                    return vo;
                }
            });
        }
        return R.ok(expenseMap);
    }

    @Override
    public LineEchartsResponse queryMonthExpense(ReportRequestVO vo) {
        List<String> dateList = DateUtils.getDateList(vo.getStartDate(), vo.getEndDate());
        List<ReportDataResponse> reportDataResponseList = reportMapper.selectExpenseByDate(vo);
        LineEchartsResponse lineEchartsResponse = new LineEchartsResponse();
        if (CollectionUtils.isEmpty(reportDataResponseList)) {
            lineEchartsResponse.setXAxisData(dateList);
            List<BigDecimal> expenseList = dateList.stream().map(item -> BigDecimal.ZERO).collect(Collectors.toList());
            lineEchartsResponse.setIncomeData(expenseList);
            lineEchartsResponse.setExpenseData(expenseList);
            return lineEchartsResponse;
        }
        Map<String, List<ReportDataResponse>> dateExpenseMap = reportDataResponseList.stream().collect(Collectors.groupingBy(ReportDataResponse::getDate));
        // 封装echarts数据
        lineEchartsResponse.setXAxisData(dateList);
        List<BigDecimal> incomeData = Lists.newArrayListWithCapacity(dateList.size());
        List<BigDecimal> expenseData = Lists.newArrayListWithCapacity(dateList.size());
        for (String s : dateList) {
            List<ReportDataResponse> dataResponses = dateExpenseMap.get(s);
            if (dataResponses != null) {
                // expenseType 1 支出，2 收入
                incomeData.add(dataResponses.stream().filter(data -> data.getExpenseType() == 2).map(ReportDataResponse::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                expenseData.add(dataResponses.stream().filter(data -> data.getExpenseType() == 1).map(ReportDataResponse::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            } else {
                incomeData.add(BigDecimal.ZERO);
                expenseData.add(BigDecimal.ZERO);
            }
        }
        lineEchartsResponse.setIncomeData(incomeData);
        lineEchartsResponse.setExpenseData(expenseData);

        return lineEchartsResponse;
    }

    @Override
    public List<BigCategoryExpenseResponse> queryBigCategoryExpense(ReportRequestVO vo) {
        // 饼图数据
        List<BigCategoryExpenseResponse> list = reportMapper.selectPieExpenseByCategoryId(vo);
        // 过滤掉支出为0的数据
        List<BigCategoryExpenseResponse> effectiveList = list.stream().filter(item -> item.getExpenseTotal().compareTo(BigDecimal.ZERO) != 0).collect(Collectors.toList());

        BigDecimal totalMoney = effectiveList.stream().map(BigCategoryExpenseResponse::getExpenseTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<BigCategoryExpenseResponse> results = Lists.newArrayList();
        if (totalMoney.compareTo(BigDecimal.ZERO) != 0) {
            results = effectiveList.stream().filter(item -> item.getExpenseTotal().compareTo(BigDecimal.ZERO) != 0).map(item -> {
                item.setExpensePercent(item.getExpenseTotal().divide(totalMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                return item;
            }).collect(Collectors.toList());
        }

        return results;
    }

    @Override
    public List<BigCategoryExpenseResponse> querySmallCategoryExpenseDetail(ReportRequestVO vo) {
        Integer bigCategoryId = vo.getBigCategoryId();
        List<BigCategoryExpenseResponse> list = reportMapper.selectBigCategoryExpenseDetail(vo.getBigCategoryId());


        return list;
    }

    @Override
    public ReportDatasetResponse queryReportDataset(ReportRequestVO vo) {
        ReportDatasetResponse response = new ReportDatasetResponse();
        List<ReportDatasetVO> datasetVOList = reportMapper.selectDatasetVO(vo);
        if (CollectionUtils.isEmpty(datasetVOList)) {
            return response;
        }
        // 类目种类
        List<String> categoryList = datasetVOList.stream().map(ReportDatasetVO::getParentName).distinct().collect(Collectors.toList());
        // 日期列表
        List<String> dateList = datasetVOList.stream().map(ReportDatasetVO::getDailyDate).distinct().collect(Collectors.toList());
        List<List<Object>> outerList = Lists.newArrayListWithCapacity(categoryList.size() + 1);
        List<Object> productList = Lists.newArrayListWithCapacity(dateList.size() + 1);
        productList.add("product");
        for (String date : dateList) {
            productList.add(date);
        }
        outerList.add(productList);
        // 根据不同月份计算数据
        Map<String, List<ReportDatasetVO>> dateSourceMap = datasetVOList.stream()
                .collect(Collectors.groupingBy(ReportDatasetVO::getDailyDate)) // 分组
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // 按键升序排序
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, // 处理键冲突（这里不会有冲突）
                        LinkedHashMap::new // 使用 LinkedHashMap 保持顺序
                ));
        LinkedMultiValueMap<String, Object> datasetMap = new LinkedMultiValueMap<>();
        for (String dateStr : dateSourceMap.keySet()) {
            List<ReportDatasetVO> datasetVOS = dateSourceMap.get(dateStr);
            Map<String, BigDecimal> categoryAmountMap = datasetVOS.stream().collect(Collectors.toMap(ReportDatasetVO::getParentName, ReportDatasetVO::getExpenseAmount));
            for (String s : categoryList) {
                if (categoryAmountMap.containsKey(s)) {
                    datasetMap.add(s, categoryAmountMap.get(s));
                } else {
                    datasetMap.add(s, 0);
                }
            }
        }

        // 封装成前端需要echarts数据集dataset
        for (String categoryName : categoryList) {
            List<Object> objects = datasetMap.get(categoryName);
            objects.add(0, categoryName);
            outerList.add(objects);
        }
        response.setSource(outerList);
        return response;
    }

    @Override
    public LineEchartsResponse queryMonthExpenseBar(ReportRequestVO vo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LineEchartsResponse lineEchartsResponse = new LineEchartsResponse();
        if (StringUtils.isEmpty(vo.getEndDate())) {
            vo.setEndDate(LocalDate.now().format(formatter));
            vo.setStartDate(LocalDate.now().withDayOfMonth(1)
                    .minusMonths(5).format(formatter));
        }
        List<String> dateList = DateUtils.getMonthDateList(vo.getStartDate(), vo.getEndDate());
        lineEchartsResponse.setXAxisData(dateList);
        List<ReportDataResponse> dataResponseList = reportMapper.selectExpenseByMonthDate(vo);
        Map<String, BigDecimal> monthAmountMap = dataResponseList.stream().collect(Collectors.toMap(ReportDataResponse::getDate, ReportDataResponse::getAmount));
        List<BigDecimal> expenseData = Lists.newArrayListWithCapacity(dateList.size());
        for (int i = 0; i < dateList.size(); i++) {
            String dateStr = dateList.get(i);
            BigDecimal orDefault = monthAmountMap.getOrDefault(dateStr, BigDecimal.ZERO);
            expenseData.add(orDefault);
        }
        lineEchartsResponse.setExpenseData(expenseData);
        return lineEchartsResponse;
    }

}
