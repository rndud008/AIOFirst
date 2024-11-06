package hello.aiofirst.service;

import hello.aiofirst.dto.InquiryAnswerRequestDTO;

public interface InquiryAnswerService {
    int save(String adminUsername, InquiryAnswerRequestDTO inquiryAnswerRequestDTO);

    long modify(InquiryAnswerRequestDTO inquiryAnswerRequestDTO,String adminUsername);
}
