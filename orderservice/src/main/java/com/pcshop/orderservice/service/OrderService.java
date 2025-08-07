package com.pcshop.orderservice.service;

import com.pcshop.orderservice.dto.CartItemResponse;
import com.pcshop.orderservice.dto.UserProfileResponse;
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
        headers.setBearerAuth(jwtToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        System.out.println("JWT token = " + jwtToken);
        System.out.println("Headers being sent: " + headers);
        System.out.println("Reaching service.placeOrder");

        // 2. Fetch cart items from cart-service
        ResponseEntity<CartItemResponse[]> cartResponse = restTemplate.exchange(
                "http://localhost:8083/api/cart",
                HttpMethod.GET,
                entity,
                CartItemResponse[].class
        );

        System.out.println("Cart Items retrieved");

        CartItemResponse[] cartItems = cartResponse.getBody();
        if (cartItems == null || cartItems.length == 0) {
            throw new RuntimeException("Cart is empty");
        }

        System.out.println("Cart is not empty");

        // 3. Map cart items to order items
        List<OrderItem> items = Arrays.stream(cartItems).map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setProductId(cartItem.getProductId());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getPrice());
            item.setTotalPrice(cartItem.getPrice() * cartItem.getQuantity());
            return item;
        }).collect(Collectors.toList());

        for (OrderItem item : items) {
            System.out.println("OrderItem{id=" + item.getId()
                    + ", productId=" + item.getProductId()
                    + ", quantity=" + item.getQuantity()
                    + ", unitPrice=" + item.getUnitPrice()
                    + ", totalPrice=" + (item.getTotalPrice())
                    + "}");
        }

        // 4. Fetch user profile from user-service
        System.out.println("Fetching user profile");

        ResponseEntity<UserProfileResponse> userResponse = restTemplate.exchange(
                "http://localhost:8081/api/user/me",
                HttpMethod.GET,
                entity,
                UserProfileResponse.class
        );

        System.out.println("Received userResponse");

        UserProfileResponse userProfile = userResponse.getBody();
        System.out.println("User service response raw body: " + userProfile);

        if (userProfile == null || userProfile.getDeliveryAddress() == null) {
            throw new RuntimeException("Failed to retrieve user profile");
        }

        // 5. Create and persist the order
        System.out.println("Creating order");

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setTotalAmount(
                items.stream().mapToDouble(OrderItem::getTotalPrice).sum()
        );
        order.setDeliveryAddress(userProfile.getDeliveryAddress());
        order.setExpectedDeliveryDate(LocalDate.now().plusWeeks(1));

        // Establish bidirectional relationship
        items.forEach(item -> item.setOrder(order));

        System.out.println("Order created");

        return orderRepository.save(order);
    }


    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    // Nested DTOs for remote responses
//    public static class CartItemResponse {
//        private Long productId;
//        private int quantity;
//        private double price;
//
//        public Long getProductId() { return productId; }
//        public void setProductId(Long productId) { this.productId = productId; }
//
//        public int getQuantity() { return quantity; }
//        public void setQuantity(int quantity) { this.quantity = quantity; }
//
//        public double getPrice() { return price; }
//        public void setPrice(double price) { this.price = price; }
//    }

//    public static class UserProfileResponse {
//        private String username;
//        private String address;
//        private List<String> roles;
//
//        public String getUsername() { return username; }
//        public void setUsername(String username) { this.username = username; }
//
//        public String getAddress() { return address; }
//        public void setAddress(String address) { this.address = address; }
//
//        public List<String> getRoles() { return roles; }
//        public void setRoles(List<String> roles) { this.roles = roles; }
//    }
}
