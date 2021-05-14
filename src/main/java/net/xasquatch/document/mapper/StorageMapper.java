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
            "WHERE m.no = #{arg0} " +
            "ORDER BY s.date DESC")
    List<StorageEntity> selectStorageList(Object memberNo);

    @Select("SELECT s.no AS no, s.mbr_no AS mbr_no, m.nick_name AS mbr_nick_name, " +
            "       s.type AS dataType, s.url AS url, s.date AS date " +
            "FROM storage s " +
            "LEFT OUTER JOIN mbr m " +
            "ON s.mbr_no = m.no " +
            "WHERE m.no = #{arg0} " +
            "AND s.url LIKE '/storage/#{arg0}/%#{arg1}%' " +
            "ORDER BY s.date DESC")
    List<StorageEntity> selectStorageListWhere(Object memberNo, String searchValue);

    @Update("UPDATE storage " +
            "SET type = #{dataType}, " +
            "   url = #{url}" +
            "WHERE no = #{no}")
    int updateStorage(StorageEntity storageFile);

    @Delete("DELETE FROM storage WHERE no = #{arg0}")
    int deleteStorage(Object storageNo);

    @Insert("INSERT INTO storage(mbr_no, type, url) " +
            "VALUES(#{mbr_no}, #{dataType}, #{url})")
    int insertStorage(StorageEntity storageFile);
}
