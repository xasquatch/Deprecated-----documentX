package net.xasquatch.document.mapper;

import net.xasquatch.document.model.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;

public interface MemberMapper {

    @Select("SELECT * FROM mbr WHERE email = #{arg0}")
    Member selectByEmail(String email);

    @Insert("INSERT INTO mbr(email, pwd, nick_name, enable) VALUES(#{email},#{pwd},#{nick_name},1)")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "no", before = false, resultType = long.class)
    int insertMember(Member member);



}
