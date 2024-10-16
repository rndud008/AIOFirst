package hello.aiofirst.domain;

import hello.aiofirst.dto.ProductVariantDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "product")
@Getter
@Builder
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_variant_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String size;
    private String color;
    private int price;
    private int stockQuantity;
    private boolean delFlag;

    public void changeProductVariant(ProductVariantDTO productVariantDTO){
        this.size = productVariantDTO.getSize();
        this.color = productVariantDTO.getColor();
        this.price = productVariantDTO.getPrice();
        this.stockQuantity = productVariantDTO.getStockQuantity();
    }
}
