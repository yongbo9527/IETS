package org.company.finance.application.vo.response;

import lombok.Data;

import java.util.List;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-01-10 10:14
 *
 */
@Data
public class ReportDatasetResponse {

    private List<List<Object>> source;

}
