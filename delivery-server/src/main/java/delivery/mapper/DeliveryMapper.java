package delivery.mapper;

import delivery.pojo.Delivery;

import java.util.List;

public interface DeliveryMapper {
    List<Delivery> getAllDeliveries();

    Delivery getDelivery(String deliveryId);

    int getDeliveryNum();

    boolean insertDelivery(Delivery delivery);
}
