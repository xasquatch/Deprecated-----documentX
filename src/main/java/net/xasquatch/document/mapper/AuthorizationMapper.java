package net.xasquatch.document.mapper;

import net.xasquatch.document.model.Authorization;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AuthorizationMapper {

    @Select("SELECT * FROM authorization WHERE mbr_nick_name = #{arg0}")
    List<Authorization> selectByMbrNickName(String name);

}
