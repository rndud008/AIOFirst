package hello.aiofirst.service;

import hello.aiofirst.domain.*;
import hello.aiofirst.dto.CategoryDTO;
import hello.aiofirst.dto.PageRequestDTO;
import hello.aiofirst.dto.PageResponseDTO;
import hello.aiofirst.dto.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductService {

    Long save(ProductDTO productDTO);

    void modify(ProductDTO productDTO);

    void remove(Long id);

    List<ProductDTO> getTopProductList(Long categoryId);
    List<ProductDTO> getSubProductList(Long categoryId);

    PageResponseDTO<ProductDTO> getPageProductList(PageRequestDTO pageRequestDTO, Long categoryId);

    ProductDTO getProduct(Long id);

    default ProductDTO entityToDTO(Product product) {

        ProductDTO productDTO =
                ProductDTO.builder()
                        .id(product.getId())
                        .productName(product.getProductName())
                        .color(product.getColor())
                        .size(product.getSize())
                        .stockQuantity(product.getStockQuantity())
                        .sellPrice(product.getSellPrice())
                        .consumerPrice(product.getConsumerPrice())
                        .categoryDTO(CategoryDTO.builder()
                                .id(product.getCategory().getId())
                                .categoryName(product.getCategory().getCategoryName())
                                .depNo(product.getCategory().getDepNo())
                                .build())
                        .build();

        List<ProductImg> productImgsmgs = product.getProductImgs();
        List<ProductDescriptionImg> productDescriptionImgs = product.getProductDescriptionImgs();

        product.getProductStatuses().forEach(status -> {
            productDTO.getProductStatuses().add(String.valueOf(status));
        });

        if (productImgsmgs != null || productImgsmgs.size() != 0) {
            List<String> fileNameList = productImgsmgs.stream()
                    .map(productImg -> productImg.getFileName()).collect(Collectors.toList());

            productDTO.setProductImgFileNames(fileNameList);
        }

        if (productDescriptionImgs != null || productDescriptionImgs.size() != 0) {
            List<String> fileNameList = productDescriptionImgs.stream()
                    .map(productImg -> productImg.getDescriptionFileName()).collect(Collectors.toList());
            productDTO.setProductImgFileNames(fileNameList);
        }



        return productDTO;
    }


    default Product dtoToEntity(ProductDTO productDTO) {

        Product product = Product.builder()
                .id(productDTO.getId())
                .productName(productDTO.getProductName())
                .color(productDTO.getColor())
                .size(productDTO.getSize())
                .stockQuantity(productDTO.getStockQuantity())
                .sellPrice(productDTO.getSellPrice())
                .consumerPrice(productDTO.getConsumerPrice())
                .category(Category.builder()
                        .id(productDTO.getCategoryDTO().getId())
                        .categoryName(productDTO.getProductName())
                        .depNo(productDTO.getCategoryDTO().getDepNo())
                        .build())
                .build();

        productDTO.getProductStatuses().forEach(status -> {
            product.getProductStatuses().add(ProductStatus.valueOf(status));
        });

        List<String> productImgFileNames = productDTO.getProductImgFileNames();
        List<String> productDescriptionImgFileNames = productDTO.getProductDescriptionImgFileNames();

        if (productImgFileNames != null || productImgFileNames.size() != 0) {
            productImgFileNames.forEach(fileName -> {
                product.addProductImgsString(fileName);
            });
        }

        if (productDescriptionImgFileNames != null || productDescriptionImgFileNames.size() != 0) {
            productDescriptionImgFileNames.forEach(fileName -> {
                product.addProductDescriptionImgsString(fileName);
            });
        }



        return product;
    }
}
