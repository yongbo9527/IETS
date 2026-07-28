package org.company.finance.tally.interfaces.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.company.finance.application.service.ImportExportService;
import org.company.finance.application.vo.balance.QueryBalanceVO;
import org.company.finance.tally.application.command.FinanceCommandService;
import org.company.finance.tally.application.query.FinanceQueryService;
import org.company.finance.tally.interfaces.rest.request.CreateExpenseRecordRequest;
import org.company.finance.tally.interfaces.rest.request.UpdateExpenseRecordRequest;
import org.company.finance.tally.interfaces.rest.response.ExpenseRecordResponse;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FinanceControllerTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Mock
    private FinanceCommandService financeCommandService;

    @Mock
    private FinanceQueryService financeQueryService;

    @Mock
    private ImportExportService importExportService;

    @InjectMocks
    private FinanceController financeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(financeController).build();
    }

    @Test
    void testListDailyRecords() throws Exception {
        Page<ExpenseRecordResponse> page = new Page<>(1, 10);
        page.setRecords(Collections.emptyList());
        page.setTotal(0);
        when(financeQueryService.listDailyRecords(any(QueryBalanceVO.class))).thenReturn(page);

        mockMvc.perform(post("/tally/listDailyRecords")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"current\":1,\"pageSize\":10,\"startDate\":\"2026-01-01\",\"endDate\":\"2026-01-31\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.total").value(0));
    }

    @Test
    void testSaveRecord() throws Exception {
        doNothing().when(financeCommandService).saveRecord(any(CreateExpenseRecordRequest.class));

        CreateExpenseRecordRequest request = new CreateExpenseRecordRequest();
        request.setCategoryId(1);
        request.setExpenseAmount(new BigDecimal("15.50"));
        request.setExpenseDate("2026-01-01");
        request.setExpenseType(1);
        request.setRemark("午饭");

        mockMvc.perform(put("/tally/saveRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("执行成功"))
                .andExpect(jsonPath("$.data").value("新增成功"));

        verify(financeCommandService, times(1)).saveRecord(any(CreateExpenseRecordRequest.class));
    }

    @Test
    void testUpdateRecord() throws Exception {
        doNothing().when(financeCommandService).updateRecord(any(UpdateExpenseRecordRequest.class));

        UpdateExpenseRecordRequest request = new UpdateExpenseRecordRequest();
        request.setId(1);
        request.setCategoryId(1);
        request.setExpenseAmount(new BigDecimal("20.50"));
        request.setExpenseDate("2026-01-02");
        request.setExpenseType(1);
        request.setRemark("晚饭");

        mockMvc.perform(put("/tally/updateRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(OBJECT_MAPPER.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("执行成功"))
                .andExpect(jsonPath("$.data").value("修改成功"));

        verify(financeCommandService, times(1)).updateRecord(any(UpdateExpenseRecordRequest.class));
    }
}
