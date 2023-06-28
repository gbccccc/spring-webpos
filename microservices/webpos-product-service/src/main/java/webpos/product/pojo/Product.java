package webpos.product.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String main_cat;

    private String title;

    private String asin;

    private List<String> category;

    private List<String> imageURLHighRes;
}
