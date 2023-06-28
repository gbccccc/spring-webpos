package webpos.order.database;

import webpos.order.pojo.Order;

import java.util.List;

public interface OrderDB {
    public List<Order> getAllOrders();

    public List<Order> getOrdersByUserId(String userId);

    public Order getOrder(String orderId);

    public boolean addOrder(Order order);

    public int getOderNum();
}
