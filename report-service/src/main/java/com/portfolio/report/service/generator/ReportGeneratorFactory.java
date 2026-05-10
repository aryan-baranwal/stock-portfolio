package com.portfolio.report.service.generator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportGeneratorFactory {

    private final PdfReportGenerator pdfReportGenerator;
    private final ExcelReportGenerator excelReportGenerator;

    public ReportGenerator getGenerator(String type) {
        return switch (type.toLowerCase()) {
            case "pdf" -> pdfReportGenerator;
            case "excel" -> excelReportGenerator;
            default -> throw new IllegalArgumentException("Unsupported report type: " + type);
        };
    }
}