package com.portfolio.report.service;

import com.portfolio.report.dto.ReportMetadataDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportService {
    ReportMetadataDto getSummaryData(Long portfolioId, Long userId);
    byte[] exportReport(Long portfolioId, Long userId, String type);
    Page<ReportMetadataDto> getReportHistory(Long userId, Pageable pageable);
    byte[] downloadReport(Long reportId, Long userId);
    void generateDailySummaryForUser(Long userId, Long portfolioId);
}