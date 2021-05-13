package net.xasquatch.document.mapper;

import net.xasquatch.document.model.StorageEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StorageMapper {

    @Select("SELECT s.no AS no, s.mbr_no AS mbr_no, m.nick_name AS mbr_nick_name, " +
            "       s.type AS dataType, s.url AS url, s.date AS date " +
            "FROM storage s " +
            "LEFT OUTER JOIN mbr m " +
            "ON s.mbr_no = m.no " +
            "WHERE m.no = #{arg0}")
    List<StorageEntity> selectStorageList(Object memberNo);

    @Update("UPDATE storage " +
            "SET mbr_no = #{mbr_no}, " +
            "   type = #{dataType}, " +
            "   url = #{url}" +
            "WHERE no = #{no}")
    int updateStorage(StorageEntity storageFile);

    @Delete("DELETE FROM storage WHERE mbr_no = #{arg0}")
    int deleteStorage(Object memberNo);

    @Insert("INSERT INTO storage(mbr_no, type, url) " +
            "SET(#{mbr_no}, #{dataType}, #{url})")
    int insertStorage(StorageEntity storageFile);
}
