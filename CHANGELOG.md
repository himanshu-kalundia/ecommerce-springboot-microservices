# Changelog

All notable changes to this project will be documented in this file.


## [Unreleased]
- Containerized all microservices using Docker. Added a docker-compose.yml to orchestrate and run the entire system with a single command. Configured MySQL as a containerized service with persistent storage using Docker volumes.

## [2025-08-08]
- API Gateway is also working now. Removed authentication from API Gateway. All authentication will be handled by services independently.
- Eureka Naming Server is working now. Configured instance names for each service in Eureka to fix the issue.
- Added Feign. Now this will be used instead of RestTemplate for communication between services.

## [2025-08-07]
- Inter-service communication is working now. There was an issue with the DTO structure mismatch, which is fixed now. Authentication is also working now.

## [2025-08-06]
- Added intercommunication between services, but it's not fully functional at the moment.

## [2025-08-05]
- Added Eureka Naming Server for service discovery (port 8761). But it's not fully functional at the moment.
- Added API Gateway (port 8080). Now all the services can be accessed at a single port 8080. There are some authentication issues when routing requests through the API gateway. So, currently, all authentication is disabled. It will be enabled back once the issue is resolved.

## [2025-07-30]
- Added Order Service (port 8084). Only buyers can place orders.
- Integrated Swagger UI for all services; future services will also include Swagger UI.
- Added Cart Service (port 8083). Only buyers can access their carts; sellers do not have carts.
- Added Product Service (port 8082). Only a seller can CREATE, UPDATE, or DELETE a product. All users can GET the products.
- Added JWT Authentication. Services from now on will have JWT authentication. Some APIs may still be open, which do not require authentication.
- Added User Service (port 8081). There are two types of users: Buyer and Seller. A user has to select this during signup.