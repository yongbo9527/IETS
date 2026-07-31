package org.company.finance.report.application.query;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.company.finance.common.util.DateUtils;
import org.company.finance.report.application.query.model.BigCategoryExpenseQueryModel;
import org.company.finance.report.application.query.model.ExpenseDetailQueryModel;
import org.company.finance.report.application.query.model.ReportDataQueryModel;
import org.company.finance.report.application.query.model.ReportDatasetQueryModel;
import org.company.finance.report.domain.repository.QueryReportRepository;
import org.company.finance.report.interfaces.rest.request.ReportRequestVO;
import org.company.finance.report.interfaces.rest.response.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11 15:10
 *
 */
@Service
@RequiredArgsConstructor
public class ReportQueryService {

    private final QueryReportRepository queryReportRepository;

    public Map<String, IncomeExpenseDataVO> getDailyExpense(String searchDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(searchDate, formatter);

        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDateStr = startDate.format(dateFormatter);
        String endDateStr = endDate.format(dateFormatter);

        List<String> dateList = DateUtils.getDateList(startDateStr, endDateStr);

        ReportRequestVO request = new ReportRequestVO();
        request.setStartDate(startDateStr);
        request.setEndDate(endDateStr);

        List<ReportDataQueryModel> reportData = queryReportRepository.findExpenseByDate(request);
        List<ReportDataQueryModel> incomeList = reportData.stream()
                .filter(item -> item.getExpenseType() == 2)
                .collect(Collectors.toList());
        List<ReportDataQueryModel> expenseList = reportData.stream()
                .filter(item -> item.getExpenseType() == 1)
                .collect(Collectors.toList());

        Map<String, IncomeExpenseDataVO> result = new LinkedHashMap<>();
        Map<String, List<ReportDataQueryModel>> incomeDataMap = incomeList.stream().collect(Collectors.groupingBy(ReportDataQueryModel::getDate));
        Map<String, List<ReportDataQueryModel>> expenseDataMap = expenseList.stream().collect(Collectors.groupingBy(ReportDataQueryModel::getDate));
        for (String date : dateList) {
            List<ReportDataQueryModel> incomeDayList = incomeDataMap.getOrDefault(date, Collections.emptyList());
            List<ReportDataQueryModel> expenseDayList = expenseDataMap.getOrDefault(date, Collections.emptyList());
            BigDecimal incomeSum = incomeDayList.stream().map(ReportDataQueryModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal expenseSum = expenseDayList.stream().map(ReportDataQueryModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put(date, new IncomeExpenseDataVO(incomeSum, expenseSum));
        }
        return result;
    }

    public LineEchartsResponse getMonthExpense(ReportRequestVO request) {
        List<String> dateList = DateUtils.getMonthList(request.getStartDate(), request.getEndDate());
        List<ReportDataQueryModel> reportData = queryReportRepository.findExpenseByDate(request);
        LineEchartsResponse response = new LineEchartsResponse();
        response.setXAxisData(dateList);
        if (CollectionUtils.isEmpty(reportData)) {
            List<BigDecimal> zeroList = dateList.stream().map(item -> BigDecimal.ZERO).collect(Collectors.toList());
            response.setIncomeData(zeroList);
            response.setExpenseData(zeroList);
            return response;
        }

        Map<String, List<ReportDataQueryModel>> dateExpenseMap = reportData.stream().collect(Collectors.groupingBy(ReportDataQueryModel::getDate));
        List<BigDecimal> incomeData = new java.util.ArrayList<>(dateList.size());
        List<BigDecimal> expenseData = new java.util.ArrayList<>(dateList.size());
        for (String date : dateList) {
            List<ReportDataQueryModel> dataResponses = dateExpenseMap.get(date);
            if (dataResponses != null) {
                incomeData.add(dataResponses.stream().filter(data -> data.getExpenseType() == 2).map(ReportDataQueryModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
                expenseData.add(dataResponses.stream().filter(data -> data.getExpenseType() == 1).map(ReportDataQueryModel::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add));
            } else {
                incomeData.add(BigDecimal.ZERO);
                expenseData.add(BigDecimal.ZERO);
            }
        }
        response.setIncomeData(incomeData);
        response.setExpenseData(expenseData);
        return response;
    }

    public List<BigCategoryExpenseResponse> getBigCategoryExpense(ReportRequestVO request) {
        List<BigCategoryExpenseQueryModel> list = queryReportRepository.findBigCategoryExpense(request);
        List<BigCategoryExpenseQueryModel> effectiveList = list.stream()
                .filter(item -> item.getExpenseTotal().compareTo(BigDecimal.ZERO) != 0)
                .collect(Collectors.toList());
        BigDecimal totalMoney = effectiveList.stream().map(BigCategoryExpenseQueryModel::getExpenseTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalMoney.compareTo(BigDecimal.ZERO) == 0) {
            return Collections.emptyList();
        }
        return effectiveList.stream()
                .map(item -> toBigCategoryExpenseResponse(item, totalMoney))
                .collect(Collectors.toList());
    }

    public List<BigCategoryExpenseResponse> getBigCategoryExpenseDetail(ReportRequestVO request) {
        return queryReportRepository.findBigCategoryExpenseDetail(request).stream()
                .map(this::toBigCategoryExpenseResponse)
                .collect(Collectors.toList());
    }

    public LineEchartsResponse getMonthExpenseBar(ReportRequestVO request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LineEchartsResponse response = new LineEchartsResponse();
        if (StringUtils.isEmpty(request.getEndDate())) {
            request.setEndDate(LocalDate.now().format(formatter));
            request.setStartDate(LocalDate.now().withMonth(2).withDayOfMonth(1).format(formatter));
        }
        List<String> dateList = DateUtils.getMonthDateList(request.getStartDate(), request.getEndDate());
        response.setXAxisData(dateList);
        List<ReportDataQueryModel> dataResponseList = queryReportRepository.findExpenseByMonth(request);
        Map<String, BigDecimal> monthAmountMap = dataResponseList.stream().collect(Collectors.toMap(ReportDataQueryModel::getDate, ReportDataQueryModel::getAmount));
        List<BigDecimal> expenseData = new java.util.ArrayList<>(dateList.size());
        for (String dateStr : dateList) {
            BigDecimal orDefault = monthAmountMap.getOrDefault(dateStr, BigDecimal.ZERO);
            expenseData.add(orDefault);
        }
        response.setExpenseData(expenseData);
        return response;
    }

    public List<ExpenseDetailResponse> getExpenseDetailList(ReportRequestVO request) {
        return queryReportRepository.findExpenseDetailList(request).stream()
                .map(this::toExpenseDetailResponse)
                .collect(Collectors.toList());
    }

    public ReportDatasetResponse getReportDataset(ReportRequestVO request) {
        ReportDatasetResponse response = new ReportDatasetResponse();
        List<ReportDatasetQueryModel> datasetList = queryReportRepository.findDataset(request);
        if (CollectionUtils.isEmpty(datasetList)) {
            return response;
        }

        List<String> categoryList = datasetList.stream().map(ReportDatasetQueryModel::getParentName).distinct().collect(Collectors.toList());
        List<String> dateList = datasetList.stream().map(ReportDatasetQueryModel::getDailyDate).distinct().collect(Collectors.toList());
        List<List<Object>> outerList = new java.util.ArrayList<>(categoryList.size() + 1);
        List<Object> productList = new java.util.ArrayList<>(dateList.size() + 1);
        productList.add("product");
        for (String date : dateList) {
            productList.add(date);
        }
        outerList.add(productList);
        Map<String, List<ReportDatasetQueryModel>> dateSourceMap = datasetList.stream()
                .collect(Collectors.groupingBy(ReportDatasetQueryModel::getDailyDate))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new
                ));

        LinkedMultiValueMap<String, Object> datasetMap = new LinkedMultiValueMap<>();
        for (String dateStr : dateSourceMap.keySet()) {
            List<ReportDatasetQueryModel> datasetModels = dateSourceMap.get(dateStr);
            Map<String, BigDecimal> categoryAmountMap = datasetModels.stream()
                    .collect(Collectors.toMap(ReportDatasetQueryModel::getParentName, ReportDatasetQueryModel::getExpenseAmount));
            for (String categoryName : categoryList) {
                if (categoryAmountMap.containsKey(categoryName)) {
                    datasetMap.add(categoryName, categoryAmountMap.get(categoryName));
                } else {
                    datasetMap.add(categoryName, BigDecimal.ZERO);
                }
            }
        }

        for (String categoryName : categoryList) {
            List<Object> objects = datasetMap.get(categoryName);
            if (objects == null) {
                objects = new java.util.ArrayList<>();
            }
            objects.add(0, categoryName);
            outerList.add(objects);
        }
        response.setSource(outerList);
        return response;
    }

    private BigCategoryExpenseResponse toBigCategoryExpenseResponse(BigCategoryExpenseQueryModel model, BigDecimal totalMoney) {
        BigCategoryExpenseResponse response = toBigCategoryExpenseResponse(model);
        response.setExpensePercent(model.getExpenseTotal()
                .divide(totalMoney, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP));
        return response;
    }

    private BigCategoryExpenseResponse toBigCategoryExpenseResponse(BigCategoryExpenseQueryModel model) {
        BigCategoryExpenseResponse response = new BigCategoryExpenseResponse();
        response.setParentId(model.getParentId());
        response.setParentCategory(model.getParentCategory());
        response.setExpenseTotal(model.getExpenseTotal());
        response.setExpenseCount(model.getExpenseCount());
        response.setChildId(model.getChildId());
        response.setChildCategory(model.getChildCategory());
        return response;
    }

    private ExpenseDetailResponse toExpenseDetailResponse(ExpenseDetailQueryModel model) {
        ExpenseDetailResponse response = new ExpenseDetailResponse();
        response.setExpenseDate(model.getExpenseDate());
        response.setParentId(model.getParentId());
        response.setParentCategory(model.getParentCategory());
        response.setChildId(model.getChildId());
        response.setChildCategory(model.getChildCategory());
        response.setExpenseTotal(model.getExpenseTotal());
        response.setRemark(model.getRemark());
        return response;
    }

    public IncomeExpenseSummaryResponse getIncomeExpenseSummary(ReportRequestVO request) {
        List<ReportDataQueryModel> reportData = queryReportRepository.findIncomeExpenseSummary(request);
        BigDecimal totalExpense = reportData.stream()
                .filter(item -> item.getExpenseType() == 1)
                .map(ReportDataQueryModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIncome = reportData.stream()
                .filter(item -> item.getExpenseType() == 2)
                .map(ReportDataQueryModel::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        IncomeExpenseSummaryResponse response = new IncomeExpenseSummaryResponse();
        response.setTotalExpense(totalExpense);
        response.setTotalIncome(totalIncome);
        response.setNetIncome(totalIncome.subtract(totalExpense));
        return response;
    }
}
