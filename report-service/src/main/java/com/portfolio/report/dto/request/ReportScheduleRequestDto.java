package com.portfolio.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportScheduleRequestDto {

    @NotBlank(message = "Cron expression is required")
    private String cronExpression;

    private boolean enabled;
}