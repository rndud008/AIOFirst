package hello.aiofirst.security.auth;

import com.google.gson.Gson;
import hello.aiofirst.domain.Admin;
import hello.aiofirst.domain.Member;
import hello.aiofirst.dto.AdminDTO;
import hello.aiofirst.dto.MemberDTO;
import hello.aiofirst.repository.AdminRepository;
import hello.aiofirst.repository.MemberRepository;
import hello.aiofirst.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminRepository.getWithRoles(username);

        if (admin == null){
            throw new UsernameNotFoundException("Not Found");
        }

        log.info("========================== loadUserByUsername");

        return new CustomDetails(admin);

    }
}
