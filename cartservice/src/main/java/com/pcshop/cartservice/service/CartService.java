package com.pcshop.cartservice.service;

import com.pcshop.cartservice.client.ProductServiceClient;
import com.pcshop.cartservice.dto.AddToCartRequest;
import com.pcshop.cartservice.dto.CartItemResponse;
import com.pcshop.cartservice.dto.ProductResponse;
import com.pcshop.cartservice.model.CartItem;
import com.pcshop.cartservice.repository.CartItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository repository;
    private final ProductServiceClient productClient;

    public CartService(CartItemRepository repository, ProductServiceClient productClient) {
        this.repository = repository;
        this.productClient = productClient; // or inject it as a @Bean
    }

    public List<CartItemResponse> getCartItems(String userId) {
        List<CartItem> items = repository.findByUserId(userId);

        return items.stream().map(item -> {
            ProductResponse product = fetchProductDetails(item.getProductId());

            CartItemResponse response = new CartItemResponse();
            response.setId(item.getId());
            response.setProductId(item.getProductId());
            response.setProductName(product.getName());
            response.setBrand(product.getBrand());
            response.setCategory(product.getCategory());
            response.setDescription(product.getDescription());
            response.setPrice(product.getPrice());
            response.setQuantity(item.getQuantity());
            response.setTotalPrice(product.getPrice() * item.getQuantity());

            return response;
        }).collect(Collectors.toList());
    }

    public CartItemResponse addToCart(String userId, AddToCartRequest request) {
        CartItem item = new CartItem();
        item.setUserId(userId);
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());

        CartItem savedItem = repository.save(item);
        ProductResponse product = fetchProductDetails(savedItem.getProductId());

        CartItemResponse response = new CartItemResponse();
        response.setId(savedItem.getId());
        response.setProductId(savedItem.getProductId());
        response.setProductName(product.getName());
        response.setBrand(product.getBrand());
        response.setCategory(product.getCategory());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setQuantity(savedItem.getQuantity());
        response.setTotalPrice(product.getPrice() * savedItem.getQuantity());

        return response;
    }

    public void removeItem(Long itemId) {
        repository.deleteById(itemId);
    }

    @Transactional
    public void clearCart(String userId) {
        repository.deleteByUserId(userId);
    }

    private ProductResponse fetchProductDetails(Long productId) {
        return productClient.getProductById(productId);
    }
}
