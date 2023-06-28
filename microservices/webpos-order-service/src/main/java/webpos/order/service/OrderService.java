package webpos.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import webpos.order.database.OrderDB;
import webpos.order.pojo.Order;

import java.util.List;

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

    public List<Order> getOrdersByUserId(String userId) {
        return orderDB.getOrdersByUserId(userId);
    }

    public List<Order> getAllOrders() {
        return orderDB.getAllOrders();
    }

    public Order getOrderById(String orderId) {
        return orderDB.getOrder(orderId);
    }

    public boolean addOrder(Order order) {
        String orderId = String.format("O%07d", orderDB.getOderNum());
        order.setOrderId(orderId);
        boolean success = orderDB.addOrder(order);
        if (success) {
            streamBridge.send("order", order);
            return true;
        }
        return false;
    }
}
