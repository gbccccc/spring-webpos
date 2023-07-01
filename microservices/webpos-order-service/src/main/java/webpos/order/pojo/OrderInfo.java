package webpos.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("order_info")
public class OrderInfo {
    @Column("id")
    private String orderId;
    @Column("userId")
    private String userId;
}
