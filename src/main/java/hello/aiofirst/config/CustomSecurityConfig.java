package hello.aiofirst.config;

import hello.aiofirst.security.filter.JWTCheckFilter;
import hello.aiofirst.security.filter.LoginFilter;
import hello.aiofirst.security.handler.LoginFailHandler;

import hello.aiofirst.security.handler.CustomAccessDeniedHandler;
import hello.aiofirst.security.handler.LoginSuccessHandler;
import hello.aiofirst.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity
public class CustomSecurityConfig {
    private final JWTUtil jwtUtil;
    private final AuthenticationConfiguration authenticationManager;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("CustomSecurityConfig");

        http.csrf(csrf -> csrf.disable());
        http.httpBasic(basic -> basic.disable());

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/loginPage").permitAll()
                        .requestMatchers( "/js/**").permitAll()
                        .requestMatchers("/login","/refresh").permitAll()
                        .requestMatchers("/admin/**", "/").hasRole("ADMIN").anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/login")
                        .successHandler(new LoginSuccessHandler(jwtUtil))
                        .failureHandler(new LoginFailHandler())
                        .permitAll())
                .addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationManager), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(config -> config.accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }


}
