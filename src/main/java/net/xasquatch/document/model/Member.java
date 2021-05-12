package net.xasquatch.document.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Data
public class Member implements UserDetails {

    private Long no;

    @NotNull
    @Size(min = 10, max = 50)
    @Pattern(regexp = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}")
    private String email;

    @NotNull
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^[a-z0-9]{8,20}")
    private String pwd;

    @NotNull
    @Size(min = 2, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9가-힣 ]{2,20}")
    private String nick_name;
    private Date created_date;
    private boolean enable;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return getPwd();
    }

    @Override
    public String getUsername() {
        return getEmail();
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
