package com.stockportfolio.user.feign;

import com.stockportfolio.user.dto.UserFeignResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserFeignClient {

    @GetMapping("/api/users/{id}")
    UserFeignResponseDto getUserById(
            @PathVariable Long id);
}