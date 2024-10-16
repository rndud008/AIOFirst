package hello.aiofirst.service;

import hello.aiofirst.domain.Product;
import hello.aiofirst.domain.ProductVariant;
import hello.aiofirst.dto.ProductVariantDTO;
import hello.aiofirst.repository.ProductRepository;
import hello.aiofirst.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;

    @Override
    @Transactional
    public void save(Long productId) {
        Optional<Product> productResult = productRepository.findById(productId);
        Product product = productResult.orElseThrow();

        productVariantRepository.saveAll(productToProductVariant(product));

    }

    @Override
    public void saveAll(List<ProductVariantDTO> productVariantDTOS, Product product) {

        List<ProductVariant> productVariantList = new ArrayList<>();
        for (ProductVariantDTO productVariantDTO : productVariantDTOS){

            ProductVariant productVariant = createProductVariant(product, productVariantDTO.getSize(), productVariantDTO.getColor(), productVariantDTO.getPrice(), productVariantDTO.getStockQuantity());

            productVariantList.add(productVariant);
        }

        productVariantRepository.saveAll(productVariantList);

    }

    @Override
    @Transactional
    public void modify(List<ProductVariantDTO> productVariantDTOS) {

        List<ProductVariant> productVariantList = productVariantRepository
                .findAllById(productVariantDTOS.stream().map(ProductVariantDTO::getProductVariantId).collect(Collectors.toList()));

        for (ProductVariantDTO productVariantDTO : productVariantDTOS) {
            ProductVariant productVariant = productVariantList.stream()
                    .filter(pv -> pv.getId().equals(productVariantDTO.getProductVariantId())).findFirst().orElseThrow();
            productVariant.changeProductVariant(productVariantDTO);
        }

    }

    @Override
    @Transactional
    public void changeDelFlag(List<Long> productVariantIds) {

        for (Long productVariantId : productVariantIds) {

            productVariantRepository.changeDelFlag(productVariantId);
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantDTO> getProductIdVariantList(Long productId) {

        List<ProductVariant> productVariantList = productVariantRepository.getProductIdVariantList(productId);

        List<ProductVariantDTO> productVariantDTOS = new ArrayList<>();

        for (ProductVariant productVariant : productVariantList) {
            productVariantDTOS.add(entityToDTO(productVariant));
        }

        return productVariantDTOS;
    }
}
