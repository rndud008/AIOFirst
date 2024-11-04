package hello.aiofirst.repository;

import hello.aiofirst.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByDepNo (Long depno);

    @Query("select c from Category c where c.depNo in(:depno)")
    List<Category> findCategoriesByDepNoAndInquery(@Param("depno") List<Long> depno);

    Optional<Category>  findByCategoryName(String name);

    @Query("select c from Category c where  c.depNo  not in (:numberList)")
    List<Category> getExcludeTopCategoryAndInquiry(@Param("numberList") List<Integer> numberList);

    @Query("select c from Category c where c.depNo = 0 and c.id <> :id")
    List<Category> getTopCategoryList(@Param("id") Long id);

}
