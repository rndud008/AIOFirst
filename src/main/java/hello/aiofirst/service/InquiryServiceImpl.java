package hello.aiofirst.service;

import hello.aiofirst.domain.Admin;
import hello.aiofirst.domain.Inquiry;
import hello.aiofirst.domain.InquiryAnswer;
import hello.aiofirst.dto.InquiryDTO;
import hello.aiofirst.dto.InquiryDetailDTO;
import hello.aiofirst.repository.AdminRepository;
import hello.aiofirst.repository.InquiryAnswerRepository;
import hello.aiofirst.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository inquiryAnswerRepository;
    private final AdminRepository adminRepository;

    @Override
    public List<InquiryDTO> getInquiryListDTO(boolean noAnswer) {
        List<Inquiry> inquiries;
        if (noAnswer){
            inquiries = inquiryRepository.getInquiryList(!noAnswer);
        }else {
             inquiries = inquiryRepository.getInquiryList();
        }

        List<InquiryDTO> inquiryDTOS = new ArrayList<>();

        if(!inquiries.isEmpty()){
            for(Inquiry inquiry: inquiries){

                InquiryAnswer inquiryAnswer = inquiryAnswerRepository.getInquiryAnswer(inquiry.getId()).orElse(null);

                boolean inquiryAnswerCheck = inquiryAnswer != null;

                inquiryDTOS.add(getInquiryDTO(inquiry, inquiryAnswerCheck));
            }
        }

        return inquiryDTOS;
    }

    @Override
    public InquiryDetailDTO getInquiryDetailDTO(String adminUsername, Long inquiryId) {
        Admin admin = adminRepository.getWithRoles(adminUsername.toUpperCase());

        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId).orElse(null);
        if (inquiry == null){
            return null;
        }

        InquiryAnswer inquiryAnswer = inquiryAnswerRepository.getInquiryAnswer(inquiry.getId()).orElse(new InquiryAnswer());

        return InquiryDetailDTO.builder()
                .inquiryId(inquiry.getId())
                .categoryId(inquiry.getCategory().getId())
                .InquiryAnswerId(inquiryAnswer.getId() !=null ? inquiryAnswer.getId() : null)
                .username(inquiry.getName())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .createdAt(inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .adminUsername(inquiryAnswer.getName() !=null ? inquiryAnswer.getName() : admin.getNickname())
                .inquiryAnswer(inquiryAnswer.getId() != null)
                .adminContent(inquiryAnswer.getContent() !=null ? inquiryAnswer.getContent() : "")
                .adminCreatedAt(inquiryAnswer.getCreatedAt() !=null ?
                        inquiryAnswer.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                        : "")
                .build();
    }

    @Override
    public InquiryDetailDTO getInquiryDetailDTO(Long inquiryId) {

        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId).orElse(null);
        if (inquiry == null){
            return null;
        }

        InquiryAnswer inquiryAnswer = inquiryAnswerRepository.getInquiryAnswer(inquiry.getId()).orElse(null);
        if (inquiryAnswer == null){
            return null;
        }

        return InquiryDetailDTO.builder()
                .inquiryId(inquiry.getId())
                .categoryId(inquiry.getCategory().getId())
                .InquiryAnswerId(inquiryAnswer.getId())
                .username(inquiry.getName())
                .title(inquiry.getTitle())
                .content(inquiry.getContent())
                .createdAt(inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .adminUsername(inquiryAnswer.getName())
                .inquiryAnswer(inquiryAnswer.getId() != null)
                .adminContent(inquiryAnswer.getContent())
                .adminCreatedAt(inquiryAnswer.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .build();
    }

    private static InquiryDTO getInquiryDTO(Inquiry inquiry, boolean inquiryAnswerCheck) {

        return InquiryDTO.builder()
                .inquiryId(inquiry.getId())
                .categoryId(inquiry.getCategory().getId())
                .username(inquiry.getName())
                .categoryName(inquiry.getCategory().getCategoryName())
                .title(inquiry.getTitle())
                .createdAt(inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
                .inquiryAnswer(inquiryAnswerCheck)
                .build();
    }
}
