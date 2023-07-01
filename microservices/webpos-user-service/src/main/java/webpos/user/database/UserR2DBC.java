package webpos.user.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import webpos.user.pojo.User;

@Primary
@Repository
public class UserR2DBC implements UserDB{
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    public void setR2dbcEntityTemplate(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<User> getUser(String userId) {
        return r2dbcEntityTemplate.select(User.class).from("user")
                .matching(Query.query(Criteria.where("id").is(userId)))
                .one();
    }

    @Override
    public Mono<User> addUser(User user) {
        return r2dbcEntityTemplate.insert(User.class).into("user").using(user)
                .onErrorResume(e->Mono.empty());
    }
}
