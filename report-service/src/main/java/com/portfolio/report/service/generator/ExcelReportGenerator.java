package com.portfolio.report.service.generator;

import com.portfolio.report.dto.HoldingReportDto;
import com.portfolio.report.dto.ReportDataDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component("excelReportGenerator")
@Slf4j
public class ExcelReportGenerator implements ReportGenerator {

    @Override
    public byte[] generate(ReportDataDto data) {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Portfolio Summary");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // Title row
            Row titleRow = sheet.createRow(0);
            titleRow.createCell(0).setCellValue("Portfolio: " + data.getPortfolioName());

            // Summary rows
            Row summaryHeader = sheet.createRow(2);
            summaryHeader.createCell(0).setCellValue("Total Invested");
            summaryHeader.createCell(1).setCellValue("Total Current Value");
            summaryHeader.createCell(2).setCellValue("Total Gain/Loss");
            summaryHeader.createCell(3).setCellValue("Gain/Loss %");

            Row summaryData = sheet.createRow(3);
            summaryData.createCell(0).setCellValue(data.getTotalInvestedValue().doubleValue());
            summaryData.createCell(1).setCellValue(data.getTotalCurrentValue().doubleValue());
            summaryData.createCell(2).setCellValue(data.getTotalGainLoss().doubleValue());
            summaryData.createCell(3).setCellValue(data.getTotalGainLossPercent().doubleValue());

            // Holdings table header
            Row holdingHeader = sheet.createRow(5);
            String[] cols = {"Symbol", "Qty", "Buy Price", "Current Price",
                    "Current Value", "Gain/Loss", "Gain/Loss %"};
            for (int i = 0; i < cols.length; i++) {
                Cell cell = holdingHeader.createCell(i);
                cell.setCellValue(cols[i]);
                cell.setCellStyle(headerStyle);
            }

            // Holdings data
            if (data.getHoldings() != null) {
                int rowNum = 6;
                for (HoldingReportDto h : data.getHoldings()) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(h.getStockSymbol());
                    row.createCell(1).setCellValue(h.getQuantity().doubleValue());
                    row.createCell(2).setCellValue(h.getBuyPrice().doubleValue());
                    row.createCell(3).setCellValue(h.getCurrentPrice().doubleValue());
                    row.createCell(4).setCellValue(h.getCurrentValue().doubleValue());
                    row.createCell(5).setCellValue(h.getGainLoss().doubleValue());
                    row.createCell(6).setCellValue(h.getGainLossPercent().doubleValue());
                }
            }

            for (int i = 0; i < cols.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(baos);
            log.info("Excel report generated for portfolio: {}", data.getPortfolioId());
            return baos.toByteArray();

        } catch (Exception e) {
            log.error("Failed to generate Excel report", e);
            throw new RuntimeException("Excel generation failed: " + e.getMessage());
        }
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    @Override
    public String getFileExtension() {
        return ".xlsx";
    }
}