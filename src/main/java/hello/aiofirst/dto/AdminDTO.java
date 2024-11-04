package hello.aiofirst.dto;

import lombok.Builder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDTO extends User {
    private Long id;
    private String username;
    private String password;
    private String nickname;

    private LocalDate createdAt;

    private List<String> roles = new ArrayList<>();


    public AdminDTO(Long id, String username, String password, String nickname, List<String> roles){
        super(
                username,
                password,
                roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_"+role)).toList()
        );

        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    public Map<String, Object> getClaims(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("id",id);
        dataMap.put("username",username);
        dataMap.put("password",password);
        dataMap.put("nickname",nickname);
        dataMap.put("roles",roles);

        return dataMap;
    }


}
