package com.stockportfolio.user.mapper;

import com.stockportfolio.user.dto.UserRequestDto;
import com.stockportfolio.user.dto.UserResponseDto;
import com.stockportfolio.user.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequestDto dto) {

        User user = new User();

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }

    public static UserResponseDto toResponse(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}