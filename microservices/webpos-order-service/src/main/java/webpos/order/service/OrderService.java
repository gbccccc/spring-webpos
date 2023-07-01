package webpos.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.order.database.OrderDB;
import webpos.order.pojo.Order;
import webpos.order.pojo.OrderApplication;

import java.util.function.BiFunction;
import java.util.function.Consumer;

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
        return orderDB.getOrdersByUserId(userId);
    }

    public Flux<Order> getAllOrders() {
        return orderDB.getAllOrders();
    }

    public Mono<Order> getOrderById(String orderId) {
        return orderDB.getOrder(orderId);
    }

    public Mono<Order> addOrder(OrderApplication orderApplication) {
        Mono<Integer> numMono = orderDB.getOrderNum();
        return userService.passwordCheck(orderApplication.getOrder().getUserId(), orderApplication.getPassword())
                .onErrorReturn(false).flatMap(
                        checkSuccess -> checkSuccess ? Mono.just(true) : Mono.empty()
                ).zipWith(numMono,
                        (checkSuccess, orderNum) -> {
                            Order order = orderApplication.getOrder();
                            order.setOrderId(String.format("O%07d", orderNum));
                            return order;
                        }
                ).flatMap(
                        order -> orderDB.addOrder(order)
                );
    }
}
