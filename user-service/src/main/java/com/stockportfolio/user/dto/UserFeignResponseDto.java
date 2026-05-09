package com.stockportfolio.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFeignResponseDto {

    private Long id;
    private String name;
    private String email;
}