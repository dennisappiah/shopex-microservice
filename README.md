# Stock Trading Microservice API

A high-performance, reactive microservice API for stock trading built with Spring WebFlux and the Spring Reactive stack, with comprehensive unit and integration testing.

## Overview

This microservice provides a scalable and responsive platform for stock trading operations, designed to handle high throughput and real-time market data. Built on reactive programming principles, it offers non-blocking I/O operations, efficient resource utilization, and resilience under varying load conditions.

## Tech Stack

- **Framework**: Spring Boot with Spring WebFlux
- **Reactive Libraries**: Project Reactor (Flux and Mono)
- **Database**: Reactive MongoDB/R2DBC PostgreSQL
- **API Documentation**: SpringDoc OpenAPI (Swagger)
- **Testing**: JUnit 5, WebTestClient, Mockito, TestContainers
- **Build Tool**: Maven/Gradle
- **Containerization**: Docker

## Features

- ⚡ **Reactive API Endpoints**
  - Non-blocking operations
  - Backpressure handling
  - Event-driven architecture

- 📈 **Stock Trading Capabilities**
  - Real-time order processing
  - Portfolio management
  - Market data integration
  - Trade execution and settlement

- 🔄 **Streaming Data Support**
  - Real-time price updates
  - Portfolio performance tracking
  - Market events notifications

- 🔐 **Security**
  - Reactive authentication and authorization
  - Rate limiting
  - Request validation

- 📊 **Analytics**
  - Trading performance metrics
  - Usage statistics
  - System health monitoring

## Architecture

The service follows a clean, hexagonal architecture pattern:

1. **API Layer** - Reactive controllers with WebFlux
2. **Service Layer** - Business logic with reactive programming models
3. **Repository Layer** - Reactive data access
4. **Domain Layer** - Core business entities
5. **Infrastructure Layer** - External service integration

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven 3.8+ or Gradle 7.0+
- Docker and Docker Compose (for containerized deployment and testing)

### Installation

1. Clone the repository
   ```
   git clone https://github.com/yourusername/stock-trading-api.git
   cd stock-trading-api
   ```

2. Build the application
   ```
   # If using Maven
   mvn clean install

   # If using Gradle
   ./gradlew build
   ```

3. Run the application
   ```
   # If using Maven
   mvn spring-boot:run

   # If using Gradle
   ./gradlew bootRun
   ```

4. Access the API documentation at `http://localhost:8080/swagger-ui.html`

### Docker Deployment

1. Build the Docker image
   ```
   docker build -t stock-trading-api .
   ```

2. Run the container
   ```
   docker run -p 8080:8080 stock-trading-api
   ```

## Testing Approach

This project emphasizes thorough testing with 100% test coverage:

### Unit Testing

- **Service Layer Tests**: Verifying business logic with mocked repositories
- **Repository Layer Tests**: Testing data access logic with test databases
- **Utility Class Tests**: Ensuring helper functions work as expected

### Integration Testing

- **API Endpoint Tests**: Using WebTestClient to verify controller behavior
- **Database Integration**: Testing with TestContainers to simulate real database operations
- **External Service Integration**: Mocking external APIs for predictable testing

### Performance Testing

- **Load Testing**: Verifying system performance under heavy load
- **Concurrency Testing**: Ensuring proper handling of concurrent requests
- **Reactive Streams Testing**: Validating backpressure and cancellation handling

### Example Test

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockOrderControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void createOrder_shouldReturnCreatedOrder() {
        OrderRequest request = new OrderRequest(
            "AAPL", 
            OrderType.BUY, 
            10, 
            150.00
        );

        webTestClient.post()
            .uri("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), OrderRequest.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(OrderResponse.class)
            .value(response -> {
                assertNotNull(response.getId());
                assertEquals("AAPL", response.getSymbol());
                assertEquals(OrderType.BUY, response.getType());
                assertEquals(10, response.getQuantity());
                assertEquals(150.00, response.getPrice());
            });
    }
}
```

## API Endpoints

### Authentication
- `POST /api/auth/login` - Authenticate user
- `POST /api/auth/register` - Register new user

### Stock Operations
- `GET /api/stocks` - Get available stocks
- `GET /api/stocks/{symbol}` - Get stock details
- `GET /api/stocks/{symbol}/price` - Get real-time stock price
- `GET /api/stocks/{symbol}/history` - Get historical price data

### Order Management
- `POST /api/orders` - Place new order
- `GET /api/orders` - Get all orders for current user
- `GET /api/orders/{id}` - Get order details
- `DELETE /api/orders/{id}` - Cancel order

### Portfolio
- `GET /api/portfolio` - Get user portfolio summary
- `GET /api/portfolio/performance` - Get portfolio performance metrics
- `GET /api/portfolio/positions` - Get current positions

### Watchlists
- `POST /api/watchlists` - Create watchlist
- `GET /api/watchlists` - Get all watchlists
- `PUT /api/watchlists/{id}` - Update watchlist
- `DELETE /api/watchlists/{id}` - Delete watchlist

### Market Data Streams
- `GET /api/stream/prices` - Stream real-time price updates (Server-Sent Events)
- `GET /api/stream/orders` - Stream order status updates (WebSocket)

## Reactive Programming Patterns

- **Publisher-Subscriber Model**: Using Flux for multi-element streams and Mono for single-element results
- **Backpressure Handling**: Managing overwhelming producers with strategies like buffer, drop, or latest
- **Error Handling**: Comprehensive error handling with onErrorResume, onErrorReturn, etc.
- **Concurrency Control**: Using publishOn() and subscribeOn() for thread management
- **Caching**: Implementing smart caching strategies for frequently accessed data

## Configuration

Configuration is managed through `application.yml`:

```yaml
spring:
  webflux:
    base-path: /api
  data:
    mongodb:
      uri: mongodb://localhost:27017/stocktrading
      
server:
  port: 8080
  
logging:
  level:
    org.springframework.data.mongodb.core.ReactiveMongoTemplate: DEBUG
    org.springframework.web.reactive: DEBUG

app:
  security:
    jwt:
      secret: your-secret-key-with-at-least-32-characters
      expiration: 86400000
  market:
    data:
      refresh-rate-ms: 1000
      sources:
        - alpha-vantage
        - yahoo-finance
```

## Monitoring and Observability

- Integrated with Micrometer for metrics collection
- Prometheus endpoint for metrics scraping
- Distributed tracing with Spring Cloud Sleuth and Zipkin
- Reactive health checks for system status monitoring

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request



## License

This project is licensed under the MIT License - see the LICENSE file for details.


