package com.stockportfolio.user.service;

import com.stockportfolio.user.dto.UpdateProfileRequestDto;
import com.stockportfolio.user.dto.UserPreferenceDto;
import com.stockportfolio.user.dto.UserProfileDto;
import com.stockportfolio.user.dto.UserRequestDto;
import com.stockportfolio.user.dto.UserResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {

    // CREATE USER
    UserResponseDto createUser(
            UserRequestDto requestDto);

    // GET ALL USERS WITH PAGINATION
    Page<UserResponseDto> getAllUsers(
            int page,
            int size);

    // GET USER BY ID
    UserResponseDto getUserById(Long id);

    // UPDATE USER
    UserResponseDto updateUser(
            Long id,
            UserRequestDto requestDto);

    // SOFT DELETE USER
    void deleteUser(Long id);

    // PROFILE APIs
    UserProfileDto getProfile(Long userId);

    UserProfileDto updateProfile(
            Long userId,
            UpdateProfileRequestDto dto);

    // PREFERENCES APIs
    UserPreferenceDto getPreferences(Long userId);

    UserPreferenceDto updatePreferences(
            Long userId,
            UserPreferenceDto dto);
}