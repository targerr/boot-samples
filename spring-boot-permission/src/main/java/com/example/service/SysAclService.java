package com.example.service;

import com.example.entity.SysAcl;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.req.AclModuleReq;
import com.example.req.AclReq;

/**
* @author wanggaoshuai
* @description 针对表【sys_acl】的数据库操作Service
* @createDate 2023-10-20 15:44:45
*/
public interface SysAclService extends IService<SysAcl> {

    void saveAcl(AclReq acl);

    void updateAcl(AclReq acl);

    void deleteAcl(int id);
}
