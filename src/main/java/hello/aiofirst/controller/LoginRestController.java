package hello.aiofirst.controller;

import com.google.gson.Gson;
import hello.aiofirst.domain.Admin;
import hello.aiofirst.dto.AdminDTO;
import hello.aiofirst.repository.AdminRepository;
import hello.aiofirst.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LoginRestController {

    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JWTUtil jwtUtil;
//
//    @PostMapping("/login")
//    public void login(@RequestParam String username, @RequestParam String password, Model model, HttpServletResponse response) throws IOException {
//
//        Admin admin = adminRepository.getWithRoles(username);
//
//        if (admin != null && bCryptPasswordEncoder.matches(password, admin.getPassword())) {
//
//            AdminDTO adminDTO = new AdminDTO(
//                    admin.getId(),
//                    admin.getUsername(),
//                    admin.getNickname(),
//                    admin.getPassword(),
//                    admin.getRoles().stream().map(role -> role.name()).collect(Collectors.toList())
//            );
//
//            Map<String, Object> claims = adminDTO.getClaims();
//            String accessToken = null;
//            String refreshToken = null;
//            try {
//
//                accessToken = jwtUtil.generateToken(claims, 10);
//                refreshToken = jwtUtil.generateToken(claims, 60 * 24);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            claims.put("accessToken", accessToken);
//            claims.put("refreshToken", refreshToken);
//
//            Gson gson = new Gson();
//
//            String jsonStr = gson.toJson(claims);
//
//            response.setContentType("application/json");
//            response.setStatus(HttpServletResponse.SC_OK);
//            response.getWriter().write(jsonStr);
//        } else {
//
//            response.setContentType("application/json");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("{\"error\": \"로그인 실패! 사용자 이름 또는 비밀번호를 확인하세요.\"}");
//        }
//    }
}
