package webpos.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.order.database.OrderDB;
import webpos.order.pojo.Order;
import webpos.order.pojo.OrderApplication;

@Service
public class OrderService {
    private OrderDB orderDB;

    private RemoteUserService userService;

    @Autowired
    public void setOrderDB(OrderDB orderDB) {
        this.orderDB = orderDB;
    }
    @Autowired
    public void setUserService(RemoteUserService userService) {
        this.userService = userService;
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

    public Mono<Boolean> addOrder(OrderApplication orderApplication) {
        return userService.passwordCheck(orderApplication.getOrder().getUserId(), orderApplication.getPassword())
                .onErrorReturn(false).map(checkSuccess -> {
                            if (!checkSuccess) {
                                return false;
                            }

                            Order order = orderApplication.getOrder();
                            String orderId = String.format("O%07d", orderDB.getOrderNum());
                            order.setOrderId(orderId);
                            boolean orderSuccess = orderDB.addOrder(order);
                            if (orderSuccess) {
                                streamBridge.send("order", order);
                                return true;
                            }
                            return false;
                        }
                );
    }
}
