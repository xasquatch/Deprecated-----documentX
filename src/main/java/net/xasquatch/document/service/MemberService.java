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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@PropertySource("/WEB-INF/setting.properties")
public class MemberService implements UserDetailsService, MemberServiceInterface {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private AuthorizationDao authorizationDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenMap tokenMap;

    @Autowired
    private PaginationService paginationService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Member member = memberDao.selectByEmail(s);

        if (member.getEmail() == null)
            throw new UsernameNotFoundException(s + "is not found.");


        //이메일
        member.setEmail(member.getUsername());
        member.setPwd(member.getPassword());
        member.setAuthorities(getAuthorities(s));

        return member;
    }

    public List<GrantedAuthority> getAuthorities(String username) {
        List<Authorization> authList = authorizationDao.selectByEmail(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Authorization Authorization : authList) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + Authorization.getName()));
        }
        return authorities;
    }

    public List<String> getPermissionList() {
        return authorizationDao.selectAuthorizationList();
    }

    public boolean modifyPermission(long memberNo, String authName) {
        return authorizationDao.updateAuthorization(memberNo, authName) == 1 ? true : false;
    }

    public boolean removePermission(long memberNo, String authName) {
        return authorizationDao.deleteAuthorization(memberNo, authName) == 1 ? true : false;
    }

    public boolean addPermission(long memberNo, String authName) {
        return authorizationDao.insertAuthorization(authName, memberNo);
    }


    //-----------------------MemberServiceInterface------------------------------------------
    @Override
    public boolean isAvailableEmail(String email) {
        return memberDao.selectByEmail(email) == null ? true : false;
    }

    @Override
    public boolean isConfirmEmailToken(String token) {
        boolean result = false;
        try {
            result = tokenMap.confirmToken(token);
        } catch (Exception e) {
            return result;
        }

        return result;
    }

    @Override
    public boolean isAvailableNickName(String nickName) {
        return memberDao.selectByNickName(nickName) > 0 ? false : true;
    }

    @Override
    public Map<String, Object> searchMemberList(String emailOrNickName, int currentPage, int pageLimit) {
        Map<String, Object> resultMap = memberDao.selectMemberList(emailOrNickName, currentPage, pageLimit);
        List<String> pagination = paginationService.getPageBlockList(pageLimit, currentPage, (Integer) resultMap.get("count"));

        resultMap.put("pagination", pagination);

        return resultMap;
    }

    @Override
    public boolean addMember(Member member) {
        boolean confirmedTokenAuthorization = tokenMap.isConfirmedTokenAuthorization(member.getEmail());
        if (!confirmedTokenAuthorization) return false;

        //password encode
        member.setPwd(passwordEncoder.encode(member.getPwd()));
        boolean insertMemberResult = memberDao.insertMember(member);
        if (insertMemberResult) {
            //TODO: 여기 수정해야됨 (임시)
            boolean insertAuthResult = authorizationDao.insertAuthorization("USER", member.getNo());
            if (!insertAuthResult) return false;

        }

        return true;
    }

    @Override
    public boolean modifyMember(Member member) {
        String encodedPwd = passwordEncoder.encode(member.getPwd());
        member.setPwd(encodedPwd);
        return memberDao.updateMember(member) == 1 ? true : false;
    }

    public boolean modifyMemberForManagement(Member member) {
        return memberDao.updateMemberWithOutPwd(member) == 1 ? true : false;
    }

    @Override
    public boolean removeMember(Member member) {
        return memberDao.deleteMember(member) == 1 ? true : false;
    }

    public Member getMemberToNumber(long memberNo) {
        return memberDao.selectMember(memberNo);

    }

    @Override
    public Map<String, Object> getMemberInfoByEmail(String email) {
        Member member = memberDao.selectByEmail(email);
        HashMap<String, Object> memberInfoMap = new HashMap<>();
        memberInfoMap.put("no", member.getNo());
        memberInfoMap.put("email", member.getEmail());
        memberInfoMap.put("nickName", member.getNick_name());
        memberInfoMap.put("date", member.getCreated_date());

        return memberInfoMap;
    }

    //-------------index----------------
    public List<Map<String, Object>> getMemberMessageCount(long memberNo) {
        List<Map<String, Object>> countMap = memberDao.selectMeassageGroupByMemberCount();

        return null;
    }

}
