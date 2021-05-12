package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.MemberMapper;
import net.xasquatch.document.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MemberDao {

    @Autowired
    private MemberMapper memberMapper;

    public Member selectByEmail(String email) {
        return memberMapper.selectByEmail(email);
    }

    public boolean insertMember(Member member){
        return memberMapper.insertMember(member) == 1;
    }
}
