package net.xasquatch.document.mapper;

import net.xasquatch.document.model.Member;
import org.apache.ibatis.annotations.Select;

public interface MemberMapper {

    @Select("SELECT * FROM mbr WHERE email = #{arg0}")
    Member selectByEmail(String email);
}
