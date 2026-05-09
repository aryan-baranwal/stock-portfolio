package com.stockportfolio.user.service;

import com.stockportfolio.user.dto.UpdateProfileRequestDto;
import com.stockportfolio.user.dto.UserPreferenceDto;
import com.stockportfolio.user.dto.UserProfileDto;
import com.stockportfolio.user.dto.UserRequestDto;
import com.stockportfolio.user.dto.UserResponseDto;
import com.stockportfolio.user.entity.User;
import com.stockportfolio.user.mapper.UserMapper;
import com.stockportfolio.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    // CREATE USER
    @Override
    public UserResponseDto createUser(
            UserRequestDto requestDto) {

        User user = new User();

        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());

        // DEFAULT VALUES
        user.setDeleted(false);

        user.setEmailNotifications(true);
        user.setPriceAlertEmail(true);
        user.setDailySummaryEmail(false);

        user.setPreferredCurrency("INR");

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    // GET ALL USERS WITH PAGINATION
    @Override
    public Page<UserResponseDto> getAllUsers(
            int page,
            int size) {

        return userRepository
                .findByDeletedFalse(PageRequest.of(page, size))
                .map(UserMapper::toResponse);
    }

    // GET USER BY ID
    @Override
    public UserResponseDto getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        return UserMapper.toResponse(user);
    }

    // UPDATE USER
    @Override
    public UserResponseDto updateUser(
            Long id,
            UserRequestDto requestDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        user.setName(requestDto.getName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(requestDto.getPassword());

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    // SOFT DELETE USER
    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        user.setDeleted(true);

        userRepository.save(user);
    }

    // GET PROFILE
    @Override
    public UserProfileDto getProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        UserProfileDto dto = new UserProfileDto();

        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setTimezone(user.getTimezone());
        dto.setCurrency(user.getCurrency());

        return dto;
    }

    // UPDATE PROFILE
    @Override
    public UserProfileDto updateProfile(
            Long userId,
            UpdateProfileRequestDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setTimezone(dto.getTimezone());
        user.setCurrency(dto.getCurrency());

        User updatedUser = userRepository.save(user);

        UserProfileDto response = new UserProfileDto();

        response.setId(updatedUser.getId());
        response.setName(updatedUser.getName());
        response.setEmail(updatedUser.getEmail());
        response.setPhone(updatedUser.getPhone());
        response.setTimezone(updatedUser.getTimezone());
        response.setCurrency(updatedUser.getCurrency());

        return response;
    }

    // GET PREFERENCES
    @Override
    public UserPreferenceDto getPreferences(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        UserPreferenceDto dto = new UserPreferenceDto();

        dto.setEmailNotifications(user.isEmailNotifications());
        dto.setPriceAlertEmail(user.isPriceAlertEmail());
        dto.setDailySummaryEmail(user.isDailySummaryEmail());
        dto.setPreferredCurrency(user.getPreferredCurrency());

        return dto;
    }

    // UPDATE PREFERENCES
    @Override
    public UserPreferenceDto updatePreferences(
            Long userId,
            UserPreferenceDto dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"));

        user.setEmailNotifications(dto.getEmailNotifications());
        user.setPriceAlertEmail(dto.getPriceAlertEmail());
        user.setDailySummaryEmail(dto.getDailySummaryEmail());
        user.setPreferredCurrency(dto.getPreferredCurrency());

        userRepository.save(user);

        return dto;
    }
}