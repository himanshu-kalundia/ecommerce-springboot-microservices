package com.pcshop.userservice.service;

import com.pcshop.userservice.model.User;
import com.pcshop.userservice.repository.UserRepository;
import com.pcshop.userservice.security.CustomUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    public boolean userExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public User saveUser(String username, String rawPassword, String deliveryAddress, Set<String> roles) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setDeliveryAddress(deliveryAddress);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public UserDetails loadUserByUsername(String username) {
        return customUserDetailsService.loadUserByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
