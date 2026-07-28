package org.company.finance.report.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.company.finance.report.application.query.ReportQueryService;
import org.company.finance.report.interfaces.rest.request.ReportRequestVO;
import org.company.finance.report.interfaces.rest.response.IncomeExpenseDataVO;
import org.company.finance.report.interfaces.rest.response.LineEchartsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReportControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private ReportQueryService reportQueryService;

    @InjectMocks
    private ReportController reportController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    @Test
    void shouldQueryDailyExpenseOnRestfulPath() throws Exception {
        Map<String, IncomeExpenseDataVO> response = new LinkedHashMap<>();
        response.put("2026-01-01", new IncomeExpenseDataVO(new BigDecimal("100.00"), new BigDecimal("20.00")));
        when(reportQueryService.getDailyExpense(eq("2026-01"))).thenReturn(response);

        mockMvc.perform(get("/reports/daily-expense").param("searchDate", "2026-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data['2026-01-01'].income").value(100.00))
                .andExpect(jsonPath("$.data['2026-01-01'].expense").value(20.00));

        verify(reportQueryService, times(1)).getDailyExpense("2026-01");
    }

    @Test
    void shouldQueryMonthExpenseOnRestfulPath() throws Exception {
        LineEchartsResponse response = new LineEchartsResponse();
        response.setXAxisData(Collections.singletonList("2026-01-01"));
        response.setIncomeData(Collections.singletonList(new BigDecimal("100.00")));
        response.setExpenseData(Collections.singletonList(new BigDecimal("50.00")));
        when(reportQueryService.getMonthExpense(any(ReportRequestVO.class))).thenReturn(response);

        ReportRequestVO request = new ReportRequestVO();
        request.setStartDate("2026-01-01");
        request.setEndDate("2026-01-31");

        mockMvc.perform(post("/reports/month-expense")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.xaxisData[0]").value("2026-01-01"));

        verify(reportQueryService, times(1)).getMonthExpense(any(ReportRequestVO.class));
    }
}
