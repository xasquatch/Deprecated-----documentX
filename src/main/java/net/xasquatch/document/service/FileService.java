package net.xasquatch.document.service;

import net.xasquatch.document.service.command.FileServiceInterface;
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

@Service
@PropertySource("WEB-INF/setting.properties")
public class FileService implements FileServiceInterface {

    @Value("${files.save.path}")
    private String filesSavePath;
    @Value("${files.context.path}")
    private String filesContextPath;

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
    public boolean isExistedFile(String path) {
        return new File(this.filesSavePath + path).exists();
    }

    @Override
    public boolean removeFiles(String path) {
        boolean result = false;
        if (isExistedFile(path))
            result = new File(this.filesSavePath + path).delete();

        return result;

    }

    //이미지 업로드하고 업로드 된 경로를 리스트에 담아 리턴
    @Override
    public List<String> uploadFile(MultipartHttpServletRequest request, String memberNo) {
        List<String> resultFileList = new ArrayList<String>();
        List<MultipartFile> multipartFiles = request.getFiles("filePackage");

        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile != null) {

                try {
                    String targetName = multipartFile.getOriginalFilename();
                    String contextPath = writeFile(multipartFile.getBytes(), File.separator + memberNo, targetName);
                    resultFileList.add(contextPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        return resultFileList;
    }
}
