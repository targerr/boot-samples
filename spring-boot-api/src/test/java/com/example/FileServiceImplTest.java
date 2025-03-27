package com.example;

import cn.hutool.core.codec.Base64;
import cn.hutool.http.HttpUtil;

import com.example.entity.FileEntity;
import com.example.service.impl.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FileServiceImplTest {

    @Autowired
    private FileService fileService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testFileService() throws IOException {

        byte[] data = Files.readAllBytes(Paths.get("C:\\Windows\\win.ini"));

        FileEntity fileData = new FileEntity();
        fileData.setFileName("test.txt");
        fileData.setFileType("txt");
        fileData.setTypeId("1");
        fileData.setPrjId(UUID.randomUUID().toString());
        fileData.setSectId(UUID.randomUUID().toString());
        fileData.setFileSize((float) data.length);
        fileData.setFileDataBase64(Base64.encode(data));

        // 上传文件
        String fileId = fileService.uploadFile(fileData);
        assertTrue(fileId != null, "文件上传失败");

        //  assertTrue(fileService.delete(fileId), "文件删除失败");

    }

    @Test
    public void testFileService2() throws IOException {

        String path = "https://appaw.95505.cn/tacapp/tiananapp/epolicy/eCarPolicyDownloadChannel?policyNo=6016930184006230004447&identifyNumber=913401007728167209（1-8）&channelCode=PD-NCPI01&signature=gIsjmKJ9sKGa1ID5Lmw5msrftvIoF%2FcjDbPbzeSQgra6towPr9kh5shvx3bnGcBzdMVSEvVhQmxm%0Am7VrI%2BkmLQ%3D%3D&guaranteeType=1";
        byte[] bytes = HttpUtil.downloadBytes(path);
        FileEntity fileData = new FileEntity();
        fileData.setFileName("6016930184006230004447.pdf");
//        fileData.setFileType("pdf");
        fileData.setTypeId("1");
        fileData.setPrjId(UUID.randomUUID().toString());
        fileData.setSectId(UUID.randomUUID().toString());
        fileData.setFileSize((float) bytes.length);
        fileData.setFileDataBase64(Base64.encode(bytes));

        // 上传文件
        String fileId = fileService.uploadFile(fileData);
        System.err.println("fileId: " + fileId);
        assertTrue(fileId != null, "文件上传失败");
    }


}