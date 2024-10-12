package hello.aiofirst.controller;


import hello.aiofirst.domain.ProductStatus;
import hello.aiofirst.dto.CategoryDTO;
import hello.aiofirst.dto.PageRequestDTO;
import hello.aiofirst.dto.ProductDTO;
import hello.aiofirst.service.CategoryService;
import hello.aiofirst.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    @GetMapping("/")
    @ResponseBody
    public void home() {

    }

    @GetMapping("/admin")
    public String admin() {

        return "views/admin/index";
    }

    @GetMapping("/admin/fragment/{fragmentName}")
    public String getFragment(@PathVariable String fragmentName, Model model, @RequestParam(name = "id", required = false) Long id, @RequestParam(name = "subcategory", required = false) Long subcategory) {

        switch (fragmentName) {
            case "productManageMent":
                break;
            case "categoryManageMent":
                break;
            case "userManageMent":
                break;
            case "orderManageMent":
                break;
            case "inquiryManageMent":
                break;
            case "statistics":
                break;
            case "categorySave":
                model.addAttribute("categoryDTO", new CategoryDTO());
                model.addAttribute("categoryDTOS", categoryService.getCategoryList(0l));
                break;
            case "categoryModify":
                model.addAttribute("categoryDTO", categoryService.getCategory(id));
                model.addAttribute("categoryDTOS", categoryService.getCategoryList(0l));
                break;
            case "categorySearch":
                model.addAttribute("categoryDTOS", categoryService.getCategoryList(0l));
                break;
            case "productSave":
                model.addAttribute("productDTO", new ProductDTO());
                model.addAttribute("productStatuses", ProductStatus.values());
                model.addAttribute("categoryList", categoryService.getExcludeInquryList());
                break;
            case "productModify":
                break;
            case "productSearch":
                model.addAttribute("categoryDTOS", categoryService.getTopCategoryList());
                break;
            case "productSearchAllResult":

                log.info("subcategory={}",subcategory);

                if (subcategory != 0) {
                    model.addAttribute("productDTOS",productService.getSubProductList(subcategory));
                }else{
                    model.addAttribute("productDTOS",productService.getTopProductList( id));
                }
                    model.addAttribute("categoryDTOS", categoryService.getCategoryDepNoList(id));
                break;
            default:

        }

        return "views/admin/fragments :: " + fragmentName;
    }

    @PostMapping("/category/save")
    public String categorySave(@ModelAttribute CategoryDTO categoryDTO) {
        log.info("categorySave categoryDTO ={}", categoryDTO);

        categoryService.save(categoryDTO);

        return "views/admin/index";

    }

    @PutMapping("/category/modify")
    public String categoryModify(@ModelAttribute CategoryDTO categoryDTO) {
        log.info("categoryModify categoryDTO ={}", categoryDTO);

        categoryService.modify(categoryDTO);

        return "views/admin/index";

    }

    @DeleteMapping("/category/delete")
    public String categoryRemove(@RequestParam("id") Long id) {
        log.info("categoryRemove id = {}", id);

        categoryService.remove(id);

        return "views/admin/index";
    }


}
