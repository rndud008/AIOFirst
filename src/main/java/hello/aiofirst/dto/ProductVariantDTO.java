package hello.aiofirst.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantDTO {

    private String  productName;
    private Long  productId;

    private Long productVariantId;
    private String size;
    private String color;
    private int price;
    private int stockQuantity;


}
