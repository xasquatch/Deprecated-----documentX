package net.xasquatch.document.mapper;

import net.xasquatch.document.model.Member;
import org.apache.ibatis.annotations.Select;

public interface MemberMapper {

    @Select("SELECT * FROM mbr WHERE nick_name = #{arg0}")
    Member selectByMbrNickName(String name);
}
