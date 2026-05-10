package com.portfolio.report.repository;

import com.portfolio.report.entity.ReportMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportMetadataRepository extends JpaRepository<ReportMetadata, Long> {
    Page<ReportMetadata> findByUserIdOrderByGeneratedAtDesc(Long userId, Pageable pageable);
}
