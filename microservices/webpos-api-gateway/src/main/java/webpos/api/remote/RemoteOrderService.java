package webpos.api.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.api.dto.Order;

@Service
public class RemoteOrderService {
    private WebClient webClient;

    @Autowired
    @Qualifier("wbb")
    public void setWebClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    public Mono<Order> getOrder(String orderId) {
        return webClient.get().uri("http://order-service/orders/" + orderId).retrieve().bodyToMono(Order.class);
    }

    public Flux<Order> getOrders() {
        return webClient.get().uri("http://order-service/orders").retrieve().bodyToFlux(Order.class);
    }

    public Flux<Order> getOrders(String userId) {
        return webClient.get().uri("http://order-service/orders" + "?userId=" + userId)
                .retrieve().bodyToFlux(Order.class);
    }

    public Mono<Boolean> addOrder(Order order) {
        return webClient.post().uri("http://order-service/new-order", order).retrieve().bodyToMono(Boolean.class);
    }
}
