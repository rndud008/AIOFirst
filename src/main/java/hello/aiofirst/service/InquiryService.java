package hello.aiofirst.service;

import hello.aiofirst.dto.InquiryDTO;
import hello.aiofirst.dto.InquiryDetailDTO;

import java.util.List;

public interface InquiryService {

    List<InquiryDTO> getInquiryListDTO(boolean noanswer);

    InquiryDetailDTO getInquiryDetailDTO(String adminUsername, Long inquiryId);
    InquiryDetailDTO getInquiryDetailDTO( Long inquiryId);
}
