package hello.aiofirst.security.filter;

import com.google.gson.Gson;
import hello.aiofirst.domain.Role;
import hello.aiofirst.dto.AdminDTO;
import hello.aiofirst.security.auth.CustomDetails;
import hello.aiofirst.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println("password = " + password);
        System.out.println("username = " + username);

        Authentication token = new UsernamePasswordAuthenticationToken(username.toUpperCase(), password, new ArrayList<>());


        try {
            Authentication authResult = authenticationManager.authenticate(token);
            System.out.println("Authentication successful: " + authResult);
            return authResult; // 성공적으로 인증된 객체를 반환
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            throw e; // 인증 실패 시 예외를 던짐
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomDetails adminDetails = (CustomDetails) authResult.getPrincipal();

        log.info("successfulAuthentication adminDetails ={}",adminDetails);

        AdminDTO adminDTO = new AdminDTO(
                adminDetails.getAdmin().getId(),
                adminDetails.getAdmin().getUsername(),
                adminDetails.getAdmin().getPassword(),
                adminDetails.getAdmin().getNickname(),
                adminDetails.getAdmin().getRoles().stream().map(Role::name).toList());

        Map<String , Object> claims = adminDTO.getClaims();

        String accessToken = jwtUtil.generateToken(claims,10);
        String refreshToken = jwtUtil.generateToken(claims,60 * 24);

        Cookie accessCookie =  new Cookie("accessToken",accessToken);
        accessCookie.setPath("/");
        accessCookie.setSecure(false);

        ResponseCookie cookie = ResponseCookie.from("refreshToken",refreshToken)
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(1))
                .path("/")
                .build();

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("Set-Cookie",cookie.toString());
        response.addCookie(accessCookie);

        PrintWriter printWriter = response.getWriter();
        printWriter.println(jsonStr);
        printWriter.flush();

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("LoginFilter.unsuccessfulAuthentication() 호출: 인증 실패");

        response.setStatus(401);
    }
}
