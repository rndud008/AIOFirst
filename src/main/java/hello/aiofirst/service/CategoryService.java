package hello.aiofirst.service;

import hello.aiofirst.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getCategoryList(Long dpeno);

    void save(CategoryDTO categoryDTO);

    void modify(CategoryDTO categoryDTO);

    CategoryDTO getCategory(Long id);

    void remove(Long id);
}
