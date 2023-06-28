package webpos.api.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webpos.api.dto.Order;
import webpos.api.dto.Product;
import webpos.api.remote.RemoteOrderService;
import webpos.api.remote.RemoteProductService;

import java.util.List;

@RestController
public class ApiGatewayController {
    RemoteProductService productService;
    RemoteOrderService orderService;

    private CircuitBreakerFactory circuitBreakerFactory;

    @Autowired
    public void setCircuitBreakerFactory(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @Autowired
    public void setRemoteProductService(RemoteProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setRemoteProductService(RemoteOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/products/all-products")
    public ResponseEntity<List<Product>> allProducts() {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> ResponseEntity.ok(productService.getProducts()),
                throwable -> ResponseEntity.notFound().build());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> product(@PathVariable String id) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> ResponseEntity.ok(productService.getProductById(id)),
                throwable -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> ResponseEntity.ok(orderService.getOrder(orderId)),
                throwable -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrdersByUserId(@RequestParam(required = false) String userId) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> ResponseEntity.ok(
                        userId == null ? orderService.getOrders() : orderService.getOrders(userId)
                ),
                throwable -> ResponseEntity.notFound().build());
    }

    @PostMapping("/new-order")
    public ResponseEntity<Boolean> addOrder(@RequestBody Order order) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        return circuitBreaker.run(() -> ResponseEntity.ok(orderService.addOrder(order)),
                throwable -> ResponseEntity.notFound().build());
    }
}
