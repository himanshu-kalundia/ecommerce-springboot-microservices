package com.pcshop.orderservice.service;

import com.pcshop.orderservice.client.CartServiceClient;
import com.pcshop.orderservice.client.UserServiceClient;
import com.pcshop.orderservice.dto.CartItemResponse;
import com.pcshop.orderservice.dto.UserProfileResponse;
import com.pcshop.orderservice.model.Order;
import com.pcshop.orderservice.model.OrderItem;
import com.pcshop.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartServiceClient cartServiceClient;
    private final UserServiceClient userServiceClient;

    public OrderService(OrderRepository orderRepository,
                        CartServiceClient cartServiceClient,
                        UserServiceClient userServiceClient) {
        this.orderRepository = orderRepository;
        this.cartServiceClient = cartServiceClient;
        this.userServiceClient = userServiceClient;
    }

    public Order placeOrder(String userId, String jwtToken) {
        String bearerToken = "Bearer " + jwtToken;
        System.out.println("Reaching service.placeOrder");

        // 1. Fetch cart items from cart-service
        List<CartItemResponse> cartItems = cartServiceClient.getCartItems(bearerToken);
        System.out.println("Cart Items retrieved");

        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Map cart items to order items
        List<OrderItem> items = cartItems.stream().map(cartItem -> {
            OrderItem item = new OrderItem();
            item.setProductId(cartItem.getProductId());
            item.setQuantity(cartItem.getQuantity());
            item.setUnitPrice(cartItem.getPrice());
            item.setTotalPrice(cartItem.getPrice() * cartItem.getQuantity());
            return item;
        }).collect(Collectors.toList());

        items.forEach(item -> System.out.println("OrderItem{id=" + item.getId()
                + ", productId=" + item.getProductId()
                + ", quantity=" + item.getQuantity()
                + ", unitPrice=" + item.getUnitPrice()
                + ", totalPrice=" + item.getTotalPrice()
                + "}"));

        // 3. Fetch user profile
        System.out.println("Fetching user profile");
        UserProfileResponse userProfile = userServiceClient.getCurrentUser(bearerToken);
        System.out.println("User service response raw body: " + userProfile);

        if (userProfile == null || userProfile.getDeliveryAddress() == null) {
            throw new RuntimeException("Failed to retrieve user profile");
        }

        // 4. Create and persist the order
        System.out.println("Creating order");

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setTotalAmount(items.stream().mapToDouble(OrderItem::getTotalPrice).sum());
        order.setDeliveryAddress(userProfile.getDeliveryAddress());
        order.setExpectedDeliveryDate(LocalDate.now().plusWeeks(1));

        // Bidirectional binding
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
}
