package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.MemberMapper;
import net.xasquatch.document.model.History;
import net.xasquatch.document.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MemberDao {

    @Autowired
    private MemberMapper memberMapper;

    public Map<String, Object> selectMemberList(Object searchValue, int currentPage, int pageLimit) {
        Map<String, Object> resultMap = new HashMap<>();

//TODO: 테이블에서 조인하여 중복으로 조회된 여러개의 권한을 컬렉션형태로 담기 위해
//  이렇게 해놓았지만 되게 비효율 적이다. 수정이 필요하다

        List<Member> members = memberMapper.selectMemberList(searchValue, currentPage - 1, pageLimit);
        Map<Long, Member> memberMap = new HashMap<>();
        for (Member member : members) {
            if (!memberMap.keySet().contains(member.getNo())) {
                List<String> authList = member.getAuthList();
                authList.add(member.getAuth());
                memberMap.put(member.getNo(), member);

            } else {
                Member memberEntity = memberMap.get(member.getNo());
                List<String> authList = memberEntity.getAuthList();
                authList.add(member.getAuth());
                memberMap.put(memberEntity.getNo(), memberEntity);

            }

        }
        members = new ArrayList<Member>();
        for (Map.Entry<Long, Member> entry : memberMap.entrySet())
            members.add(entry.getValue());


        List<Integer> integers = memberMapper.selectMemberListCount(searchValue);
        int count = integers.size();


        resultMap.put("memberList", members);
        resultMap.put("count", count);

        return resultMap;
    }

    public Member selectByEmail(String email) {
        return memberMapper.selectByEmail(email);
    }

    public boolean insertMember(Member member) {
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

    public int updateMemberWithOutPwd(Member member) {
        return memberMapper.updateMemberWithOutPwd(member);
    }


    public Member selectMember(long memberNo) {

        Member resultMember = new Member();
        List<Member> members = memberMapper.selectMember(memberNo);
        for (Member member : members) {
            if (!resultMember.equals(member)) resultMember = member;
            resultMember.getAuthList().add(member.getAuth());
        }
        return resultMember;
    }

    public int insertHistory(History history) {
        return memberMapper.insertHistory(history);
    }

    public List<History> selectHistoryList(long memberNo) {
        return memberMapper.selectHistoryList(memberNo);
    }

    public List<Map<String ,Object>> selectMeassageGroupByMemberCount(){
        return memberMapper.selectMessageGroupByMemberCount();
    }

}
