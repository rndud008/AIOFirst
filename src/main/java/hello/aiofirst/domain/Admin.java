package hello.aiofirst.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString(exclude = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private Long id;

    private String username;
    private String password;
    private String nickname;
    
    @ElementCollection
    @CollectionTable(name = "admin_role" , joinColumns = @JoinColumn(name = "admin_id"))
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    public Admin resisterAdmin(String username, String password, String nickname, List<Role> roles){
        return Admin.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .roles(roles)
                .build();

    }
}
