package hello.aiofirst.controller;

import hello.aiofirst.util.CustomJSWTException;
import hello.aiofirst.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class RefreshController {

    private final JWTUtil jwtUtil;

    @GetMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,
                                       String refreshToken) {

        if (refreshToken == null) {
            throw new CustomJSWTException("NULL_REFRESH");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJSWTException("INVALID STRING");
        }

        String accessToken = authHeader.substring(7);

        if(checkExpiredToken(accessToken) == false){
            return Map.of("accessToken",accessToken, "refreshToken",refreshToken);
        }

        Map<String, Object> claims = jwtUtil.validateToken(refreshToken);

        log.info("refresh claims ={}",claims);
        String newAccessToken = jwtUtil.generateToken(claims,10);
        String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? jwtUtil.generateToken(claims, 60 * 24) : refreshToken;


        return Map.of("accessToken",newAccessToken, "refreshToken",newRefreshToken);
    }

    private boolean checkTime(Integer exp){
        Date expDate = new Date((long) exp * 1000);

        long gap = expDate.getTime() - System.currentTimeMillis();

        long leftMin = gap / (1000 * 60);

        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try {
            jwtUtil.validateToken(token);

        } catch (CustomJSWTException e) {
            if (e.getMessage().equals("Expired")) {
                return true;
            }
        }

        return false;
    }
}
