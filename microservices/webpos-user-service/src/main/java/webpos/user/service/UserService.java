package webpos.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import webpos.user.database.UserDB;
import webpos.user.pojo.User;

import java.util.function.Function;

@Service
public class UserService {
    private UserDB userDB;

    @Autowired
    public void setUserDB(UserDB userDB) {
        this.userDB = userDB;
    }

    public Mono<Boolean> passwordCheck(String userId, String password) {
        return Mono.defer(
                () -> {
                    User user = userDB.getUser(userId);
                    return Mono.just(user != null && user.getPassword().equals(password));
                }
        );
    }

    public Mono<User> register(User user) {
        return Mono.defer(
                () -> Mono.just(userDB.addUser(user))
        ).flatMap(
                success -> success ? Mono.just(user) : Mono.empty()
        );
    }
}
