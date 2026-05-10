package com.portfolio.report.service.generator;

import com.portfolio.report.dto.ReportDataDto;

public interface ReportGenerator {
    byte[] generate(ReportDataDto data);
    String getContentType();
    String getFileExtension();
}