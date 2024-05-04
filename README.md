# ACME Project Repository
This project is the codebase for a microservices application developed in Java, focusing solely on backend APIs for writing reviews. The application is designed with scalability, resilience, and modularity in mind, utilizing various technologies and architectural patterns.

## Key Features

- Microservices Architecture: The application is built as a collection of loosely coupled microservices, each responsible for a specific domain: User, Product, Review, and Vote.

- API Gateway: An API gateway is used for load balancing and routing requests to the appropriate microservice. This ensures efficient communication between clients and the backend services.

- Message Broker: RabbitMQ is employed as the message broker to facilitate asynchronous communication and event-driven architecture among microservices.

- Service Discovery: Eureka is utilized for service registration and discovery. Microservices register their endpoints with Eureka, enabling dynamic routing and load balancing.

- Polyglot Persistence: Multiple databases are employed, following the CQRS (Command Query Responsibility Segregation - One DB for queries and one for commands) pattern and database per service principle: Review microservice uses Neo4j, a graph database, for storing review-related data; User and Product microservices utilize MongoDB, a NoSQL document database, for storing user and product information; Vote microservice utilizes Redis, an in-memory data store, for storing voting-related data.

- Onion Architecture: Each microservice follows the Onion Architecture pattern, which emphasizes separation of concerns and domain-driven design.

- Strangler Fig Pattern: The application has been refactored from a monolithic architecture to microservices using the Strangler Fig Pattern. This approach gradually replaces components of the monolith with microservices, reducing risk and ensuring continuity of service.
