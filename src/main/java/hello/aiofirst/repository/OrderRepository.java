package hello.aiofirst.repository;

import hello.aiofirst.domain.Order;
import hello.aiofirst.domain.OrderStatus;
import hello.aiofirst.domain.Payment;
import hello.aiofirst.domain.PaymentStatus;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {


    @Query("select o, p from Order o " +
            "left join fetch Payment p on p.order.id = o.id " +
            "and p.paymentStatus in(:paymentStatuses) " +
            "order by o.createdAt desc ")
    List<Order> getOrderList(@Param("paymentStatuses")List<PaymentStatus> paymentStatuses);

    @Query("select o, p from Order o " +
            "left join fetch Payment p on p.order.id = o.id " +
            "and p.paymentStatus in(:paymentStatuses) " +
            "where o.orderStatus = :orderStatus " +
            "order by o.createdAt desc ")
    List<Order> getOrderList(@Param("paymentStatuses")List<PaymentStatus> paymentStatuses, @Param("orderStatus") OrderStatus orderStatus);

    @Query("select o from Order o " +
            "left join fetch o.address " +
            "where o.id = :orderId")
    Order getOrder(@Param("orderId") Long orderId);

}
