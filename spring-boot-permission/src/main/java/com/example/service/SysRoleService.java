package com.example.service;

import com.example.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.req.RoleReq;

import java.net.SocketException;
import java.util.List;

/**
* @author wanggaoshuai
* @description 针对表【sys_role】的数据库操作Service
* @createDate 2023-10-19 14:33:50
*/
public interface SysRoleService extends IService<SysRole> {

    void saveRole(RoleReq roleReq) ;
    void updateRole(RoleReq roleReq) ;

    List<SysRole> getRoleListByUserId(int userId);
}
