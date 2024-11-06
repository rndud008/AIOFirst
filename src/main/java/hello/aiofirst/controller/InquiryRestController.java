package hello.aiofirst.controller;

import hello.aiofirst.dto.InquiryAnswerRequestDTO;
import hello.aiofirst.service.InquiryAnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/inquiries")
public class InquiryRestController {

    private final InquiryAnswerService inquiryAnswerService;

    @PostMapping("/save")
    public ResponseEntity<?> inquiryAnswerSave(@AuthenticationPrincipal UserDetails userDetails
            , @RequestBody InquiryAnswerRequestDTO inquiryAnswerRequestDTO){

        int result = inquiryAnswerService.save(userDetails.getUsername(), inquiryAnswerRequestDTO);

        if (result >0){
            return ResponseEntity.status(200).body("SUCCESS");
        }

        return ResponseEntity.status(404).body("FAIL");
    }

    @PostMapping("/modify")
    public ResponseEntity<?> inquiryAnswerModify(@RequestBody InquiryAnswerRequestDTO inquiryAnswerRequestDTO, @AuthenticationPrincipal UserDetails userDetails){

        long inquiryAnswerId = inquiryAnswerService.modify(inquiryAnswerRequestDTO,userDetails.getUsername());

        if (inquiryAnswerId > 0){
            return ResponseEntity.status(200).body(inquiryAnswerId);
        }
        
        return ResponseEntity.status(404).body("FAIL");
    }
}
