package net.xasquatch.document.mapper;

import net.xasquatch.document.mapper.builder.MemberBuilder;
import net.xasquatch.document.model.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MemberMapper {

    @SelectProvider(type = MemberBuilder.class, method = "selectMemberList")
    List<Member> selectMemberList(Object searchValue, Object currentPage, Object pageLimit);

    @SelectProvider(type = MemberBuilder.class, method = "selectMemberListCount")
    int selectMemberListCount(Object searchValue);

    @Select("SELECT * FROM mbr WHERE email = #{arg0}")
    Member selectByEmail(String email);

    @Insert("INSERT INTO mbr(email, pwd, nick_name, enable) VALUES(#{email},#{pwd},#{nick_name},1)")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "no", before = false, resultType = long.class)
    int insertMember(Member member);

    @Select("SELECT COUNT(nick_name) FROM mbr WHERE nick_name = #{arg0}")
    int selectByNickName(String nickName);

    @Delete("DELETE FROM mbr WHERE no = #{no} AND email = #{email}")
    int deleteMember(Member member);

    @Update("UPDATE mbr SET pwd = #{pwd}, nick_name = #{nick_name} WHERE no = #{no}")
    int updateMember(Member member);

    @Update("UPDATE mbr SET email = #{email}, nick_name = #{nick_name} WHERE no = #{no}")
    int updateMemberWithOutPwd(Member member);

    @SelectProvider(type = MemberBuilder.class, method = "selectMember")
    List<Member> selectMember(Object memberNo);
}
