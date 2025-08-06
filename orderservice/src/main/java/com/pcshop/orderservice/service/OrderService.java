package com.pcshop.orderservice.service;

import com.pcshop.orderservice.model.Order;
import com.pcshop.orderservice.model.OrderItem;
import com.pcshop.orderservice.repository.OrderRepository;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository, RestTemplate restTemplate) {
        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public Order placeOrder(String userId, String jwtToken) {
        // 1. Prepare Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken); // use passed token
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 2. Fetch cart items from cart-service
        ResponseEntity<CartItemResponse[]> cartResponse = restTemplate.exchange(
                "http://CART-SERVICE/api/cart",
                HttpMethod.GET,
                entity,
                CartItemResponse[].class
        );

        CartItemResponse[] cartItems = cartResponse.getBody();
        if (cartItems == null || cartItems.length == 0) {
            throw new RuntimeException("Cart is empty");
        }

        // 3. Fetch user profile from user-service
        ResponseEntity<UserProfileResponse> userResponse = restTemplate.exchange(
                "http://USER-SERVICE/api/user/me",
                HttpMethod.GET,
                entity,
                UserProfileResponse.class
        );

        UserProfileResponse userProfile = userResponse.getBody();
        if (userProfile == null || userProfile.getAddress() == null) {
            throw new RuntimeException("Failed to retrieve user profile");
        }

        // 4. Map cart items to order items
        List<OrderItem> items = Arrays.stream(cartItems).map(i -> {
            OrderItem item = new OrderItem();
            item.setProductId(i.getProductId());
            item.setQuantity(i.getQuantity());
            item.setPrice(i.getPrice());
            return item;
        }).collect(Collectors.toList());

        // 5. Create order
        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setTotalAmount(
                items.stream().mapToDouble(i -> i.getQuantity() * i.getPrice()).sum()
        );
        order.setDeliveryAddress(userProfile.getAddress());
        order.setExpectedDeliveryDate(LocalDate.now().plusWeeks(1));

        items.forEach(item -> item.setOrder(order));  // set order ref in items

        return orderRepository.save(order);
    }


    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Nested DTOs for remote responses
    public static class CartItemResponse {
        private Long productId;
        private int quantity;
        private double price;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
    }

    public static class UserProfileResponse {
        private String username;
        private String address;
        private List<String> roles;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }

        public List<String> getRoles() { return roles; }
        public void setRoles(List<String> roles) { this.roles = roles; }
    }
}
