package com.example.service;

import com.example.config.OssYmlConfig;
import com.example.constant.OssSavePlaceEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @Author: wgs
 * @Date 2022/6/18 09:47
 * @Classname LocalFileService
 * @Description 本地存储实现类
 */
@Service
@Slf4j
@ConditionalOnProperty(name = "isys.oss.service-type", havingValue = "local")
public class LocalFileService implements IOssService {
    @Autowired
    private OssYmlConfig ossYmlConfig;

    /**
     * 将上传的文件进行保存
     * @param ossSavePlaceEnum
     * @param multipartFile
     * @param saveDirAndFileName
     * @return
     */
    @Override
    public String upload2PreviewUrl(OssSavePlaceEnum ossSavePlaceEnum, MultipartFile multipartFile, String saveDirAndFileName) {
        String path = null;
        try {

            String savePath = ossSavePlaceEnum == OssSavePlaceEnum.PUBLIC ?
                    ossYmlConfig.getOss().getFilePublicPath() : ossYmlConfig.getOss().getFilePrivatePath();
            path = savePath + File.separator + saveDirAndFileName;
            File saveFile = new File(path);

            //如果文件夹不存在则创建文件夹
            File dir = saveFile.getParentFile();
            if (!dir.exists()) {
                dir.mkdirs();
            }
            multipartFile.transferTo(saveFile);

        } catch (Exception e) {
            log.error("", e);
        }

        // 私有文件 不返回预览文件地址
        if (ossSavePlaceEnum == OssSavePlaceEnum.PRIVATE) {
            return saveDirAndFileName;
        }

        return path;
    }

    @Override
    public boolean downloadFile(OssSavePlaceEnum ossSavePlaceEnum, String source, String target) {
        return false;
    }
}
