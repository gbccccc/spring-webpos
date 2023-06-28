package delivery.controller;

import delivery.pojo.Delivery;
import delivery.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DeliveryController {
    DeliveryService service;

    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    @GetMapping("/deliveries")
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        List<Delivery> deliveries = service.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    @GetMapping("/deliveries/{id}")
    public ResponseEntity<Delivery> getDelivery(@PathVariable String id) {
        Delivery delivery = service.getDelivery(id);
        return delivery == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(delivery);
    }
}
