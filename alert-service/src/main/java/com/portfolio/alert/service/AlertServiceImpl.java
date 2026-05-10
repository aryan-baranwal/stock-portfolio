package com.portfolio.alert.service;

import com.portfolio.alert.dto.AlertResponseDto;
import com.portfolio.alert.dto.CreateAlertRequestDto;
import com.portfolio.alert.dto.UpdateAlertRequestDto;
import com.portfolio.alert.entity.Alert;
import com.portfolio.alert.enums.AlertStatus;
import com.portfolio.alert.exception.AlertNotFoundException;
import com.portfolio.alert.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    @Override
    @Transactional
    public AlertResponseDto createAlert(Long userId,
                                        CreateAlertRequestDto request) {
        log.info("Creating alert for userId: {}, type: {}",
                userId, request.getAlertType());

        Alert alert = Alert.builder()
                .userId(userId)
                .alertType(request.getAlertType())
                .stockSymbol(request.getStockSymbol())
                .targetPrice(request.getTargetPrice())
                .condition(request.getCondition())
                .portfolioId(request.getPortfolioId())
                .lossThresholdPercent(request.getLossThresholdPercent())
                .build();

        Alert saved = alertRepository.save(alert);
        log.info("Alert created successfully with id: {}", saved.getId());

        return mapToDto(saved);
    }

    @Override
    public List<AlertResponseDto> getAllAlertsForUser(Long userId) {
        return alertRepository
                .findByUserIdAndIsDeletedFalse(userId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AlertResponseDto getAlertById(Long alertId, Long userId) {
        Alert alert = alertRepository
                .findByIdAndUserIdAndIsDeletedFalse(alertId, userId)
                .orElseThrow(() -> new AlertNotFoundException(alertId));

        return mapToDto(alert);
    }

    @Override
    @Transactional
    public AlertResponseDto updateAlert(Long alertId, Long userId,
                                        UpdateAlertRequestDto request) {
        Alert alert = alertRepository
                .findByIdAndUserIdAndIsDeletedFalse(alertId, userId)
                .orElseThrow(() -> new AlertNotFoundException(alertId));

        if (request.getTargetPrice() != null) {
            alert.setTargetPrice(request.getTargetPrice());
        }
        if (request.getCondition() != null) {
            alert.setCondition(request.getCondition());
        }
        if (request.getLossThresholdPercent() != null) {
            alert.setLossThresholdPercent(request.getLossThresholdPercent());
        }

        return mapToDto(alertRepository.save(alert));
    }

    @Override
    @Transactional
    public void updateAlertStatus(Long alertId, Long userId,
                                  AlertStatus status) {
        Alert alert = alertRepository
                .findByIdAndUserIdAndIsDeletedFalse(alertId, userId)
                .orElseThrow(() -> new AlertNotFoundException(alertId));

        alert.setStatus(status);
        alertRepository.save(alert);
        log.info("Alert id: {} status updated to: {}", alertId, status);
    }

    @Override
    @Transactional
    public void deleteAlert(Long alertId, Long userId) {
        Alert alert = alertRepository
                .findByIdAndUserIdAndIsDeletedFalse(alertId, userId)
                .orElseThrow(() -> new AlertNotFoundException(alertId));

        // Soft delete — we don't remove from DB, just mark as deleted
        alert.setDeleted(true);
        alertRepository.save(alert);
        log.info("Alert id: {} soft-deleted for userId: {}", alertId, userId);
    }

    @Override
    public List<AlertResponseDto> getTriggeredAlerts(Long userId) {
        return alertRepository
                .findByUserIdAndStatusAndIsDeletedFalse(
                        userId, AlertStatus.TRIGGERED)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // ── Helper: convert Alert entity → AlertResponseDto ──
    private AlertResponseDto mapToDto(Alert alert) {
        return AlertResponseDto.builder()
                .id(alert.getId())
                .userId(alert.getUserId())
                .alertType(alert.getAlertType())
                .stockSymbol(alert.getStockSymbol())
                .targetPrice(alert.getTargetPrice())
                .condition(alert.getCondition())
                .portfolioId(alert.getPortfolioId())
                .lossThresholdPercent(alert.getLossThresholdPercent())
                .status(alert.getStatus())
                .createdAt(alert.getCreatedAt())
                .lastTriggeredAt(alert.getLastTriggeredAt())
                .build();
    }
}