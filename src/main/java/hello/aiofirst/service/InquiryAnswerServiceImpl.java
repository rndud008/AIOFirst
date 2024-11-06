package hello.aiofirst.service;

import hello.aiofirst.domain.Admin;
import hello.aiofirst.domain.Inquiry;
import hello.aiofirst.domain.InquiryAnswer;
import hello.aiofirst.dto.InquiryAnswerRequestDTO;
import hello.aiofirst.repository.AdminRepository;
import hello.aiofirst.repository.InquiryAnswerRepository;
import hello.aiofirst.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InquiryAnswerServiceImpl implements InquiryAnswerService {
    private final AdminRepository adminRepository;
    private final InquiryRepository inquiryRepository;
    private final InquiryAnswerRepository inquiryAnswerRepository;

    @Override
    @Transactional
    public int save(String adminUsername, InquiryAnswerRequestDTO inquiryAnswerRequestDTO) {
        Admin admin = adminRepository.getWithRoles(adminUsername.toUpperCase());
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryAnswerRequestDTO.getInquiryId()).orElse(null);

        if (inquiry == null){
            return 0;
        }

        InquiryAnswer inquiryAnswer = InquiryAnswer.builder()
                .inquiry(inquiry)
                .content(inquiryAnswerRequestDTO.getAdminContent())
                .name(admin.getNickname())
                .build();

        inquiry.changeAnswer(true);

        inquiryAnswerRepository.save(inquiryAnswer);

        return 1;

    }

    @Override
    @Transactional
    public long modify(InquiryAnswerRequestDTO inquiryAnswerRequestDTO,String adminUsername) {

        Admin admin = adminRepository.getWithRoles(adminUsername.toUpperCase());

        InquiryAnswer inquiryAnswer = inquiryAnswerRepository.findById(inquiryAnswerRequestDTO.getInquiryAnswerId()).orElse(null);
        if(inquiryAnswer == null){
            return 0;
        }

        inquiryAnswer.changeValue(inquiryAnswerRequestDTO.getAdminContent(), admin.getNickname());

        return inquiryAnswer.getId();
    }
}
