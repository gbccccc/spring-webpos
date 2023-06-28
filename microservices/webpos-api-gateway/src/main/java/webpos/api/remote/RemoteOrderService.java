package webpos.api.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import webpos.api.dto.Order;

import java.util.List;

@Service
public class RemoteOrderService {
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("rt")
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Order getOrder(String orderId) {
        String url = "http://order-service/orders/" + orderId;
        return restTemplate.exchange(url,
                HttpMethod.GET, null, Order.class).getBody();
    }

    public List<Order> getOrders() {
        String url = "http://order-service/orders";
        ParameterizedTypeReference<List<Order>> responseBodyType = new ParameterizedTypeReference<>() {
        };
        return restTemplate.exchange(url,
                HttpMethod.GET, null, responseBodyType).getBody();
    }

    public List<Order> getOrders(String userId) {
        String url = "http://order-service/orders" + "?userId=" + userId;
        ParameterizedTypeReference<List<Order>> responseBodyType = new ParameterizedTypeReference<>() {
        };
        return restTemplate.exchange(url,
                HttpMethod.GET, null, responseBodyType).getBody();
    }

    public boolean addOrder(Order order) {
        String url = "http://order-service/new-order";
        return Boolean.TRUE.equals(restTemplate.postForEntity(url, order, Boolean.class).getBody());
    }
}
