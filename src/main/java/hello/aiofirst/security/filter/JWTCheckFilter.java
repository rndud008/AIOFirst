package hello.aiofirst.security.filter;

import com.google.gson.Gson;
import hello.aiofirst.domain.Role;
import hello.aiofirst.dto.AdminDTO;
import hello.aiofirst.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.w3c.dom.ls.LSInput;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JWTCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeaderStr = request.getHeader("Authorization");

        String accessToken = null;
        String refreshToken = null;
        log.info("authHeaderStr ={} ---------------------------------------", authHeaderStr);

        if (authHeaderStr != null && authHeaderStr.startsWith("Bearer ")) {
            accessToken = authHeaderStr.substring(7);
        }

        if(accessToken == null || accessToken.trim().isEmpty()){
            Cookie[] cookies = request.getCookies();

            if(cookies != null){
                for(Cookie cookie : cookies){
                     if (cookie.getName().equals("accessToken")){
                        accessToken = cookie.getValue();
                    }
                    if (cookie.getName().equals("refreshToken")){
                        refreshToken = cookie.getValue();
                    }
                }
            }
        }

        if(accessToken != null){
            try {
                Map<String, Object> claims = jwtUtil.validateToken(accessToken);

                log.info("doFilterInternal claims={}", claims);

                Long id = Long.valueOf((Integer) claims.get("id")) ;
                String username = (String) claims.get("username");
                String nickname = (String) claims.get("nickname");
                String password = (String) claims.get("password");
                List<String> roleNames = (List<String>) claims.get("roles");

                log.info("roleNames = {}",roleNames);

                AdminDTO adminDTO = new AdminDTO(id, username.toUpperCase(), password, nickname, roleNames);

                log.info("adminDTO = {}", adminDTO);
                log.info("adminDTO.getAuthorities = {}", adminDTO.getAuthorities());

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(adminDTO, password, adminDTO.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {

//                Cookie[] cookies = request.getCookies();
//                if(cookies != null){
//                    for(Cookie cookie : cookies){
//                        if (cookie.getName().equals("refreshToken")){
//                            refreshToken = cookie.getValue();
//                        }
//                    }
//                }

                if (refreshToken != null){
                    try {

                        Map<String, Object> refreshClaims = jwtUtil.validateToken(refreshToken);

                        Long id = Long.valueOf((Integer) refreshClaims.get("id")) ;
                        String username = (String) refreshClaims.get("username");
                        String nickname = (String) refreshClaims.get("nickname");
                        String password = (String) refreshClaims.get("password");
                        List<String> roleNames = (List<String>) refreshClaims.get("roles");

                        AdminDTO adminDTO = new AdminDTO(id, username.toUpperCase(), password, nickname, roleNames);

                        UsernamePasswordAuthenticationToken authenticationToken
                                = new UsernamePasswordAuthenticationToken(adminDTO, password, adminDTO.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                        accessToken = jwtUtil.generateToken(refreshClaims,10);
                        refreshToken = jwtUtil.generateToken(refreshClaims,60*24);

                        Cookie accessCookie =  new Cookie("accessToken",accessToken);
                        accessCookie.setPath("/");
                        accessCookie.setSecure(false);

                        ResponseCookie cookie = ResponseCookie.from("refreshToken",refreshToken)
                                .httpOnly(true)
                                .secure(false)
                                .maxAge(Duration.ofDays(1))
                                .path("/")
                                .build();

                        response.addHeader("Set-Cookie",cookie.toString());
                        response.addCookie(accessCookie);


                    }catch (Exception refreshE){

                        Cookie cookie = new Cookie("accessToken",null);
                        cookie.setSecure(false);
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);

                        cookie = new Cookie("refreshToken",null);
                        cookie.setHttpOnly(true);
                        cookie.setSecure(false);
                        cookie.setPath("/");
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);

                    }
                }else {
                    Cookie cookie = new Cookie("accessToken",null);
                    cookie.setSecure(false);
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }

            }
        }


        filterChain.doFilter(request, response);

    }
}
