package net.xasquatch.document.service.command;

import net.xasquatch.document.model.Member;

import java.util.Map;

public interface MemberServiceInterface {

    boolean isAvailableEmail(String email);
    boolean isConfirmEmailToken(String token);
    boolean isAvailableNickName(String nickName);
    Map<String, Object> searchMemberList(String emailOrNickName, Object currentPage, Object pageLimit);
    boolean addMember(Member member);
    boolean modifyMember(Member member);
    boolean removeMember(Member member);

}
