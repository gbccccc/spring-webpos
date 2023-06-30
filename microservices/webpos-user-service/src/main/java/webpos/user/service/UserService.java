package webpos.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import webpos.user.database.UserDB;
import webpos.user.pojo.User;

@Service
public class UserService {
    private UserDB userDB;

    @Autowired
    public void setUserDB(UserDB userDB) {
        this.userDB = userDB;
    }

    public Mono<Boolean> passwordCheck(String userId, String password) {
        return Mono.just(userDB).map(
                userDB1 -> userDB1.getUser(userId)
        ).onErrorResume(e -> Mono.empty()).map(
                realUser -> realUser.getPassword().equals(password)
        ).defaultIfEmpty(false);
    }

    public Mono<Boolean> register(User user) {
        return Mono.just(userDB).map(
                userDB1 -> userDB1.addUser(user)
        );
    }
}
