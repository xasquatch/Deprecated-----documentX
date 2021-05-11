package net.xasquatch.document.service;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Authorization;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.repository.AuthorizationDao;
import net.xasquatch.document.repository.MemberDao;
import net.xasquatch.document.service.command.MemberServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Slf4j
@PropertySource("/WEB-INF/setting.properties")
public class MemberService implements UserDetailsService, MemberServiceInterface {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private AuthorizationDao authorizationDao;

    @Autowired
    private TokenMap tokenMap;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Member member = memberDao.selectByEmail(s);

        if (member.getEmail() == null) {
            throw new UsernameNotFoundException(s + "is not found.");
        }

        member.setEmail(member.getUsername());
        member.setPwd(member.getPassword());
        member.setAuthorities(getAuthorities(s));

        return member;
    }

    public Collection<GrantedAuthority> getAuthorities(String username) {
        List<Authorization> authList = authorizationDao.selectByEmail(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authorization Authorization : authList) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + Authorization.getName()));
        }
        return authorities;
    }

    //-----------------------MemberServiceInterface------------------------------------------
    @Override
    public boolean isAvailableEmail(String email) {
        return true;
    }

    @Override
    public boolean isConfirmEmail(String email, String token) {

        boolean result = false;
        try {
            result = tokenMap.confirmToken(email + token);
        } catch (Exception e) {
            return result;
        }

        return result;
    }

    @Override
    public boolean isAvailableNickName(String nickName) {
        return true;
    }

    @Override
    public List<Member> searchMemberList(String emailOrNickName) {
        return null;
    }

    @Override
    public boolean addMember(Member member) {
        boolean confirmedTokenAuthorization = tokenMap.isConfirmedTokenAuthorization(member.getEmail());

        return true;
    }

    @Override
    public boolean modifyMember(Member member) {
        return true;
    }

    @Override
    public boolean removeMember(Member member) {
        return true;
    }
}
