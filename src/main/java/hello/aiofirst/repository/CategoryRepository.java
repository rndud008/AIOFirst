package hello.aiofirst.repository;

import hello.aiofirst.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<List<Category>> findCategoriesByDepNo (Long depno);

}
