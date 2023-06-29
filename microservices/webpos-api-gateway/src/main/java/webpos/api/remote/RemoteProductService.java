package webpos.api.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.api.dto.Product;

import java.util.List;

@Service
public class RemoteProductService {
    private WebClient webClient;

    @Autowired
    @Qualifier("wbb")
    public void setWebClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    public Flux<Product> getProducts() {
        return webClient.get().uri("http://product-service/products").retrieve().bodyToFlux(Product.class);
    }

    public Mono<Product> getProductById(String productId) {
        return webClient.get().uri("http://product-service/products/" + productId).retrieve().bodyToMono(Product.class);
    }
}
