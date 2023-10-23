package com.example.service;

import com.alibaba.fastjson.JSON;
import com.example.entity.SysRoleUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.req.RoleUserReq;

/**
* @author wanggaoshuai
* @description 针对表【sys_role_user】的数据库操作Service
* @createDate 2023-10-19 14:34:10
*/
public interface SysRoleUserService extends IService<SysRoleUser> {

    void changeRoleUsers(RoleUserReq roleUserReq);

    JSON querySelectedAandUnselectedByRoleId(int roleId);
}
