package com.pcshop.orderservice.client;

import com.pcshop.orderservice.dto.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/user/me")
    UserProfileResponse getCurrentUser(@RequestHeader("Authorization") String bearerToken);
}
