package hello.aiofirst.domain;

import hello.aiofirst.dto.CategoryDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    private String categoryName;
    private Long depNo;

    public void ChangeCategory(CategoryDTO categoryDTO){
        this.categoryName = categoryDTO.getCategoryName();
        this.depNo = categoryDTO.getDepNo();
    }

}
