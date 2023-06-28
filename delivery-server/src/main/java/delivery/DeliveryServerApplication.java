package delivery;

import delivery.orderconsumer.OrderConsumer;
import delivery.pojo.Order;
import delivery.service.DeliveryService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class DeliveryServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryServerApplication.class, args);
    }

    @Bean
    public Consumer<Order> orderConsumer(DeliveryService service) {
        return new OrderConsumer(service);
    }
}
