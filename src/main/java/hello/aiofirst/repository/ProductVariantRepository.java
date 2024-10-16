package hello.aiofirst.repository;

import hello.aiofirst.domain.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {


    @Query("select pv from ProductVariant pv " +
            "left join fetch Product p on pv.product.id = p.id where pv.product.id = :productId and pv.delFlag = false ")
    List<ProductVariant> getProductIdVariantList(@Param("productId") Long productId);

    @Modifying
    @Query("update ProductVariant pv set pv.delFlag = false where pv.id = :id")
    void changeDelFlag(@Param("id") Long id);
}
