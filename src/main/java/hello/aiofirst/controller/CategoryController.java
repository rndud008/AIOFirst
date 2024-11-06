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

    @GetMapping("/admin/category")
    public String adminCategory(Model model) {

        model.addAttribute("categoryCheck", "true");

        return "views/layout";
    }

    @GetMapping("/admin/category/save")
    public String categorySaveForm(@ModelAttribute CategoryDTO categoryDTO, Model model) {

        model.addAttribute("categoryCheck", true);
        model.addAttribute("categorySaveForm", true);
        model.addAttribute("categoryDTO", new CategoryDTO());
        model.addAttribute("categoryDTOS", categoryService.getCategoryList(0L));

        return "views/layout";

    }

    @PostMapping("/admin/category/save")
    public String categorySave(@ModelAttribute CategoryDTO categoryDTO) {
        log.info("categorySave categoryDTO ={}", categoryDTO);

        categoryService.save(categoryDTO);

        return "views/layout";

    }

    @GetMapping("/admin/category/modify/{id}")
    public String categoryModifyForm(@ModelAttribute CategoryDTO categoryDTO, Model model, @PathVariable("id") Long id) {
        log.info("categoryModify categoryDTO ={}", categoryDTO);

        model.addAttribute("categoryCheck", true);
        model.addAttribute("categoryModifyForm", true);
        model.addAttribute("categoryDTO", categoryService.getCategory(id));
        model.addAttribute("categoryDTOS", categoryService.getCategoryList(0L));

        return "views/layout";

    }

    @PostMapping("/admin/category/modify")
    public String categoryModify(@ModelAttribute CategoryDTO categoryDTO) {
        log.info("categoryModify categoryDTO ={}", categoryDTO);

        categoryService.modify(categoryDTO);

        return "views/layout";

    }

    @DeleteMapping("/admin/category/delete")
    public String categoryRemove(@RequestParam("id") Long id) {
        log.info("categoryRemove id = {}", id);

        categoryService.remove(id);

        return "views/layout";
    }

    @GetMapping("/admin/categories")
    public String categoryList(Model model){
        model.addAttribute("categoryCheck", true);
        model.addAttribute("categorySearch", true);
        model.addAttribute("categoryDTOS", categoryService.getCategoryList(0L));

        return "views/layout";
    }







}
