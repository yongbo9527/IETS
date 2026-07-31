package org.company.finance.tally.interfaces.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 *  @Author: Ron Yu
 *  @Create: 2025-10-11
 *
 */
@Data
@Schema(description = "导入导出请求参数")
public class ImportExportRequestVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "日期格式不能为空")
    @Schema(description = "开始日期 yyyy-MM-dd")
    private String startDate;

    @NotBlank(message = "结束日期不能为空")
    @Schema(description = "结束日期 yyyy-MM-dd")
    private String endDate;
}
