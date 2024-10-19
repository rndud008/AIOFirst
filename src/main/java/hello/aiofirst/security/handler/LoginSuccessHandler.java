package hello.aiofirst.security.handler;

import com.google.gson.Gson;
import hello.aiofirst.domain.Admin;
import hello.aiofirst.domain.Role;
import hello.aiofirst.dto.AdminDTO;
import hello.aiofirst.security.auth.CustomDetails;
import hello.aiofirst.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private  final JWTUtil jwtUtil;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        log.info("onAuthenticationSuccess --------------------------------------------------------------------------------------");
        log.info("authentication ={}",authentication);

        CustomDetails customDetails = (CustomDetails) authentication.getPrincipal();

        AdminDTO adminDTO = new AdminDTO(customDetails.getAdmin().getId(),
                customDetails.getAdmin().getUsername(),
                customDetails.getAdmin().getPassword(),
                customDetails.getAdmin().getNickname(),
                customDetails.getAdmin().getRoles().stream().map(Role::name).toList());

        Map<String , Object> claims = adminDTO.getClaims();

        String accessToken = jwtUtil.generateToken(claims,10);
        String refreshToken = jwtUtil.generateToken(claims,60 * 24);

        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");

        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.close();

    }

}
