package net.xasquatch.document.repository;

import net.xasquatch.document.mapper.StorageMapper;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.model.StorageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StorageDao {

    @Autowired
    private StorageMapper storageMapper;


    public Map<String, Object> selectStorageList(Member member, Object searchValue, int currentPage, Object pageLimit) {

        Map<String, Object> resultMap = new HashMap<>();
        List<StorageEntity> storageList = storageMapper.selectStorageList(member, searchValue, currentPage - 1, pageLimit);
        int count = storageMapper.selectStorageListCount(member, searchValue, currentPage - 1, pageLimit);

        resultMap.put("storageList", storageList);
        resultMap.put("count", count);

        return resultMap;
    }

    public Map<String, Object> selectStorageListForManagement(Member member, Object searchValue, int currentPage, Object pageLimit) {

        Map<String, Object> resultMap = new HashMap<>();

        List<StorageEntity> storageList = storageMapper.selectStorageListForManagement(member, searchValue, currentPage - 1, pageLimit);
        int count = storageMapper.selectStorageListForManagementCount(member, searchValue, currentPage - 1, pageLimit);

        resultMap.put("storageList", storageList);
        resultMap.put("count", count);

        return resultMap;
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
