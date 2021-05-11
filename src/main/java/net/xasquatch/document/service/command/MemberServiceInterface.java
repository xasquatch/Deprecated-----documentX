package net.xasquatch.document.service.command;

import net.xasquatch.document.model.Member;

import java.util.List;

public interface MemberServiceInterface {

    boolean isAvailableEmail(String email);
    boolean isConfirmEmail(String email, String token);
    boolean isAvailableNickName(String nickName);
    List<Member> searchMemberList(String emailOrNickName);
    boolean addMember(Member member);
    boolean modifyMember(Member member);
    boolean removeMember(Member member);

}
