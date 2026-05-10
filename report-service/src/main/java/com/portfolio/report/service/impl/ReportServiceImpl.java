package com.portfolio.report.service;

import com.portfolio.report.dto.ReportDataDto;
import com.portfolio.report.dto.ReportMetadataDto;
import com.portfolio.report.entity.ReportMetadata;
import com.portfolio.report.enums.ReportType;
import com.portfolio.report.feign.PortfolioFeignClient;
import com.portfolio.report.messaging.ReportEventPublisher;
import com.portfolio.report.repository.ReportMetadataRepository;
import com.portfolio.report.service.generator.ReportGenerator;
import com.portfolio.report.service.generator.ReportGeneratorFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportServiceImpl implements ReportService {

    private final PortfolioFeignClient portfolioFeignClient;
    private final ReportGeneratorFactory reportGeneratorFactory;
    private final ReportMetadataRepository reportMetadataRepository;
    private final ReportEventPublisher reportEventPublisher;

    @Value("${report.storage-path}")
    private String storagePath;

    @Override
    @Transactional(readOnly = true)
    public ReportMetadataDto getSummaryData(Long portfolioId, Long userId) {
        ReportDataDto data = portfolioFeignClient.getPortfolioSummary(portfolioId);
        return ReportMetadataDto.builder()
                .portfolioId(portfolioId)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    @Override
    @Transactional
    public byte[] exportReport(Long portfolioId, Long userId, String type) {
        ReportDataDto data = portfolioFeignClient.getPortfolioSummary(portfolioId);

        ReportGenerator generator = reportGeneratorFactory.getGenerator(type);
        byte[] fileBytes = generator.generate(data);

        String fileName = "portfolio_" + portfolioId + "_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))
                + generator.getFileExtension();

        String filePath = saveToStorage(fileName, fileBytes);

        ReportMetadata metadata = ReportMetadata.builder()
                .userId(userId)
                .portfolioId(portfolioId)
                .reportType(type.equalsIgnoreCase("pdf") ? ReportType.PDF : ReportType.EXCEL)
                .fileName(fileName)
                .filePath(filePath)
                .build();

        reportMetadataRepository.save(metadata);
        log.info("Report exported: {} for userId: {}", fileName, userId);

        return fileBytes;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReportMetadataDto> getReportHistory(Long userId, Pageable pageable) {
        return reportMetadataRepository
                .findByUserIdOrderByGeneratedAtDesc(userId, pageable)
                .map(this::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] downloadReport(Long reportId, Long userId) {
        ReportMetadata metadata = reportMetadataRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found: " + reportId));

        try {
            return Files.readAllBytes(Paths.get(metadata.getFilePath()));
        } catch (IOException e) {
            throw new RuntimeException("Report file not found on disk");
        }
    }

    @Override
    @Transactional
    public void generateDailySummaryForUser(Long userId, Long portfolioId) {
        byte[] pdf = exportReport(portfolioId, userId, "pdf");
        log.info("Daily summary generated for userId: {}", userId);
        reportEventPublisher.publishDailySummaryReady(userId, portfolioId);
    }

    private String saveToStorage(String fileName, byte[] bytes) {
        try {
            Path dir = Paths.get(storagePath);
            Files.createDirectories(dir);
            Path filePath = dir.resolve(fileName);
            Files.write(filePath, bytes);
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to save report file: " + e.getMessage());
        }
    }

    private ReportMetadataDto toDto(ReportMetadata entity) {
        return ReportMetadataDto.builder()
                .id(entity.getId())
                .portfolioId(entity.getPortfolioId())
                .reportType(entity.getReportType())
                .fileName(entity.getFileName())
                .generatedAt(entity.getGeneratedAt())
                .build();
    }
}