package com.portfolio.notification.controller;

import com.portfolio.notification.dto.response.ApiResponse;
import com.portfolio.notification.dto.NotificationLogDto;
import com.portfolio.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Notifications", description = "Notification history and management")
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get notification history (paginated)")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationLogDto>>> getNotifications(
            @RequestHeader("X-User-Id") Long userId,
            Pageable pageable) {
        log.info("Get notifications for userId: {}", userId);
        return ResponseEntity.ok(ApiResponse.success(notificationService.getNotifications(userId, pageable)));
    }

    @Operation(summary = "Get specific notification")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationLogDto>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getNotificationById(id)));
    }

    @Operation(summary = "Mark notification as read")
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @Operation(summary = "Get unread notification count")
    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Long>> getUnreadCount(@RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(ApiResponse.success(notificationService.getUnreadCount(userId)));
    }
}