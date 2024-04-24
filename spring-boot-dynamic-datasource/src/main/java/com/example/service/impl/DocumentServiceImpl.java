package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mapper.DocumentMapper;
import com.example.pojo.Document;
import com.example.service.DocumentService;
import org.springframework.stereotype.Service;

/**
* @author wanggaoshuai
* @description 针对表【document(文档)】的数据库操作Service实现
* @createDate 2024-04-23 15:14:18
*/
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document>
    implements DocumentService {

}




