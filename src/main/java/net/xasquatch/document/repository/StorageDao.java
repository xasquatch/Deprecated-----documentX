package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.StorageMapper;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.model.StorageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StorageDao {

    @Autowired
    private StorageMapper storageMapper;

    public List<StorageEntity> selectStorageList(Member member, Object searchValue, Object currentPage, Object pageLimit) {

        return storageMapper.selectStorageList(member, searchValue, currentPage, pageLimit);
    }

    public int insertStorage(StorageEntity storageFile) {
        return storageMapper.insertStorage(storageFile);

    }

    public int updateStorage(StorageEntity storageFile) {
        return storageMapper.updateStorage(storageFile);

    }

    public int deleteStorage(Object storageNo) {
        return storageMapper.deleteStorage(storageNo);

    }

}
