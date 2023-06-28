package webpos.api.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import webpos.api.dto.Product;

import java.util.List;

@Service
public class RemoteProductService {
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("rt")
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getProducts() {
        String url = "http://product-service/products";
        ParameterizedTypeReference<List<Product>> responseBodyType = new ParameterizedTypeReference<>() {
        };
        return restTemplate.exchange(url,
                HttpMethod.GET, null, responseBodyType).getBody();
    }

    public Product getProductById(String productId) {
        String url = "http://product-service/products/" + productId;
        return restTemplate.exchange(url,
                HttpMethod.GET, null, Product.class).getBody();
    }
}
