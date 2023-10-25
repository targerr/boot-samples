package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.context.ReqInfoContext;
import com.example.entity.SysRoleAcl;
import com.example.req.RoleAclReq;
import com.example.service.SysRoleAclService;
import com.example.mapper.SysRoleAclMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wanggaoshuai
 * @description 针对表【sys_role_acl】的数据库操作Service实现
 * @createDate 2023-10-20 15:45:10
 */
@Service
public class SysRoleAclServiceImpl extends ServiceImpl<SysRoleAclMapper, SysRoleAcl>
        implements SysRoleAclService {
    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeRoleAcls(RoleAclReq roleAclReq) {

        if (CollectionUtils.isEmpty(roleAclReq.getAclIds())) {
            return;
        }

        sysRoleAclMapper.delete(new LambdaQueryWrapper<SysRoleAcl>().eq(SysRoleAcl::getRoleId, roleAclReq.getRoleId()));
        final ReqInfoContext.ReqInfo reqInfo = ReqInfoContext.getReqInfo();
        List<SysRoleAcl> roleAclList = roleAclReq.getAclIds().stream().map(aclId -> {
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleAclReq.getRoleId()).aclId(aclId)
                    .operator(reqInfo.getUser().getUserName())
                    .operateIp(reqInfo.getClientIp())
                    .operateTime(new Date()).build();

            return roleAcl;
        }).collect(Collectors.toList());


        roleAclList.forEach(e -> sysRoleAclMapper.insert(e));
    }


}




