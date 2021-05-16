package net.xasquatch.document.mapper;

import net.xasquatch.document.mapper.builder.StorageBuilder;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.model.StorageEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StorageMapper {

    @SelectProvider(type = StorageBuilder.class, method = "selectStorageList")
    List<StorageEntity> selectStorageList(Member member, Object searchValue, Object currentPage, Object pageLimit);

    @SelectProvider(type = StorageBuilder.class, method = "selectStorageListCount")
    int selectStorageListCount(Member member, Object searchValue, Object currentPage, Object pageLimit);

    @SelectProvider(type = StorageBuilder.class, method = "selectStorageListForManagement")
    List<StorageEntity> selectStorageListForManagement(Member member, Object searchValue, Object currentPage, Object pageLimit);

    @SelectProvider(type = StorageBuilder.class, method = "selectStorageListForManagementCount")
    int selectStorageListForManagementCount(Member member, Object searchValue, Object currentPage, Object pageLimit);


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
