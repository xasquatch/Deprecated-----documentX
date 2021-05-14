package net.xasquatch.document.service;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.StorageEntity;
import net.xasquatch.document.model.enumulation.DataType;
import net.xasquatch.document.repository.StorageDao;
import net.xasquatch.document.service.command.StorageServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@PropertySource("WEB-INF/setting.properties")
public class StorageService implements StorageServiceInterface {

    @Value("${files.save.path}")
    private String filesSavePath;
    @Value("${files.context.path}")
    private String filesContextPath;
    @Value("${files.image.extension}")
    private String fileExtension;

    @Autowired
    private StorageDao storageDao;

    //생성된 경로 리턴
    @Override
    public String writeFile(byte[] byteArray, String path, String saveFileName) {
        File filedir = new File(this.filesSavePath + path);
        String filePath = this.filesSavePath + path + File.separator + saveFileName;
        filedir.mkdirs();

        try (BufferedOutputStream bytebuffer = new BufferedOutputStream(new FileOutputStream(filePath));) {

            bytebuffer.write(byteArray);
            filePath = this.filesContextPath + path + File.separator + saveFileName;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        filePath = filePath.replace(File.separator, "/");

        return filePath;
    }

    @Override
    public boolean removeFiles(String path) {
        boolean result = false;
        if (isExistedFile(path))
            result = new File(this.filesSavePath + path).delete();

        return result;

    }

    @Override
    public boolean isExistedFile(String path) {
        return new File(this.filesSavePath + path).exists();
    }

    public String[] getSupportedImageExtension() {
        return fileExtension.split(",");

    }

    public boolean isImageExtension(String extension) {
        boolean result = false;
        String[] extensionUnit = getSupportedImageExtension();
        for (String unit : extensionUnit) {
            if (extension.equals(unit)) {
                result = true;
                break;
            }
        }

        return result;
    }

    //------controller Mapping method-------------------------

    //이미지 업로드하고 업로드 된 경로를 리스트에 담아 리턴
    @Override
    public int uploadFile(MultipartHttpServletRequest request, String memberNo) {
        int resultInt = 0;
        List<StorageEntity> resultFileList = new ArrayList<StorageEntity>();
        List<MultipartFile> multipartFiles = request.getFiles("filePackage");

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile != null) {

                try {
                    String targetName = multipartFile.getOriginalFilename();
                    String contextPath = writeFile(multipartFile.getBytes(), File.separator + memberNo, targetName);
                    String targetExtension = targetName.substring(targetName.lastIndexOf('.') + 1);

                    StorageEntity entity = new StorageEntity();
                    if (isImageExtension(targetExtension)) {
                        entity.setDataType(DataType.IMAGE);

                    } else {
                        entity.setDataType(DataType.FILE);

                    }
                    entity.setUrl(contextPath);
                    entity.setMbr_no(Long.parseLong(memberNo));
                    resultFileList.add(entity);
                    resultInt += storageDao.insertStorage(entity);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        return resultInt;
    }

    public List<StorageEntity> getFileList(Object memberNo) {
        return storageDao.selectStorageList(memberNo);

    }

    public boolean renameFile(StorageEntity entity, String renameString) {

        boolean result = false;

        String url = entity.getUrl();
        try {

            //-------------server localFile setting
            String targetPath = url.substring(filesContextPath.length() - 1);

            //TODO: 지우기
            System.out.println("targetPath = " + targetPath);

            String renamePath = renameString.substring(filesContextPath.length() - 1);
            String renameExtension = renamePath.substring(renamePath.lastIndexOf('.'));
            //TODO: 지우기
            System.out.println("renamePath = " + renamePath);

            File targetFile = new File(filesSavePath + File.separator + targetPath);

            targetFile.renameTo(new File(filesSavePath + File.separator + renamePath));

            //-------------model & DB setting

            entity.setUrl(filesContextPath + File.separator + renamePath);

            if (isImageExtension(renameExtension)) {
                entity.setDataType(DataType.IMAGE);

            } else {
                entity.setDataType(DataType.FILE);

            }

            if (storageDao.updateStorage(entity) == 1) result = true;

        } catch (Exception e) {
            log.error("파일 수정 에러: {}", e.getMessage());
            return result;
        }

        return result;
    }

    public boolean deleteFile(Object storageNo) {
        boolean result = false;
        if (storageDao.deleteStorage(storageNo) == 1) result = true;

        return result;
    }

}
