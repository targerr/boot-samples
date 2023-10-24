package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SysRoleAcl;
import com.example.req.RoleAclReq;
import com.example.service.SysRoleAclService;
import com.example.mapper.SysRoleAclMapper;
import org.springframework.stereotype.Service;

/**
* @author wanggaoshuai
* @description 针对表【sys_role_acl】的数据库操作Service实现
* @createDate 2023-10-20 15:45:10
*/
@Service
public class SysRoleAclServiceImpl extends ServiceImpl<SysRoleAclMapper, SysRoleAcl>
    implements SysRoleAclService{

    @Override
    public void changeRoleAcls(RoleAclReq roleAclReq) {

    }
}




