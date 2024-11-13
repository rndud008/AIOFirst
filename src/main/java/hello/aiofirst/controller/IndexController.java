package hello.aiofirst.controller;

import com.google.gson.Gson;
import hello.aiofirst.domain.Admin;
import hello.aiofirst.domain.Role;
import hello.aiofirst.dto.AdminDTO;
import hello.aiofirst.dto.LoginRequestDTO;
import hello.aiofirst.repository.AdminRepository;
import hello.aiofirst.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping
public class IndexController {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"/admin", "/"})
    public String adminMain() {

        return "views/layout";

    }
    @GetMapping("/loginPage")
    public String loginPage(Model model, @AuthenticationPrincipal UserDetails userDetails){

        if (userDetails == null){
            model.addAttribute("adminLgoinDTO",new LoginRequestDTO());
            return "views/loginPage";
        }
        return "views/layout";
    }

    @PostMapping("/join")
    public String join(@RequestBody LoginRequestDTO adminRequestDTO){

        Admin admin = new Admin().resisterAdmin(
                adminRequestDTO.getUsername().toUpperCase(), bCryptPasswordEncoder.encode(adminRequestDTO.getPassword()) ,"관리자", List.of(Role.ADMIN,Role.MEMBER)
        );

        adminRepository.save(admin);

        log.info("join ={}",admin);

        return "views/loginPage";

    }


}
