package hello.aiofirst.security.filter;

import com.google.gson.Gson;
import hello.aiofirst.domain.Role;
import hello.aiofirst.dto.AdminDTO;
import hello.aiofirst.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.w3c.dom.ls.LSInput;

import java.io.IOException;
import java.io.PrintWriter;
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
        log.info("authHeaderStr ={} ---------------------------------------", authHeaderStr);

        if (authHeaderStr != null && authHeaderStr.startsWith("Bearer ")) {
            accessToken = authHeaderStr.substring(7);
        }

        if(accessToken == null){
            Cookie[] cookies = request.getCookies();

            if(cookies != null){
                for(Cookie cookie : cookies){
                    if (cookie.getName().equals("accessToken")){
                        accessToken = cookie.getValue();
                        break;
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

                AdminDTO adminDTO = new AdminDTO(id, username, nickname, password, roleNames);

                log.info("adminDTO = {}", adminDTO);
                log.info("adminDTO.getAuthorities = {}", adminDTO.getAuthorities());

                UsernamePasswordAuthenticationToken authenticationToken
                        = new UsernamePasswordAuthenticationToken(adminDTO, password, adminDTO.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {

                log.error("JWT Check Error ={}", e.getMessage());

                Gson gson = new Gson();
                String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

                response.setContentType("application/json");
                PrintWriter printWriter = response.getWriter();
                printWriter.println(msg);
                printWriter.close();

            }
        }


        filterChain.doFilter(request, response);

    }
}
