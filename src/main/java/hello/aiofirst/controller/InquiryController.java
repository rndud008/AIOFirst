package hello.aiofirst.controller;

import hello.aiofirst.dto.InquiryDetailDTO;
import hello.aiofirst.service.InquiryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("")
    public String inquiryPage(Model model) {

        model.addAttribute("inquiryCheck", true);
        model.addAttribute("inquiries", inquiryService.getInquiryListDTO(false));

        return "views/layout";
    }

    @GetMapping("/noanswer")
    public String noAnswerPage(Model model) {

        model.addAttribute("inquiryCheck", true);
        model.addAttribute("inquiries", inquiryService.getInquiryListDTO(true));

        return "views/layout";
    }

    @GetMapping("/detail/{inquiryId}")
    public String inquiryDetailPage(@PathVariable("inquiryId") Long inquiryId, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        model.addAttribute("inquiryDetailCheck", true);
        model.addAttribute("inquiryDetailDTO", inquiryService.getInquiryDetailDTO(userDetails.getUsername(), inquiryId));

        return "views/layout";
    }

    @GetMapping("/modifyform/{inquiryId}")
    public String inquiryModifyForm(@PathVariable("inquiryId") Long inquiryId, Model model) {


        InquiryDetailDTO inquiryDetailDTO = inquiryService.getInquiryDetailDTO(inquiryId);
        if (inquiryDetailDTO != null) {
            model.addAttribute("inquiryModifyFormCheck", true);
            model.addAttribute("inquiryDetailDTO", inquiryDetailDTO);
        }else {
            model.addAttribute("inquiryCheck", true);
            model.addAttribute("inquiries", inquiryService.getInquiryListDTO(false));
        }


        return "views/layout";
    }

}
