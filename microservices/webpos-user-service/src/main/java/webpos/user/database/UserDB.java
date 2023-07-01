package webpos.user.database;

import reactor.core.publisher.Mono;
import webpos.user.pojo.User;

import java.util.List;

public interface UserDB {
    public Mono<User> getUser(String userId);

    public Mono<User> addUser(User user);
}
