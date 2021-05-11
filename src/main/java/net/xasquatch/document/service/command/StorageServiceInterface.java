package net.xasquatch.document.service.command;


import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

public interface StorageServiceInterface {

    //생성된 경로 리턴
    String writeFile(byte[] byteArray, String path, String saveFileName);
    boolean isExistedFile(String path);
    boolean removeFiles(String path);
    //이미지 업로드하고 업로드 된 경로를 리스트에 담아 리턴
    List<String> uploadFile(MultipartHttpServletRequest request, String memberNo);
}