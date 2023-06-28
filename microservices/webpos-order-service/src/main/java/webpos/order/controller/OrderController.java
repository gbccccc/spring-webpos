package webpos.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webpos.order.pojo.Order;
import webpos.order.service.OrderService;

import java.util.List;

@RestController
public class OrderController {
    final private OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable String orderId) {
        Order order = service.getOrderById(orderId);
        return order == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(order);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getOrdersByUserId(@RequestParam(required = false) String userId) {
        List<Order> orders = userId == null ? service.getAllOrders() : service.getOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/new-order")
    public ResponseEntity<Boolean> addOrder(@RequestBody Order order) {
        return ResponseEntity.ok(service.addOrder(order));
    }
}
