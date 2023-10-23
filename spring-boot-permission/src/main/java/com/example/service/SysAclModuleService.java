package com.example.service;

import com.example.entity.SysAclModule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.req.AclModuleReq;

/**
* @author wanggaoshuai
* @description 针对表【sys_acl_module】的数据库操作Service
* @createDate 2023-10-20 15:45:01
*/
public interface SysAclModuleService extends IService<SysAclModule> {

    void saveAclModule(AclModuleReq aclModule);

    void updateAclModule(AclModuleReq aclModule);

    void deleteAclModule(int id);
}
