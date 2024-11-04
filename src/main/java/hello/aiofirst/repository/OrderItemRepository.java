package hello.aiofirst.repository;

import hello.aiofirst.domain.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("select oi from OrderItem oi " +
            "left join fetch oi.order " +
            "left join fetch oi.productVariant " +
            "left join fetch oi.productVariant.product " +
            "where oi.order.id = :orderId")
    List<OrderItem> getOrderItemList(@Param("orderId") Long orderId);

}
