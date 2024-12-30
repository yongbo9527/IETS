package org.oneself.balance.demo.service.impl;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.oneself.balance.demo.mapper.ReportMapper;
import org.oneself.balance.demo.service.ReportService;
import org.oneself.balance.demo.utils.DateUtils;
import org.oneself.balance.demo.vo.calendar.IncomeExpenseDataVO;
import org.oneself.balance.demo.vo.request.ReportRequestVO;
import org.oneself.balance.demo.vo.response.BigCategoryExpenseResponse;
import org.oneself.balance.demo.vo.response.LineEchartsResponse;
import org.oneself.balance.demo.vo.response.ReportDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
        BigDecimal totalMoney = list.stream().map(BigCategoryExpenseResponse::getExpenseTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<BigCategoryExpenseResponse> results = Lists.newArrayList();
        if (totalMoney.compareTo(BigDecimal.ZERO) != 0) {
            results = list.stream().filter(item -> item.getExpenseTotal().compareTo(BigDecimal.ZERO) != 0).map(item -> {
                item.setExpensePercent(item.getExpenseTotal().divide(totalMoney, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
                return item;
            }).collect(Collectors.toList());
        }

        return results;
    }

    @Override
    public BigCategoryExpenseResponse querySmallCategoryExpense(ReportRequestVO vo) {
        return null;
    }
}
