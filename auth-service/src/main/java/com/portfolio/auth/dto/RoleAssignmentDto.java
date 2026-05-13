package com.portfolio.auth.dto;

import com.portfolio.auth.enums.RoleEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RoleAssignmentDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Role is required")
    private RoleEnum role;
}