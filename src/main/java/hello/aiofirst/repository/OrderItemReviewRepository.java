package hello.aiofirst.repository;

import hello.aiofirst.domain.OrderItemReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemReviewRepository extends JpaRepository<OrderItemReview,Long> {
}
