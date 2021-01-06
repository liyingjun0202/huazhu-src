package com.hz.show.utils;

import com.hz.show.dao.InspectionDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;

/**
 * @author liyj
 * @Description
 * @createTime 2020/12/30 18:35
 **/
@Component
public class FileUtil {


    private static String path;

    @Value("${file_path}")
    public void setPath(String path) {
        FileUtil.path = path;
    }

    public static void moveFile(MultipartFile file, String relativePath) {
        try {
            String filename = upload(file, relativePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static String upload(MultipartFile file, String relativePath) throws Exception {
        String realPath = path + relativePath;
        File dir = new File(realPath.substring(0, realPath.lastIndexOf("/")));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File newFile = new File(realPath);
//        if (relativePath.toLowerCase().endsWith("jpg") || relativePath.toLowerCase().endsWith("png")) {
//            newFile.delete();
//        }
        //如果当前pdf存在 则将当前pdf加上当前时间戳的后缀展示
        if (relativePath.toLowerCase().endsWith("pdf")) {
            if(newFile.exists()) {
                File  lastFile = new File(realPath.substring(0,realPath.lastIndexOf(".pdf"))+"-"+System.currentTimeMillis()+".pdf");
                Files.copy(newFile.toPath(),lastFile.toPath());
            }
        }
        file.transferTo(newFile);
        if (relativePath.toLowerCase().endsWith("jpg") || relativePath.toLowerCase().endsWith("png")) {
            ImageUtil.reduceImg(realPath, realPath, 0.6f);
        }
        return newFile.getName();
    }

}
