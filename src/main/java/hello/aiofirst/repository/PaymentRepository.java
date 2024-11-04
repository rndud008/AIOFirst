package hello.aiofirst.repository;

import hello.aiofirst.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

    @Query("select p from Payment p " +
            "left join fetch p.order " +
            "where p.order.id = :orderId")
    Payment findByOrderId(@Param("orderId") Long orderId);
}
