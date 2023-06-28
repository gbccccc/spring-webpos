package delivery.database;

import delivery.pojo.Delivery;

import java.util.List;

public interface DeliveryDB {
    List<Delivery> getAllDeliveries();

    Delivery getDelivery(String deliveryId);

    int getDeliveryNum();

    boolean addDelivery(Delivery delivery);
}
