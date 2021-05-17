package net.xasquatch.document.mapper;

import net.xasquatch.document.model.Authorization;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AuthorizationMapper {

    @Select("SELECT a.no AS no, a.name AS name, a.mbr_no AS mbr_no, " +
            "       m.nick_name AS mbr_nick_name, a.date AS date " +
            "FROM authorization a " +
            "LEFT OUTER JOIN mbr m ON a.mbr_no = m.no " +
            "WHERE m.email = #{arg0}")
    List<Authorization> selectByEmail(String email);

    @Insert("INSERT INTO authorization(name, mbr_no) VALUES(#{arg0}, #{arg1})")
    int insertAuthorization(String authorization, Object MemberNo);

    @Update("UPDATE authorization SET name = #{arg1} WHERE mbr_no = #{arg0}")
    int updateAuthorization(Object memberNo, Object authName);

    @Delete("DELETE FROM authorization WHERE mbr_no = #{arg0} AND name = #{arg1}")
    int deleteAuthorization(long memberNo, String authName);

    @Select("SELECT name AS permission FROM authorization group by name")
    List<String> selectAuthorizationList();

}