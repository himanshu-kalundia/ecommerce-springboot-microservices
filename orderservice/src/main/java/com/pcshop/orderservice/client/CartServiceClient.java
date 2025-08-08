package com.pcshop.orderservice.client;

import com.pcshop.orderservice.dto.CartItemResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(
        name = "cartServiceClient",
        url = "http://localhost:8083"
)
public interface CartServiceClient {

    @GetMapping("/api/cart")
    List<CartItemResponse> getCartItems(@RequestHeader("Authorization") String bearerToken);
}
