package com.stockportfolio.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequestDto {

    private String name;

    private String phone;

    private String timezone;

    private String currency;
}