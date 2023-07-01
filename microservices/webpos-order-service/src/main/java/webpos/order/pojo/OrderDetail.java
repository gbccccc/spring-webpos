package webpos.order.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("order_detail")
public class OrderDetail {
    @Column("orderId")
    private String orderId;
    @Column("itemAsin")
    private String asin;
    @Column("itemNum")
    private int num;
}
