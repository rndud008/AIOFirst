package hello.aiofirst.dto;

import hello.aiofirst.domain.ProductAlpha;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Long id;
    private String productName;
    private String size;
    private String color;
    private int stockQuantity;

    private int consumerPrice;
    private int sellPrice;

    private CategoryDTO categoryDTO;

    @Builder.Default
    private List<MultipartFile> productImgs = new ArrayList<>();
    @Builder.Default
    private List<MultipartFile> productDescriptionImgs = new ArrayList<>();

    @Builder.Default
    private List<String> productImgFileNames = new ArrayList<>();
    @Builder.Default
    private List<String> productDescriptionImgFileNames = new ArrayList<>();

    @Builder.Default
    private List<String > productStatuses= new ArrayList<>();

    private List<ProductAlpha> productAlphas= new ArrayList<>();



}
