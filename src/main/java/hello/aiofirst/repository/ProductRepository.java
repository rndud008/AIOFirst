package hello.aiofirst.repository;

import hello.aiofirst.domain.Product;
import hello.aiofirst.domain.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>  {

    @EntityGraph(attributePaths = {"productStatuses","productAlphas","productImgs","category","productDescriptionImgs"})
    @Query("select p from Product p where p.id = :id")
    Optional<Product> getProduct(@Param("id") Long id);

    @Query("select p from Product p " +
            "left join p.productImgs pi " +
            "left join p.productAlphas pa " +
            "left join p.category pc " +
            "left join p.productDescriptionImgs pdi where pc.depNo= :categoryId and :status MEMBER OF p.productStatuses" )
    Page<Object[]> selectList(Pageable pageable, @Param("status")ProductStatus productStatus, @Param("categoryId") Long categoryId);

    @Query("select p,pi,pa,pdi,c from Product p " +
            "left join p.productImgs pi " +
            "left join p.productAlphas pa " +
            "left join p.productDescriptionImgs pdi " +
            "left join p.category c where c.depNo = :id")
    List<Product> getTopProductList(@Param("id") Long id);

    @Query("select p,pi,pa,pdi,c from Product p " +
            "left join p.productImgs pi " +
            "left join p.productAlphas pa " +
            "left join p.productDescriptionImgs pdi " +
            "left join p.category c where c.id = :id")
    List<Product> getSubProductList(@Param("id") Long id);

}
