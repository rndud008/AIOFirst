package hello.aiofirst.controller;

import hello.aiofirst.domain.Product;
import hello.aiofirst.dto.ProductVariantDTO;
import hello.aiofirst.dto.ProductVariantListDTO;
import hello.aiofirst.service.ProductService;
import hello.aiofirst.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class ProductVariantController {

    private final ProductVariantService productVariantService;
    private final ProductService productService;

    @GetMapping("/admin/product/variant/{id}")
    public String productVariantForm(@PathVariable("id") Long productId, Model model) {

        ProductVariantListDTO productVariantListDTO = new ProductVariantListDTO();
        productVariantListDTO.setExistingVariants(productVariantService.getProductIdVariantList(productId));
        model.addAttribute("productCheck", true);
        model.addAttribute("productVariantForm", true);

        model.addAttribute("productVariantListDTO", productVariantListDTO);


        return "views/layout";
    }

    @PostMapping("/admin/product/variant/{id}")
    public String productVariantModify(@PathVariable("id") Long productId
            , @ModelAttribute ProductVariantListDTO productVariantListDTO) {

        Product product = productService.dtoToEntity(productService.getProduct(productId));

        List<ProductVariantDTO> originalList = productVariantService.getProductIdVariantList(productId);

        //exist 수정 리스트
        List<ProductVariantDTO> modifyList = productVariantListDTO.getExistingVariants().stream().filter(pv -> pv.getProductVariantId() != null).toList();

        if (originalList.size() != modifyList.size()) {

            List<Long> originalIdList = originalList.stream().map(ProductVariantDTO::getProductVariantId).toList();
            List<Long> modifyIdList = modifyList.stream().map(ProductVariantDTO::getProductVariantId).toList();

            //exist 삭제 리스트
            List<Long> deleteIdList = originalIdList.stream().filter(id -> modifyIdList.indexOf(id) == -1).toList();

            productVariantService.changeDelFlag(deleteIdList);

        }

        productVariantService.modify(modifyList);

        // new 생성 리스트
        List<ProductVariantDTO> newList = productVariantListDTO.getNewVariants();

        if (!newList.isEmpty()) {
            productVariantService.saveAll(newList, product);
        }


        return "views/layout";
    }


}
