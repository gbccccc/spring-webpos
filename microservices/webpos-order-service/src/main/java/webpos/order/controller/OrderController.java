package webpos.order.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.order.pojo.Order;
import webpos.order.pojo.OrderApplication;
import webpos.order.service.OrderService;

@RestController
public class OrderController {
    final private OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/orders/{orderId}")
    public Mono<Order> getOrder(@PathVariable String orderId) {
        return service.getOrderById(orderId);
    }

    @GetMapping("/orders")
    public Flux<Order> getOrdersByUserId(@RequestParam(required = false) String userId) {
        return userId == null ? service.getAllOrders() : service.getOrdersByUserId(userId);
    }

    @PostMapping("/new-order")
    public Mono<Order> addOrder(@RequestBody OrderApplication orderApplication) {
        return service.addOrder(orderApplication);
    }
}
