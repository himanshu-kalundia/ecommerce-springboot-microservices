package com.pcshop.userservice.controller;

import com.pcshop.userservice.model.User;
import com.pcshop.userservice.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("api/user/me")
//    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
//        String username = authentication.getName();
//        User user = userService.findByUsername(username);
//
//        if (user == null) {
//            return ResponseEntity.notFound().build();
//        }
//
//        return ResponseEntity.ok(Map.of(
//                "username", user.getUsername(),
//                "roles", user.getRoles(),
//                "deliveryAddress", user.getDeliveryAddress()
//        ));
//    }

    @GetMapping("/api/user/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        System.out.println("🔍 Authentication: " + authentication);
        if (authentication == null) {
            return ResponseEntity.status(401).body("Unauthenticated");
        }

        String username = authentication.getName();
        System.out.println("🔍 Authenticated Username: " + username);

        User user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        // Log for debugging
        System.out.println("Found User: " + user.getUsername() + ", Address: " + user.getDeliveryAddress());

        Map<String, Object> response = new HashMap<>();
        response.put("username", user.getUsername());
        response.put("deliveryAddress", user.getDeliveryAddress());
        response.put("roles", user.getRoles());

        return ResponseEntity.ok(response);
    }

}
