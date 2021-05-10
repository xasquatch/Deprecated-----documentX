package net.xasquatch.document.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;

@Data
public class Member {

    private Long no;

    @NotNull
    @Pattern(regexp = "/^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]{2,3}$/")
    private String email;

    @NotNull
    @Pattern(regexp = "/^[a-z0-9]{8,20}$/")
    @Size(min = 8, max = 20)
    private String pwd;

    @Size(min = 8, max = 20)
    @Pattern(regexp = "/^[0-9a-z]{8,20}$/")
    private String nick_name;
    private Date created_date;
    private boolean enable;
    private Collection<? extends GrantedAuthority> authorities;


}
