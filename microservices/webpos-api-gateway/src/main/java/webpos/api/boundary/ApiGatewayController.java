package webpos.api.boundary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.api.dto.Order;
import webpos.api.dto.OrderApplication;
import webpos.api.dto.Product;
import webpos.api.dto.User;
import webpos.api.remote.RemoteOrderService;
import webpos.api.remote.RemoteProductService;
import webpos.api.remote.RemoteUserService;

import java.util.List;

@Slf4j
@RestController
public class ApiGatewayController {
    RemoteUserService userService;
    RemoteProductService productService;
    RemoteOrderService orderService;

    @Autowired
    public void setRemoteUserService(RemoteUserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setRemoteProductService(RemoteProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setRemoteProductService(RemoteOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/products")

    public Mono<ResponseEntity<List<Product>>> allProducts(@RequestParam int pageId, @RequestParam int numPerPage) {
        return productService.getProducts(pageId, numPerPage).collectList().map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/products/{id}")
    public Mono<ResponseEntity<Product>> product(@PathVariable String id) {
        return productService.getProductById(id).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/orders/{orderId}")
    public Mono<ResponseEntity<Order>> getOrder(@PathVariable String orderId) {
        return orderService.getOrder(orderId).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @GetMapping("/orders")
    public Mono<ResponseEntity<List<Order>>> getOrdersByUserId(@RequestParam(required = false) String userId) {
        Flux<Order> ordersFlux = userId == null ? orderService.getOrders() : orderService.getOrders(userId);
        return ordersFlux.collectList().map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @PostMapping("/orders/new-order")
    public Mono<ResponseEntity<Order>> addOrder(@RequestBody OrderApplication orderApplication) {
        return orderService.addOrder(orderApplication).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatusCode.valueOf(401)).build())
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }

    @PostMapping("/users/register")
    public Mono<ResponseEntity<User>> register(@RequestBody User user) {
        return userService.register(user).map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.status(HttpStatusCode.valueOf(401)).build())
                .onErrorReturn(ResponseEntity.internalServerError().build());
    }
}
