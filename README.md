# Overview

This is a continuation of https://github.com/programmerboi0987/e-commerce-microservices.git. That account is currently facing some technical issues.

This is an e-commerce microservices project built using Java Spring Boot.

The work is still in progress. Some parts of this project might not work.

## Update Logs

- Added user service (port 8081). There are two types of users: Buyer and Seller. A user has to select this during signup.
- Added JWT Authentication. Services from now on will have JWT authentication. Some APIs may still be open, which do not require authentication.
- Added product service (port 8082). Only the Seller can CREATE, UPDATE, or DELETE a product. All users can GET the products.
- 30 July 2025: Converting the previous monolithic e-commerce application to a microservices architecture.
- 30 July 2025: Added cart service (port 8083). Only a Buyer can access their cart. Sellers don't have carts.
- 30 July 2025: Added Swagger UI for all services. Future services will also have Swagger UI.
- 30 July 2025: Added order service (port 8084). Only a Buyer can put an order. 
- 5 July 2025: Added API Gateway (port 8080). Now all the services can be accessed at a single port 8080. There are some authentication issues when routing requests through the API gateway. So, currently, all authentication is disabled. It will be enabled back once the issue is resolved.
- 5 August 2025: Added Eureka Naming Server for service discovery (port 8761). But it's not fully functional at the moment.
- 6 August 2025: Changed a few fields in services. Now there will be communication among the services. But, there are some issues at the moment.
- 7 August 2025: Inter-service communication is working now. There was an issue with the DTO structure mismatch, which is fixed now.
- 8 August 2025: Added Feign. Now this will be used instead of RestTemplate for communication between services.
- 8 August 2025: Eureka Naming Server is working now. Configured instance names for each service in Eureka to fix the issue.
- 8 August 2025: API Gateway is also working now. Removed authentication from API Gateway. All authentication will be handled by services independently.

## Currently working on

- Creating a CHANGELOG.md for tracking these changes so that README.md will be cleaner.
- Thorough testing of APIs for finding any bugs.
- Work on improving the performance and security of APIs.
