package delivery.service;

import delivery.database.DeliveryDB;
import delivery.pojo.Delivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {

    private DeliveryDB deliveryDB;

    @Autowired
    public void setDeliveryDB(DeliveryDB deliveryDB) {
        this.deliveryDB = deliveryDB;
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryDB.getAllDeliveries();
    }

    public Delivery getDelivery(String id) {
        return deliveryDB.getDelivery(id);
    }

    public boolean addDelivery(Delivery delivery) {
        String deliveryId = String.format("D%07d", deliveryDB.getDeliveryNum());
        delivery.setDeliveryId(deliveryId);
        return deliveryDB.addDelivery(delivery);
    }
}
