package webpos.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class RemoteUserService {
    private WebClient webClient;

    @Autowired
    @Qualifier("wbb")
    public void setWebClient(WebClient.Builder webClientBuilder) {
        webClient = webClientBuilder.build();
    }

    public Mono<Boolean> passwordCheck(String userId, String password) {
        return webClient.get().uri("http://user-service/password-check?userId=" + userId + "&password=" + password)
                .retrieve().bodyToMono(Boolean.class);
    }
}
