package com.example.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @Author: wgs
 * @Date 2022/6/17 11:22
 * @Classname LocalUtil
 * @Description
 */
@Component
@Slf4j
public class LocalUtil {
    /**
     * 上传
     * https://gitee.com/renrenio/renren-fast/blob/master/src/main/java/io/renren/modules/oss/cloud/OSSFactory.java
     *
     * @param name          上传地址
     * @param localFilePath 上传地址 /Downloads/6b2f3194ab2d4a9e91053e449807727f.png
     * @throws Exception
     */
    public static void upload(String localFilePath, String name) throws Exception {
        File file = FileUtil.file(localFilePath);

        BufferedInputStream bufferedInputStream = FileUtil.getInputStream(localFilePath);
        String outFilePath = "/home/" + RandomUtil.randomNumbers(4) + "." + FileUtil.getType(file);
        BufferedOutputStream bufferOutputStream = FileUtil.getOutputStream(outFilePath);
        IoUtil.copy(bufferedInputStream, bufferOutputStream);

    }

}
