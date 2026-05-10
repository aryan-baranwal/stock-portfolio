package com.portfolio.report.controller;

import com.portfolio.report.dto.ApiResponse;
import com.portfolio.report.dto.ReportMetadataDto;
import com.portfolio.report.dto.request.ReportScheduleRequestDto;
import com.portfolio.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Reports", description = "Portfolio report generation and export")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Get portfolio summary data (JSON)")
    @GetMapping("/portfolio-summary")
    public ResponseEntity<ApiResponse<ReportMetadataDto>> getSummary(
            @RequestParam Long portfolioId,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getSummaryData(portfolioId, userId)));
    }

    @Operation(summary = "Export portfolio report as PDF or Excel")
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportReport(
            @RequestParam Long portfolioId,
            @RequestParam(defaultValue = "pdf") String type,
            @RequestHeader("X-User-Id") Long userId) {

        log.info("Export report request: type={}, portfolioId={}, userId={}", type, portfolioId, userId);

        byte[] reportBytes = reportService.exportReport(portfolioId, userId, type);

        String contentType = type.equalsIgnoreCase("pdf")
                ? "application/pdf"
                : "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

        String extension = type.equalsIgnoreCase("pdf") ? ".pdf" : ".xlsx";
        String filename = "portfolio_report_" + portfolioId + extension;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(reportBytes);
    }

    @Operation(summary = "View previously generated reports")
    @GetMapping("/history")
    public ResponseEntity<ApiResponse<Page<ReportMetadataDto>>> getHistory(
            @RequestHeader("X-User-Id") Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(reportService.getReportHistory(userId, pageable)));
    }

    @Operation(summary = "Re-download an old report")
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") Long userId) {

        byte[] bytes = reportService.downloadReport(id, userId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report_" + id + "\"")
                .body(bytes);
    }

    @Operation(summary = "Configure daily report schedule")
    @PostMapping("/schedule")
    public ResponseEntity<ApiResponse<String>> schedule(
            @Valid @RequestBody ReportScheduleRequestDto request,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success("Schedule configured: " + request.getCronExpression()));
    }
}