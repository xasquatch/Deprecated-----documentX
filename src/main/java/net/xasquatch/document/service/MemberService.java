package net.xasquatch.document.service;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Authorization;
import net.xasquatch.document.model.CustomUserDetails;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.repository.AuthorizationDao;
import net.xasquatch.document.repository.MemberDao;
import net.xasquatch.document.service.command.MemberServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Slf4j
@Service
@PropertySource("/WEB-INF/setting.properties")
public class MemberService implements UserDetailsService, MemberServiceInterface {

    @Value("${password.encryption.salt}")
    private String salt;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private AuthorizationDao authorizationDao;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Member member = memberDao.selectByMbrNickName(s);

        if (member == null) {
            throw new UsernameNotFoundException(s + "is not found.");
        }

        CustomUserDetails userDetails = new CustomUserDetails();
        userDetails.setUsername(member.getEmail());
        userDetails.setPassword(member.getPwd());
        userDetails.setAuthorities(getAuthorities(s));
        userDetails.setEnabled(true);
        userDetails.setAccountNonExpired(true);
        userDetails.setAccountNonLocked(true);
        userDetails.setCredentialsNonExpired(true);

        return userDetails;
    }

    public Collection<GrantedAuthority> getAuthorities(String username) {
        List<Authorization> authList = authorizationDao.selectByMbrNickName(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authorization Authorization : authList) {
            authorities.add(new SimpleGrantedAuthority(Authorization.getName()));
        }
        return authorities;
    }

}
