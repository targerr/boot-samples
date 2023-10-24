package com.example.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SysAcl;
import com.example.enums.ResultEnum;
import com.example.exception.PreException;
import com.example.req.AclReq;
import com.example.service.SysAclService;
import com.example.mapper.SysAclMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

/**
 * @author wanggaoshuai
 * @description 针对表【sys_acl】的数据库操作Service实现
 * @createDate 2023-10-20 15:44:45
 */
@Service
public class SysAclServiceImpl extends ServiceImpl<SysAclMapper, SysAcl>
        implements SysAclService {

    @Resource
    private SysAclMapper sysAclMapper;

    @Override
    public void saveAcl(AclReq req) {
        if (checkNameExists(req)) {
            throw new PreException(ResultEnum.ACL_NAME_ERROR);
        }

        SysAcl acl = SysAcl.builder().name(req.getName()).aclModuleId(req.getAclModuleId()).url(req.getUrl())
                .type(req.getType()).status(req.getStatus()).seq(req.getSeq()).remark(req.getRemark()).build();
        acl.setCode(RandomUtil.randomNumbers(10));
//        acl.setOperator(RequestHolder.getCurrentUser().getUsername());
//        acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        acl.setOperateTime(new Date());
        sysAclMapper.insert(acl);
    }

    private boolean checkNameExists(AclReq acl) {
        LambdaQueryWrapper<SysAcl> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysAcl::getName, acl.getName())
                .eq(SysAcl::getAclModuleId, acl.getAclModuleId())
                .ne(Objects.nonNull(acl.getId()), SysAcl::getId, acl.getId());
        return sysAclMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public void updateAcl(AclReq param) {
        SysAcl dbAcl = sysAclMapper.selectById(param.getId());
        if (Objects.isNull(dbAcl)){
            throw new PreException(ResultEnum.ACL_NOT_EXIST);
        }

        if (checkNameExists(param)) {
            throw new PreException(ResultEnum.ACL_NAME_ERROR);
        }
        SysAcl after = SysAcl.builder().id(param.getId()).name(param.getName()).aclModuleId(param.getAclModuleId()).url(param.getUrl())
                .type(param.getType()).status(param.getStatus()).seq(param.getSeq()).remark(param.getRemark()).build();
//        after.setOperator(RequestHolder.getCurrentUser().getUsername());
//        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysAclMapper.updateById(after);
    }

    @Override
    public void deleteAcl(int id) {

    }
}




