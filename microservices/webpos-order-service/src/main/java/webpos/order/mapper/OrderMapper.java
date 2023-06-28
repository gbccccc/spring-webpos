package webpos.order.mapper;

import webpos.order.pojo.Order;

import java.util.List;

public interface OrderMapper {
    List<Order> getAllOrders();

    List<Order> getOrdersByUserId(String userId);

    Order getOrder(String orderId);

    int getOrderNum();

    void insertOrder(Order order);
}
