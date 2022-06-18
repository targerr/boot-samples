package com.example.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import com.example.model.OssFileConfig;
import com.example.service.IOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/*
 * 统一文件上传接口（ossFile）
 *
 */
@RestController
@RequestMapping("/api/ossFiles")
public class OssFileController {

    @Autowired
    private IOssService ossService;

    /**
     * 上传文件 （单文件上传）
     * @param file
     * @param bizType
     * @return
     */
    @PostMapping("/{bizType}")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable("bizType") String bizType) {
        if (file == null) {
            return "选择文件不存在";
        }

        OssFileConfig ossFileConfig = OssFileConfig.getOssFileConfigByBizType(bizType);

        //1. 判断bizType 是否可用
        if (ossFileConfig == null) {
            return "类型有误";
        }

        // 2. 判断文件是否支持
        String fileSuffix = FileUtil.extName(file.getOriginalFilename());
        if (!ossFileConfig.isAllowFileSuffix(fileSuffix)) {
            return "上传文件格式不支持";
        }

        // 3. 判断文件大小是否超限
        if (!ossFileConfig.isMaxSizeLimit(file.getSize())) {
            return "上传大小请限制在[\" + ossFileConfig.getMaxSize() / 1024 / 1024 + \"M]以内！";
        }

        // 新文件地址 (xxx/xxx.jpg 格式)
        String saveDirAndFileName = bizType + "/" + UUID.fastUUID() + "." + fileSuffix;
        return ossService.upload2PreviewUrl(ossFileConfig.getOssSavePlaceEnum(), file, saveDirAndFileName);

    }

}
