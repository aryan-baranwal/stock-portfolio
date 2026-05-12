package com.stockportfolio.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferenceDto {

    private Boolean emailNotifications;

    private Boolean priceAlertEmail;

    private Boolean dailySummaryEmail;

    private String preferredCurrency;
}