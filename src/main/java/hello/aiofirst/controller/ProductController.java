package hello.aiofirst.controller;

import hello.aiofirst.domain.ProductStatus;
import hello.aiofirst.dto.PageRequestDTO;
import hello.aiofirst.dto.PageResponseDTO;
import hello.aiofirst.dto.ProductDTO;
import hello.aiofirst.service.CategoryService;
import hello.aiofirst.service.ProductService;
import hello.aiofirst.service.ProductVariantService;
import hello.aiofirst.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;
    private final ProductVariantService productVariantService;
    private final CategoryService categoryService;

    @GetMapping("/admin/product")
    public String adminProduct(Model model){

        model.addAttribute("productCheck", true);

        return "views/layout";
    }

    @GetMapping("/admin/product/save")
    public String productSaveFrom(Model model){

        model.addAttribute("productCheck", true);
        model.addAttribute("productSaveForm", true);

        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("productStatuses", ProductStatus.values());
        model.addAttribute("categoryList", categoryService.getExcludeInquryList());

        return "views/layout";
    }

    @PostMapping("/admin/product/save")
    public String productSave(@ModelAttribute ProductDTO productDTO){
        log.info("product register ={}",productDTO);

        List<MultipartFile> files = productDTO.getProductImgs();
        List<String > productImgFileNames = fileUtil.saveFiles(files);
        productDTO.setProductImgFileNames(productImgFileNames);

        files = productDTO.getProductDescriptionImgs();
        List<String> productDescriptionFileNames = fileUtil.saveFiles(files);
        productDTO.setProductDescriptionImgFileNames(productDescriptionFileNames);

        log.info("productImgFileNames ={}",productImgFileNames);
        log.info("productDescriptionFileNames ={}",productDescriptionFileNames);

        Long productId = productService.save(productDTO);
        productVariantService.save(productId);

        return "views/layout";
    }

    @GetMapping("/admin/product/menu")
    public String productSearch(Model model){

        model.addAttribute("productCheck", true);
        model.addAttribute("productSearch", true);

        model.addAttribute("categoryDTOS", categoryService.getTopCategoryList());

        return "views/layout";
    }

    @GetMapping("/admin/product/menu/{categoryId}")
    public String getProductDTOList(@ModelAttribute PageRequestDTO pageRequestDTO,@PathVariable("categoryId") Long categoryId, Model model,
                                     @RequestParam(name = "subcategory", required = false) Long subcategory){
        log.info("getProductDTOList = {}",pageRequestDTO);

        model.addAttribute("productCheck", true);
        model.addAttribute("productSearch", true);
        model.addAttribute("productSearchAllResult", true);

        log.info("subcategory={}",subcategory);

        if (subcategory != 0) {
            model.addAttribute("productDTOS",productService.getPageProductList(pageRequestDTO, subcategory, false));
        }else{
            model.addAttribute("productDTOS",productService.getPageProductList(pageRequestDTO, categoryId, true));
        }
        model.addAttribute("categoryDTOS", categoryService.getTopCategoryList());
        model.addAttribute("subCategoryDTOS", categoryService.getCategoryDepNoList(categoryId));

        return "views/layout";
    }

    @GetMapping("/admin/product/modify/{id}")
    public String productModifyForm(@PathVariable("id") Long id, Model model){

        model.addAttribute("productCheck", true);
        model.addAttribute("productModifyForm",true);

        model.addAttribute("productDTO", productService.getProduct(id));
        model.addAttribute("productStatuses", ProductStatus.values());
        model.addAttribute("categoryList", categoryService.getExcludeInquryList());

        return "views/layout";
    }




    @PostMapping("/admin/product/modify/{id}")
    public String productModify(@PathVariable Long id, @ModelAttribute ProductDTO productDTO){

        ProductDTO oldProductDTO = productService.getProduct(id);

        log.info("--------------------------------------------------productModify ={}",productDTO.getProductImgs().size());

        List<MultipartFile> files = productDTO.getProductImgs();
        List<String> currentProductImgFileNames = fileUtil.saveFiles(files);
        List<String> productImgFileNames = productDTO.getProductImgFileNames();
        if(currentProductImgFileNames != null && !currentProductImgFileNames.isEmpty()){
            productImgFileNames.addAll(currentProductImgFileNames);
        }

        files = productDTO.getProductDescriptionImgs();
        List<String> currentProductDescriptionImgFileNames = fileUtil.saveFiles(files);
        List<String> productDescriptionFileNames = productDTO.getProductDescriptionImgFileNames();
        if(currentProductDescriptionImgFileNames != null && !currentProductDescriptionImgFileNames.isEmpty()){
            productDescriptionFileNames.addAll(currentProductDescriptionImgFileNames);
        }

        productService.modify(productDTO);

        List<String> oldProductImgFileNames = oldProductDTO.getProductImgFileNames();
        if(oldProductImgFileNames != null && oldProductImgFileNames.size() >0){
            List<String> removeFiles =
                    oldProductImgFileNames.stream().filter(fileName -> productImgFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            fileUtil.deleteFiles(removeFiles);
        }

        List<String> oldProductDescriptionFileNames = oldProductDTO.getProductDescriptionImgFileNames();
        if(oldProductDescriptionFileNames != null && oldProductDescriptionFileNames.size() >0){
            List<String> removeFiles =
                    oldProductDescriptionFileNames.stream().filter(fileName -> productDescriptionFileNames.indexOf(fileName) == -1).collect(Collectors.toList());
            fileUtil.deleteFiles(removeFiles);
        }

        return "views/layout";

    }

    public Map<String ,String > remove(@PathVariable Long id){

//        List<String> oldFileNames = productService.getProduct(id).getUploadedFileNames();

        productService.remove(id);

//        fileUtil.deleteFiles(oldFileNames);

        return Map.of("reuslt","SUCCESS");
    }
}