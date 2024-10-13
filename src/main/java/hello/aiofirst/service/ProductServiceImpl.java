package hello.aiofirst.service;

import hello.aiofirst.domain.*;
import hello.aiofirst.dto.CategoryDTO;
import hello.aiofirst.dto.PageRequestDTO;
import hello.aiofirst.dto.PageResponseDTO;
import hello.aiofirst.dto.ProductDTO;
import hello.aiofirst.repository.CategoryRepository;
import hello.aiofirst.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Long save(ProductDTO productDTO) {
        log.info("ProductServiceImpl save={}",productDTO);

        Product product = dtoToEntity(productDTO);

        Product result = productRepository.save(product);

        return result.getId();

    }

    @Override
    @Transactional
    public void modify(ProductDTO productDTO) {

        Optional<Product> result = productRepository.findById(productDTO.getId());
        Product product = result.orElseThrow();

        product.changeProduct(productDTO);

    }

    @Override
    @Transactional
    public void remove(Long id) {
        Optional<Product> result = productRepository.findById(id);
        Product product = result.orElseThrow();

        product.clearStatusList();
        product.changeStatus(ProductStatus.SELL_CLOSE);

    }

    @Override
    @Transactional
    public List<ProductDTO> getTopProductList(Long categoryId) {

        List<Product> products = productRepository.getTopProductList(categoryId);
        log.info("products={}",products);

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products){
            productDTOS.add(entityToDTO(product));
        }
        log.info("productDTOS={}",productDTOS);
        return productDTOS;
    }

    @Override
    @Transactional
    public List<ProductDTO> getSubProductList(Long categoryId) {

        List<Product> products = productRepository.getSubProductList(categoryId);
        log.info("products={}",products);

        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product : products){
            productDTOS.add(entityToDTO(product));
        }
        log.info("productDTOS={}",productDTOS);
        return productDTOS;

    }


    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductDTO> getPageProductList(PageRequestDTO pageRequestDTO, Long categoryId, boolean topCategory) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage()-1,pageRequestDTO.getSize(), Sort.by("id").descending());
        Page<Product> result = null;

        if (topCategory){
            result = productRepository.selectList(pageable, categoryId);
        }else {
            result = productRepository.selectSubList(pageable, categoryId);
        }


        log.info("getPageProductList result ={}",result);


        List<ProductDTO> productDTOS = result.stream().map(product ->{
            ProductDTO productDTO = null;

            Category category = product.getCategory();
            List<ProductImg> productImgs = product.getProductImgs();
            List<ProductAlpha> productAlphas = product.getProductAlphas();
            List<ProductDescriptionImg> productDescriptionImgs = product.getProductDescriptionImgs();
            List<ProductStatus> productStatuses = product.getProductStatuses();

            productDTO = ProductDTO.builder()
                    .id(product.getId())
                    .productName(product.getProductName())
                    .color(product.getColor())
                    .size(product.getSize())
                    .stockQuantity(product.getStockQuantity())
                    .sellPrice(product.getSellPrice())
                    .consumerPrice(product.getConsumerPrice())
                    .categoryDTO(CategoryDTO.builder()
                            .id(category.getId())
                            .categoryName(category.getCategoryName())
                            .depNo(category.getDepNo())
                            .build())
                    .productImgFileNames(productImgs.stream().map(ProductImg::getFileName).collect(Collectors.toList()))
                    .productDescriptionImgFileNames(productDescriptionImgs.stream().map(ProductDescriptionImg::getDescriptionFileName).collect(Collectors.toList()))
                    .productStatuses(productStatuses.stream().map(ProductStatus::name).collect(Collectors.toList()))
                    .build();

            return productDTO;
        }).collect(Collectors.toList());

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<ProductDTO>withAll()
                .dtoList(productDTOS)
                .pageRequestDTO(pageRequestDTO)
                .total(totalCount).build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProduct(Long id) {

        Optional<Product> result = productRepository.findById(id);

        Product product = result.orElseThrow();

        return entityToDTO(product);
    }
}
