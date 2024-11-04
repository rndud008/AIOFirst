package hello.aiofirst.repository;

import hello.aiofirst.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PointRepository extends JpaRepository<Point,Long> {

    @Query("select p from Point p " +
            "left join fetch p.order " +
            "where p.order.id =:orderId")
    Point getPoint(@Param("orderId") Long orderId);

    @Query("select p from Point p " +
            "left join fetch p.order " +
            "left join fetch p.order.address " +
            "left join fetch p.order.address.member " +
            "where p.order.address.member.id = :memberId " +
            "order by p.createdAt limit 1")
    Point currentPoint(@Param("memberId") Long memberId);
}
