package org.company.finance.tally.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.company.finance.category.domain.model.Category;
import org.company.finance.tally.application.query.model.ExpenseRecordQueryModel;
import org.company.finance.tally.domain.model.ExpenseDate;
import org.company.finance.tally.domain.model.ExpenseRecord;
import org.company.finance.tally.domain.model.ExpenseType;
import org.company.finance.tally.domain.model.Money;
import org.company.finance.tally.domain.model.Remark;
import org.company.finance.tally.domain.repository.CommandRecordRepository;
import org.company.finance.tally.domain.repository.QueryRecordRepository;
import org.company.finance.tally.interfaces.rest.request.ImportExportRequestVO;
import org.company.finance.tally.interfaces.rest.request.QueryBalanceVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImportExportServiceImpl implements ImportExportService {

    private static final String[] EXPORT_HEADERS = {"日期", "类目ID", "类目名称", "金额", "类型", "备注"};
    private static final String[] IMPORT_HEADERS = {"expenseDate", "categoryId", "categoryName", "expenseAmount", "expenseType", "remark"};
    private static final String TEMPLATE_FILE_NAME = "收支记录导入模板.xlsx";

    private final QueryRecordRepository queryRecordRepository;
    private final CommandRecordRepository commandRecordRepository;

    @Override
    public void exportTemplate(HttpServletResponse response) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("收支记录模板");
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(EXPORT_HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue("2024-01-01");
            dataRow.createCell(1).setCellValue(1);
            dataRow.createCell(2).setCellValue("餐饮");
            dataRow.createCell(3).setCellValue(100.00);
            dataRow.createCell(4).setCellValue("1");
            dataRow.createCell(5).setCellValue("示例备注");

            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            downloadFile(response, out.toByteArray(), TEMPLATE_FILE_NAME);
        }
    }

    @Override
    public void exportData(ImportExportRequestVO vo, HttpServletResponse response) throws IOException {
        QueryBalanceVO queryVo = new QueryBalanceVO();
        queryVo.setStartDate(vo.getStartDate());
        queryVo.setEndDate(vo.getEndDate());
        queryVo.setCurrent(1);
        queryVo.setPageSize(10000);

        Page<ExpenseRecordQueryModel> page = queryRecordRepository.findByUserIdAndDateRange(null, queryVo);
        List<ExpenseRecordQueryModel> records = page.getRecords();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("收支记录");
            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(EXPORT_HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 1;
            for (ExpenseRecordQueryModel record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getExpenseDate());
                row.createCell(1).setCellValue(record.getCategoryId());
                row.createCell(2).setCellValue(record.getCategoryName() != null ? record.getCategoryName() : "");
                row.createCell(3).setCellValue(record.getExpenseAmount() != null ? record.getExpenseAmount().doubleValue() : 0);
                row.createCell(4).setCellValue(record.getExpenseType() != null ? record.getExpenseType().toString() : "");
                row.createCell(5).setCellValue(record.getRemark() != null ? record.getRemark() : "");
            }

            for (int i = 0; i < EXPORT_HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            String fileName = String.format("收支记录_%s_%s.xlsx", vo.getStartDate(), vo.getEndDate());
            workbook.write(out);
            downloadFile(response, out.toByteArray(), fileName);
        }
    }

    @Override
    public String importData(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("上传文件不能为空");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
            throw new IllegalArgumentException("文件格式必须是Excel文件(.xlsx或.xls)");
        }

        List<ExpenseRecord> records = new ArrayList<>();


        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet.getLastRowNum() < 1) {
                throw new IllegalArgumentException("文件没有数据");
            }

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> headerMap = new HashMap<>();
            for (Cell cell : headerRow) {
                headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String expenseDate = getCellStringValue(row, headerMap.get("日期"));
                Integer categoryId = getCellIntValue(row, headerMap.get("类目ID"));
                BigDecimal expenseAmount = getCellBigDecimalValue(row, headerMap.get("金额"));
                Integer expenseType = getCellIntValue(row, headerMap.get("类型"));
                String remark = getCellStringValue(row, headerMap.get("备注"));

                if (expenseDate != null && categoryId != null && expenseAmount != null && expenseType != null) {
                    Category category = Category.restore(categoryId, "IMPORT", 0, ExpenseType.ofCode(expenseType), false);
                    records.add(ExpenseRecord.create(
                            null,
                            category,
                            Money.of(expenseAmount),
                            ExpenseType.ofCode(expenseType),
                            ExpenseDate.of(expenseDate),
                            Remark.of(remark)
                    ));
                }
            }
        }

        if (!records.isEmpty()) {
            commandRecordRepository.saveBatch(records);
        }

        return String.format("导入成功，共导入 %d 条数据", records.size());
    }

    private String getCellStringValue(Row row, Integer columnIndex) {
        if (columnIndex == null) return null;
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;
        return cell.getCellType() == CellType.STRING ? cell.getStringCellValue() : 
               cell.getCellType() == CellType.NUMERIC ? String.valueOf(cell.getNumericCellValue()) : "";
    }

    private Integer getCellIntValue(Row row, Integer columnIndex) {
        if (columnIndex == null) return null;
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;
        return cell.getCellType() == CellType.NUMERIC ? (int) cell.getNumericCellValue() : 
               cell.getCellType() == CellType.STRING ? Integer.parseInt(cell.getStringCellValue()) : null;
    }

    private BigDecimal getCellBigDecimalValue(Row row, Integer columnIndex) {
        if (columnIndex == null) return null;
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;
        return cell.getCellType() == CellType.NUMERIC ? BigDecimal.valueOf(cell.getNumericCellValue()) : 
               cell.getCellType() == CellType.STRING ? new BigDecimal(cell.getStringCellValue()) : null;
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void downloadFile(HttpServletResponse response, byte[] data, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        response.setContentLength(data.length);
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }
}
