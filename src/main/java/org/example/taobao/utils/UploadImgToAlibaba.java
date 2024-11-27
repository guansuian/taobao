package org.example.taobao.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author 关岁安
 */
public class UploadImgToAlibaba {

    public static String uploadImgToAlibaba(MultipartFile file) throws Exception {
        String originalFileName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFileName.substring(originalFileName.lastIndexOf("."));
        String url = AliOssUtil.upLoadFile(fileName,file.getInputStream());
        return url;
    }

}
