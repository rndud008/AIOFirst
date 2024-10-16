package hello.aiofirst.service;

import hello.aiofirst.domain.Product;
import hello.aiofirst.domain.ProductVariant;
import hello.aiofirst.dto.ProductDTO;
import hello.aiofirst.dto.ProductVariantDTO;

import java.util.*;

public interface ProductVariantService {

    void save(Long productId);

    void saveAll(List<ProductVariantDTO> productVariantDTOS,Product product);

    void modify(List<ProductVariantDTO> productVariantDTOS);

    void changeDelFlag(List<Long> productVariantId);

    List<ProductVariantDTO> getProductIdVariantList(Long productId);

    default List<ProductVariant> productToProductVariant(Product product) {


        List<String> colorList = Arrays.stream(product.getColor().split(",")).toList();
        List<String> sizeList = Arrays.stream(product.getSize().split(",")).toList();

        List<ProductVariant> productVariantList = new ArrayList<>();

        for (String color : colorList) {
            for (String size : sizeList) {

                ProductVariant productVariant = createProductVariant(product,size,color, null, product.getStockQuantity());
                productVariantList.add(productVariant);
            }
        }

        return productVariantList;

    }

    default ProductVariant createProductVariant(Product product,String size, String color, Integer price, Integer stock){

        if (price == null){
            price = 0;
        }

        ProductVariant productVariant = ProductVariant.builder()
                .product(product)
                .price(price)
                .size(size)
                .color(color)
                .stockQuantity(stock)
                .build();

        return productVariant;
    }

    default ProductVariantDTO entityToDTO(ProductVariant productVariant){

        ProductVariantDTO productVariantDTO = ProductVariantDTO.builder()
                .productName(productVariant.getProduct().getProductName())
                .productId(productVariant.getProduct().getId())
                .productVariantId(productVariant.getId())
                .color(productVariant.getColor())
                .size(productVariant.getSize())
                .price(productVariant.getPrice())
                .stockQuantity(productVariant.getStockQuantity())
                .build();

        return productVariantDTO;
    }


}
