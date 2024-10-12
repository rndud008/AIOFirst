package hello.aiofirst.service;

import hello.aiofirst.domain.Category;
import hello.aiofirst.dto.CategoryDTO;
import hello.aiofirst.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public List<CategoryDTO> getCategoryList(Long dpeno) {
        Optional<List<Category>> result = categoryRepository.findCategoriesByDepNo(dpeno);
        List<Category> categories = result.orElseThrow();

        log.info("getCategoryList ={}",categories);

        List<CategoryDTO> categoryDTOS = new ArrayList<>();

        if(categories.isEmpty()){
            return categoryDTOS;
        }

        for(Category category : categories){

            List<CategoryDTO> categoryDTOList = getCategoryDTOS(categoryRepository.findCategoriesByDepNo(category.getId()).orElseThrow());
            CategoryDTO categoryDTO = categoryToCategoryDTO(category,categoryDTOList);

            categoryDTOS.add(categoryDTO);
        }

        log.info("categoryDTOS ={}",categoryDTOS);

        return categoryDTOS;
    }

    @Override
    @Transactional
    public void save(CategoryDTO categoryDTO) {

        Category category = createCategory(categoryDTO);

        log.info("category save ={}",category);
        categoryRepository.save(category);

    }

    @Transactional
    @Override
    public void modify(CategoryDTO categoryDTO) {
        Optional<Category> result = categoryRepository.findById(categoryDTO.getId());

        Category category = result.orElseThrow();

        category.ChangeCategory(categoryDTO);

        log.info("modify category ={}",category);

    }

    @Override
    @Transactional
    public CategoryDTO getCategory(Long id) {

        Optional<Category> result = categoryRepository.findById(id);

        Category category = result.orElseThrow();

        log.info("getCategory ={}",category);

        Optional<List<Category>> listResult = categoryRepository.findCategoriesByDepNo(category.getId());

        List<Category> categoryList = listResult.orElseThrow();
        log.info("getCategory ={}",categoryList);

        List<CategoryDTO> categories = new ArrayList<>();

        if (!categoryList.isEmpty()){
            categories = getCategoryDTOS(categoryList);
        }

        CategoryDTO categoryDTO = categoryToCategoryDTO(category, categories);

        return categoryDTO;
    }

    @Override
    @Transactional
    public void remove(Long id) {

        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
            log.info("카테고리 삭제 완료.");
        }else {
            log.info("삭제할 카테고리가 존재하지 않음.");
        }

    }

    private static List<CategoryDTO> getCategoryDTOS(List<Category> listResult) {
        return listResult.stream().map(category -> CategoryDTO
                .builder()
                .categoryName(category.getCategoryName())
                .depNo(category.getDepNo())
                .id(category.getId()).build()
        ).collect(Collectors.toList());
    }

    private static CategoryDTO categoryToCategoryDTO(Category category, List<CategoryDTO> categories) {
        return CategoryDTO.builder()
                .id(category.getId())
                .depNo(category.getDepNo())
                .categoryName(category.getCategoryName())
                .subCategories(categories)
                .build();
    }

    private static Category createCategory(CategoryDTO categoryDTO) {
        return Category.builder()
                .depNo(categoryDTO.getDepNo())
                .categoryName(categoryDTO.getCategoryName())
                .build();
    }
}
