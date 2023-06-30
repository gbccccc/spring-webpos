package webpos.api.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import webpos.api.dto.User;

@Service
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

    public Mono<Boolean> register(User user) {
        return webClient.post().uri("http://user-service/register").bodyValue(user)
                .retrieve().bodyToMono(Boolean.class);
    }
}
