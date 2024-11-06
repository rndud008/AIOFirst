package hello.aiofirst.repository;

import hello.aiofirst.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {

    @Query("select i from Inquiry i " +
            "left join fetch i.category " +
            "order by i.createdAt desc ")
    List<Inquiry> getInquiryList();

    @Query("select i from Inquiry i " +
            "left join fetch i.category " +
            "where i.answer = :answer " +
            "order by i.createdAt desc ")
    List<Inquiry> getInquiryList(@Param("answer") boolean answer);

    @Query("select i from Inquiry i " +
            "left join fetch i.category " +
            "where i.id = :inquiryId")
    Optional<Inquiry> getInquiry(@Param("inquiryId") Long inquiryId);

}
