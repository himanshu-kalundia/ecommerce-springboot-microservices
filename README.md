# Overview

This is a continuation of https://github.com/programmerboi0987/e-commerce-microservices.git. That account is currently facing some technical issues.

This is an e-commerce microservices project made using Java SpringBoot.

The work is still in progress. Some parts of this project might not work.

## Update Logs

- Added user service (port 8081). There are two types of users: Buyer and Seller. A user has to select this during signup.
- Added JWT Authentication. Services from now on will have JWT authentication. Some APIs may still be open, which do not require authentication.
- Added product service (port 8082). Only the Seller can CREATE, UPDATE, or DELETE a product. All users can GET the products.
- Added cart service (port 8083). Only a Buyer can access their cart. Sellers don't have carts.
- Added order service (port 8084). Only a Buyer can put an order. 
- Added API Gateway (port 8080). There are some authentication issues when routing requests through the API gateway. So, currently, all authentication is disabled. It will be enabled back once the issue is resolved.

## Currently working on

- Resolving issues related to authentication and the API gateway.
- Adding Eureka Naming Server
