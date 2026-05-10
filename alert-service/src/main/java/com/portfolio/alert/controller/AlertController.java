package com.portfolio.alert.controller;

import com.portfolio.alert.dto.AlertResponseDto;
import com.portfolio.alert.dto.CreateAlertRequestDto;
import com.portfolio.alert.dto.UpdateAlertRequestDto;
import com.portfolio.alert.enums.AlertStatus;
import com.portfolio.alert.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Alerts", description = "Manage stock price and portfolio alerts")
public class AlertController {

    private final AlertService alertService;

    // ── Create Alert ──────────────────────────────────────
    @Operation(summary = "Create a new alert")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Alert created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping
    public ResponseEntity<AlertResponseDto> createAlert(
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateAlertRequestDto request) {

        log.info("POST /api/alerts — userId: {}", userId);
        AlertResponseDto response = alertService.createAlert(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ── Get All Alerts ────────────────────────────────────
    @Operation(summary = "Get all alerts for current user")
    @GetMapping
    public ResponseEntity<List<AlertResponseDto>> getAllAlerts(
            @RequestHeader("X-User-Id") Long userId) {

        log.info("GET /api/alerts — userId: {}", userId);
        return ResponseEntity.ok(alertService.getAllAlertsForUser(userId));
    }

    // ── Get Single Alert ──────────────────────────────────
    @Operation(summary = "Get a specific alert by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Alert found"),
            @ApiResponse(responseCode = "404", description = "Alert not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlertResponseDto> getAlertById(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {

        log.info("GET /api/alerts/{} — userId: {}", id, userId);
        return ResponseEntity.ok(alertService.getAlertById(id, userId));
    }

    // ── Update Alert ──────────────────────────────────────
    @Operation(summary = "Update an existing alert")
    @PutMapping("/{id}")
    public ResponseEntity<AlertResponseDto> updateAlert(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @Valid @RequestBody UpdateAlertRequestDto request) {

        log.info("PUT /api/alerts/{} — userId: {}", id, userId);
        return ResponseEntity.ok(alertService.updateAlert(id, userId, request));
    }

    // ── Pause / Reactivate Alert ──────────────────────────
    @Operation(summary = "Pause or reactivate an alert")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateAlertStatus(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id,
            @RequestParam AlertStatus status) {

        log.info("PATCH /api/alerts/{}/status — userId: {}, status: {}",
                id, userId, status);
        alertService.updateAlertStatus(id, userId, status);
        return ResponseEntity.noContent().build();
    }

    // ── Delete Alert ──────────────────────────────────────
    @Operation(summary = "Delete an alert (soft delete)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlert(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long id) {

        log.info("DELETE /api/alerts/{} — userId: {}", id, userId);
        alertService.deleteAlert(id, userId);
        return ResponseEntity.noContent().build();
    }

    // ── Get Triggered Alerts ──────────────────────────────
    @Operation(summary = "Get alert history (triggered alerts)")
    @GetMapping("/triggered")
    public ResponseEntity<List<AlertResponseDto>> getTriggeredAlerts(
            @RequestHeader("X-User-Id") Long userId) {

        log.info("GET /api/alerts/triggered — userId: {}", userId);
        return ResponseEntity.ok(alertService.getTriggeredAlerts(userId));
    }
}