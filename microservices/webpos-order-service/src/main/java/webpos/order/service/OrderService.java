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
        return Mono.just(orderDB).flatMapIterable(
                orderDB1 -> orderDB1.getOrdersByUserId(userId)
        );
    }

    public Flux<Order> getAllOrders() {
        return Mono.just(orderDB).flatMapIterable(OrderDB::getAllOrders);
    }

    public Mono<Order> getOrderById(String orderId) {
        return Mono.just(orderDB).map(
                orderDB1 -> orderDB1.getOrder(orderId)
        ).onErrorResume(e -> Mono.empty());
    }

    public Mono<Boolean> addOrder(Order order) {
        return Mono.just(orderDB).map(
                orderDB1 -> {
                    String orderId = String.format("O%07d", orderDB1.getOrderNum());
                    order.setOrderId(orderId);
                    boolean success = orderDB1.addOrder(order);
                    if (success) {
                        streamBridge.send("order", order);
                        return true;
                    }
                    return false;
                }
        );
    }
}
