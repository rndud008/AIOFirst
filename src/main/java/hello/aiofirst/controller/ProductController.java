package hello.aiofirst.controller;

import hello.aiofirst.dto.PageRequestDTO;
import hello.aiofirst.dto.PageResponseDTO;
import hello.aiofirst.dto.ProductDTO;
import hello.aiofirst.service.ProductService;
import hello.aiofirst.util.CustomFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class ProductController {

    private final CustomFileUtil fileUtil;
    private final ProductService productService;

    public ProductDTO getProductDTO(@PathVariable("id") Long id){

        return productService.getProduct(id);
    }

    @GetMapping("/products/{categoryId}")
    public PageResponseDTO<ProductDTO> getProductDTOList(PageRequestDTO pageRequestDTO,@PathVariable Long categoryId){
        log.info("getProductDTOList = {}",pageRequestDTO);
        return productService.getPageProductList(pageRequestDTO, categoryId);
    }

    @PostMapping("/product/save")
    public Map<String,Long> register(ProductDTO productDTO){
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

        return Map.of("result", productId);
    }


    public Map<String ,String> modify(@PathVariable Long id, @ModelAttribute ProductDTO productDTO){

        ProductDTO oldProductDTO = productService.getProduct(id);

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

        return Map.of("return" , "SUCCESS");

    }

    public Map<String ,String > remove(@PathVariable Long id){

//        List<String> oldFileNames = productService.getProduct(id).getUploadedFileNames();

        productService.remove(id);

//        fileUtil.deleteFiles(oldFileNames);

        return Map.of("reuslt","SUCCESS");
    }
}
