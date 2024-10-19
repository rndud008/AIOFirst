package hello.aiofirst.dto;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDate;
import java.util.*;

public class MemberDTO {

    private Long id;
    private String username;
    private String nickname;
    private String password;
    private String email;
    private String provider;
    private String gender;
    private int phoneNumber;
    private LocalDate createdAt;
    private LocalDate birthday;
    private List<String> roleNames = new ArrayList<>();

    public MemberDTO(Long id, String username, String nickname, String password, String email, String provider, String gender, Integer phoneNumber, LocalDate createdAt, LocalDate birthday, List<String> roleNames){

//        super(
//                username,
//                password,
//                roleNames.stream().map(str -> new SimpleGrantedAuthority("ROLE_"+str)).toList()
//        );

        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.provider = provider;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.birthday = birthday;
        this.roleNames = roleNames;
    }

    public Map<String,Object> getClaims(){
        Map<String,Object> dataMap = new HashMap<>();

        dataMap.put("id",id);
        dataMap.put("username",username);
        dataMap.put("nickname",nickname);
        dataMap.put("password",password);
        dataMap.put("email",email);
        dataMap.put("provider",provider);
        dataMap.put("gender",gender);
        dataMap.put("phoneNumber",phoneNumber);
        dataMap.put("createdAt",createdAt);
        dataMap.put("birthday",birthday);
        dataMap.put("roleNames",roleNames);

        return dataMap;
    }




}
