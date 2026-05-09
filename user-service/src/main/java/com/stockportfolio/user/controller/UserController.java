package com.stockportfolio.user.controller;

import com.stockportfolio.user.dto.UpdateProfileRequestDto;
import com.stockportfolio.user.dto.UserPreferenceDto;
import com.stockportfolio.user.dto.UserProfileDto;
import com.stockportfolio.user.dto.UserRequestDto;
import com.stockportfolio.user.dto.UserResponseDto;
import com.stockportfolio.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE USER
    @PostMapping
    public UserResponseDto createUser(
            @Valid
            @RequestBody UserRequestDto requestDto) {

        return userService.createUser(requestDto);
    }

    // GET ALL USERS WITH PAGINATION
    @GetMapping
    public Page<UserResponseDto> getAllUsers(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "5")
            int size) {

        return userService.getAllUsers(page, size);
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public UserResponseDto getUserById(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public UserResponseDto updateUser(
            @PathVariable Long id,

            @Valid
            @RequestBody UserRequestDto requestDto) {

        return userService.updateUser(
                id,
                requestDto);
    }

    // SOFT DELETE USER
    @DeleteMapping("/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        userService.deleteUser(id);

        return "User soft deleted successfully";
    }

    // GET PROFILE
    @GetMapping("/profile/{id}")
    public UserProfileDto getProfile(
            @PathVariable Long id) {


        return userService.getProfile(id);
    }

    // UPDATE PROFILE
    @PutMapping("/profile/{id}")
    public UserProfileDto updateProfile(
            @PathVariable Long id,

            @Valid
            @RequestBody UpdateProfileRequestDto dto) {

        return userService.updateProfile(id, dto);
    }
    @GetMapping("/internal/{id}")
    public UserResponseDto getInternalUser(
            @PathVariable Long id) {

        return userService.getUserById(id);
    }

    // GET PREFERENCES
    @GetMapping("/preferences/{id}")
    public UserPreferenceDto getPreferences(
            @PathVariable Long id) {

        return userService.getPreferences(id);
    }

    // UPDATE PREFERENCES
    @PutMapping("/preferences/{id}")
    public UserPreferenceDto updatePreferences(
            @PathVariable Long id,

            @RequestBody UserPreferenceDto dto) {

        return userService.updatePreferences(id, dto);
    }
}