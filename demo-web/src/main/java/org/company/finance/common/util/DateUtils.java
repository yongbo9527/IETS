package org.company.finance.common.util;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2024-12-23 21:08
 *
 */
public class DateUtils {

    /**
     * 获取指定时间范围内的日期列表
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getDateList(String beginDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate begin = LocalDate.parse(beginDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<String> dateList = new ArrayList<>();

        while (!begin.isAfter(end)) {
            dateList.add(begin.format(formatter));
            begin = begin.plusDays(1);
        }
        return dateList;
    }

    public static List<String> getMonthList(String beginDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth begin = YearMonth.parse(beginDate.substring(0, 7), formatter);
        YearMonth end = YearMonth.parse(endDate.substring(0, 7), formatter);
        List<String> monthList = new ArrayList<>();

        while (!begin.isAfter(end)) {
            monthList.add(begin.format(formatter));
            begin = begin.plusMonths(1);
        }
        return monthList;
    }

    public static List<String> getMonthDateList(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter resultFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate begin = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<String> dateList = new ArrayList<>();

        while (!begin.isAfter(end)) {
            dateList.add(begin.format(resultFormatter));
            begin = begin.plusMonths(1);
        }
        return dateList;
    }
}
