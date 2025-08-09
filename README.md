
# Ecommerce Spring Boot Microservices

This project is a modular e-commerce platform built using Java Spring Boot, following a microservices architecture. It is a continuation of the work from [this repository](https://github.com/programmerboi0987/e-commerce-microservices.git), due to technical issues with the original account.

## Features

- User Service: Supports Buyer and Seller roles with role selection during signup.
- JWT Authentication: Secure access to APIs with token-based authentication.
- Product Service: Sellers can create, update, and delete products; all users can view products.
- Cart Service: Buyers can manage their shopping carts.
- Order Service: Buyers can place orders for products.
- API Gateway: Centralized entry point for all services.
- Eureka Naming Server: Service discovery for dynamic microservice registration.
- Inter-service Communication: Utilizes Feign clients for efficient communication between services.
- Swagger UI: Interactive API documentation for all services.

## Architecture

- Each service is independently deployable and scalable.
- Communication between services is handled via REST APIs and Feign clients.
- Service discovery is managed by Eureka.
- API Gateway routes requests to appropriate services and manages cross-cutting concerns.

## Getting Started

1. Clone the repository.
2. Build each service using Maven.
3. Start the Eureka Naming Server.
4. Start the API Gateway.
5. Start the individual microservices (User, Product, Cart, Order).
6. Access Swagger UI for API documentation at `/swagger-ui.html` for each service.

## Documentation

- For a detailed list of changes and ongoing development, please refer to the [CHANGELOG.md](./CHANGELOG.md).

## License

This project is licensed under the MIT License.
