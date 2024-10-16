package hello.aiofirst.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantListDTO {

   private List<ProductVariantDTO> existingVariants = new ArrayList<>();
   private List<ProductVariantDTO> newVariants = new ArrayList<>();


}
