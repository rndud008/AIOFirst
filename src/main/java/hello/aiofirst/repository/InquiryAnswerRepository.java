package hello.aiofirst.repository;

import hello.aiofirst.domain.InquiryAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer,Long> {

    @Query("select ia from InquiryAnswer ia " +
            "left join fetch ia.inquiry " +
            "where ia.inquiry.id = :inquiryId")
    Optional<InquiryAnswer> getInquiryAnswer(@Param("inquiryId") Long inquiryId);
}
