package net.xasquatch.document.mapper;

import net.xasquatch.document.model.Member;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MemberMapper {

    @Select("SELECT * " +
            "FROM mbr " +
            "WHERE email LIKE '%#{arg0}%' " +
            "OR nick_name LIKE '%#{arg0}%' ")
    List<Member> selectMemberList(String searchValue);

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
}
