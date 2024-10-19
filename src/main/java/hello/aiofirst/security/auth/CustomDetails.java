package hello.aiofirst.security.auth;

import hello.aiofirst.domain.Admin;
import hello.aiofirst.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomDetails implements UserDetails {

    private Admin admin;
    public Admin getAdmin(){
        return this.admin;
    }

    public CustomDetails(Admin admin){
        this.admin = admin;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        if (admin.getRoles() ==null) return collection;

        admin.getRoles().forEach(role -> collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return role.name().trim();
            }
            public String toString(){
                return role.name().trim();
            }
        }));

        return collection;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getPassword();
    }

    public String getNickname(){
        return admin.getNickname();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
