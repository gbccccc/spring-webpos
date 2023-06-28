package delivery.orderconsumer;

import delivery.pojo.Delivery;
import delivery.pojo.Order;
import delivery.service.DeliveryService;

import java.util.function.Consumer;

public class OrderConsumer implements Consumer<Order> {
    private DeliveryService service;

    public OrderConsumer(DeliveryService service) {
        this.service = service;
    }

    @Override
    public void accept(Order order) {
        System.out.println(order.getOrderId());
        Delivery delivery = new Delivery("", order.getOrderId());
        service.addDelivery(delivery);
    }
}
