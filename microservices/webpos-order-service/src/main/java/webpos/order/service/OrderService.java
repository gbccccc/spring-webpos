package webpos.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.order.database.OrderDB;
import webpos.order.pojo.Order;

@Service
public class OrderService {
    private OrderDB orderDB;

    @Autowired
    public void setOrderDB(OrderDB orderDB) {
        this.orderDB = orderDB;
    }

    private StreamBridge streamBridge;

    @Autowired
    public void setStreamBridge(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public Flux<Order> getOrdersByUserId(String userId) {
        return Flux.fromIterable(orderDB.getOrdersByUserId(userId));
    }

    public Flux<Order> getAllOrders() {
        return Flux.fromIterable(orderDB.getAllOrders());
    }

    public Mono<Order> getOrderById(String orderId) {
        return Mono.just(orderDB.getOrder(orderId));
    }

    public Mono<Boolean> addOrder(Order order) {
        String orderId = String.format("O%07d", orderDB.getOderNum());
        order.setOrderId(orderId);
        boolean success = orderDB.addOrder(order);
        if (success) {
            streamBridge.send("order", order);
            return Mono.just(true);
        }
        return Mono.just(false);
    }
}
