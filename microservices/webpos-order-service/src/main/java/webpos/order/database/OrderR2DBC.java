package webpos.order.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import webpos.order.pojo.Order;
import webpos.order.pojo.OrderDetail;
import webpos.order.pojo.OrderInfo;
import webpos.order.pojo.OrderItem;

import java.lang.reflect.GenericArrayType;
import java.util.List;
import java.util.function.BiFunction;

@Primary
@Repository
public class OrderR2DBC implements OrderDB {
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Autowired
    public void setR2dbcEntityTemplate(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Flux<Order> getAllOrders() {
        Flux<OrderInfo> orderInfoFlux = r2dbcEntityTemplate.select(OrderInfo.class).from("order_info").all();
        return assemblyOrder(orderInfoFlux);
    }

    @Override
    public Flux<Order> getOrdersByUserId(String userId) {
        Flux<OrderInfo> orderInfoFlux = r2dbcEntityTemplate.select(OrderInfo.class).from("order_info")
                .matching(Query.query(Criteria.where("userId").is(userId)))
                .all();
        return assemblyOrder(orderInfoFlux);
    }

    @Override
    public Mono<Order> getOrder(String orderId) {
        Flux<OrderInfo> orderInfoFlux = r2dbcEntityTemplate.select(OrderInfo.class).from("order_info")
                .matching(Query.query(Criteria.where("id").is(orderId)))
                .all();
        return assemblyOrder(orderInfoFlux).next();
    }

    @Override
    public Mono<Order> addOrder(Order order) {
        Mono<OrderInfo> orderInfoMono = r2dbcEntityTemplate.insert(OrderInfo.class).into("order_info")
                .using(new OrderInfo(order.getOrderId(), order.getUserId()));
        Flux<OrderItem> orderItemFlux = Flux.fromIterable(order.getItems()).flatMap(
                orderItem -> r2dbcEntityTemplate.insert(OrderDetail.class).into("order_detail")
                        .using(new OrderDetail(order.getOrderId(), orderItem.getAsin(), orderItem.getNum()))
        ).map(
                orderDetail -> new OrderItem(orderDetail.getAsin(), orderDetail.getNum())
        );
        return orderItemFlux.collectList().zipWith(orderInfoMono,
                (orderItems, orderInfo) -> new Order(orderInfo.getOrderId(), orderInfo.getUserId(), orderItems)
        );
    }

    @Override
    public Mono<Integer> getOrderNum() {
        return r2dbcEntityTemplate.select(OrderInfo.class).from("order_info").count()
                .map(Long::intValue);
    }

    private Flux<Order> assemblyOrder(Flux<OrderInfo> orderInfoFlux) {
        Flux<List<OrderItem>> orderItemFlux = orderInfoFlux.flatMap(
                orderInfo -> r2dbcEntityTemplate.select(OrderDetail.class).from("order_detail")
                        .matching(Query.query(Criteria.where("orderId").is(orderInfo.getOrderId())))
                        .all().map(
                                orderDetail -> new OrderItem(orderDetail.getOrderId(), orderDetail.getNum())
                        ).collectList()
        );
        return orderItemFlux.zipWith(orderInfoFlux,
                (orderItems, orderInfo) -> new Order(orderInfo.getOrderId(), orderInfo.getUserId(), orderItems)
        );
    }
}
