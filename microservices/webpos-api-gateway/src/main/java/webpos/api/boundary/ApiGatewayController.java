package webpos.api.boundary;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.api.dto.Order;
import webpos.api.dto.Product;
import webpos.api.remote.RemoteOrderService;
import webpos.api.remote.RemoteProductService;

import java.util.List;

@RestController
public class ApiGatewayController {
    RemoteProductService productService;
    RemoteOrderService orderService;

    @Autowired
    public void setRemoteProductService(RemoteProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setRemoteProductService(RemoteOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/products/all-products")
    public Mono<ResponseEntity<List<Product>>> allProducts() {
        return productService.getProducts().collectList().map(ResponseEntity::ok).
                defaultIfEmpty(ResponseEntity.notFound().build()).onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/products/{id}")
    public Mono<ResponseEntity<Product>> product(@PathVariable String id) {
        return productService.getProductById(id).map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders/{orderId}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId).map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public Mono<ResponseEntity<List<Order>>> getOrdersByUserId(@RequestParam(required = false) String userId) {
        Flux<Order> ordersFlux = userId == null ? orderService.getOrders() : orderService.getOrders(userId);
        return ordersFlux.collectList().map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/new-order")
    public Mono<ResponseEntity<Boolean>> addOrder(@RequestBody Order order) {
        return orderService.addOrder(order).map(ResponseEntity::ok)
                .onErrorReturn(ResponseEntity.notFound().build());
    }
}
