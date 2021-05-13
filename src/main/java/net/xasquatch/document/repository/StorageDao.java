package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.StorageMapper;
import net.xasquatch.document.model.StorageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StorageDao {

    @Autowired
    private StorageMapper storageMapper;

    public boolean insertFileList(List<String> resultFileList) {
        boolean result = false;

        return result;
    }

    public List<StorageEntity> selectStorageList(Object memberNo) {
        return storageMapper.selectStorageList(memberNo);
    }

    public int insertStorage(StorageEntity storageFile) {
        return storageMapper.insertStorage(storageFile);

    }

    public int updateStorage(StorageEntity storageFile) {
        return storageMapper.updateStorage(storageFile);

    }

    public int deleteStorage(Object memberNo) {
        return storageMapper.deleteStorage(memberNo);

    }

}
