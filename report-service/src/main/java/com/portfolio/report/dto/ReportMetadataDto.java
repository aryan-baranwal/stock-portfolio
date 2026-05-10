package com.portfolio.report.dto;

import com.portfolio.report.enums.ReportType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportMetadataDto {
    private Long id;
    private Long portfolioId;
    private ReportType reportType;
    private String fileName;
    private LocalDateTime generatedAt;
}
