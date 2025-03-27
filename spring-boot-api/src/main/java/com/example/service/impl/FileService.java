package com.example.service.impl;

import com.example.entity.FileEntity;

public interface FileService  {

    String uploadFile(FileEntity fileData);

    Boolean delete(String fileId);

    String download(String fileId);
}