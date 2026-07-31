package org.company.finance.tally.application;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.company.finance.tally.domain.model.ExpenseRecord;
import org.company.finance.tally.domain.repository.CommandRecordRepository;
import org.company.finance.tally.domain.repository.QueryRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ImportExportServiceImplTest {

    @Mock
    private QueryRecordRepository queryRecordRepository;

    @Mock
    private CommandRecordRepository commandRecordRepository;

    private ImportExportServiceImpl importExportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        importExportService = new ImportExportServiceImpl(queryRecordRepository, commandRecordRepository);
    }

    @Test
    void shouldImportRecordsThroughCommandRepository() throws Exception {
        byte[] workbookBytes = buildWorkbook();
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "records.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                workbookBytes
        );

        doNothing().when(commandRecordRepository).saveBatch(org.mockito.ArgumentMatchers.<ExpenseRecord>anyList());

        importExportService.importData(file);

        verify(commandRecordRepository, times(1)).saveBatch(org.mockito.ArgumentMatchers.<ExpenseRecord>anyList());
    }

    private byte[] buildWorkbook() throws Exception {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("收支记录");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("日期");
            header.createCell(1).setCellValue("类目ID");
            header.createCell(2).setCellValue("类目名称");
            header.createCell(3).setCellValue("金额");
            header.createCell(4).setCellValue("类型");
            header.createCell(5).setCellValue("备注");

            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue("2026-01-01");
            row.createCell(1).setCellValue(1);
            row.createCell(2).setCellValue("餐饮");
            row.createCell(3).setCellValue(12.50);
            row.createCell(4).setCellValue(1);
            row.createCell(5).setCellValue("午饭");

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}
