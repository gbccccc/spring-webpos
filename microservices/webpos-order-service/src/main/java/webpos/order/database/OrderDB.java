package webpos.order.database;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.order.pojo.Order;

import java.util.List;

public interface OrderDB {
    public Flux<Order> getAllOrders();

    public Flux<Order> getOrdersByUserId(String userId);

    public Mono<Order> getOrder(String orderId);

    public Mono<Order> addOrder(Order order);

    public Mono<Integer> getOrderNum();
}
