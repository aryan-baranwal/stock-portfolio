package com.portfolio.alert.service;

import com.portfolio.alert.dto.AlertResponseDto;
import com.portfolio.alert.dto.CreateAlertRequestDto;
import com.portfolio.alert.dto.UpdateAlertRequestDto;
import com.portfolio.alert.enums.AlertStatus;

import java.util.List;

public interface AlertService {

    AlertResponseDto createAlert(Long userId, CreateAlertRequestDto request);

    List<AlertResponseDto> getAllAlertsForUser(Long userId);

    AlertResponseDto getAlertById(Long alertId, Long userId);

    AlertResponseDto updateAlert(Long alertId, Long userId,
                                 UpdateAlertRequestDto request);

    void updateAlertStatus(Long alertId, Long userId, AlertStatus status);

    void deleteAlert(Long alertId, Long userId);

    List<AlertResponseDto> getTriggeredAlerts(Long userId);
}