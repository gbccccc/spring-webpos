package webpos.user.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import webpos.user.pojo.User;
import webpos.user.service.UserService;

@RestController
public class UserController {
    final private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/password-check")
    public Mono<Boolean> passwordCheck(@RequestParam String userId, @RequestParam String password) {
        return service.passwordCheck(userId, password);
    }

    @PostMapping("/register")
    public Mono<Boolean> register(@RequestBody User user) {
        return service.register(user);
    }
}
