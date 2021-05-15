package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.MemberMapper;
import net.xasquatch.document.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDao {

    @Autowired
    private MemberMapper memberMapper;

    public Map<String, Object> selectMemberList(String searchValue){
        Map<String, Object> resultMap = new HashMap<>();

        List<Member> members = memberMapper.selectMemberList(searchValue, 0, 10);
        int count = memberMapper.selectMemberListCount(searchValue);

        resultMap.put("memberList", members);
        resultMap.put("count", count);

        return resultMap;
    }

    public Member selectByEmail(String email) {
        return memberMapper.selectByEmail(email);
    }

    public boolean insertMember(Member member){
        return memberMapper.insertMember(member) == 1;
    }

    public int selectByNickName(String nickName) {
        return memberMapper.selectByNickName(nickName);
    }

    public int deleteMember(Member member) {
        return memberMapper.deleteMember(member);
    }

    public int updateMember(Member member) {
        return memberMapper.updateMember(member);
    }
}
