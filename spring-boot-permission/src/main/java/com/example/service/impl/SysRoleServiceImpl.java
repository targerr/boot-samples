package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SysRole;
import com.example.entity.SysRoleUser;
import com.example.enums.ResultEnum;
import com.example.exception.PreException;
import com.example.mapper.SysRoleUserMapper;
import com.example.req.RoleReq;
import com.example.service.SysRoleService;
import com.example.mapper.SysRoleMapper;
import com.example.util.IpUtil;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wanggaoshuai
 * @description 针对表【sys_role】的数据库操作Service实现
 * @createDate 2023-10-19 14:33:50
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
        implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public void saveRole(RoleReq roleReq) {
        if (existNameExist(roleReq.getName(), roleReq.getId())) {
            throw new PreException(ResultEnum.ROLE_NAME_ERROR);
        }

        SysRole role = SysRole.builder()
                .name(roleReq.getName())
                .status(roleReq.getStatus())
                .type(roleReq.getType())
                .remark(roleReq.getRemark())
                .build();
//        role.setOperator(RequestHolder.getCurrentUser().getUsername());
//        role.setOperateIp(IpUtil.getLocalIp4Address());
        role.setOperateTime(new Date());
        sysRoleMapper.insert(role);
    }

    @Override
    public void updateRole(RoleReq roleReq) {
        SysRole sysRole = sysRoleMapper.selectById(roleReq.getId());
        if (Objects.isNull(sysRole)){
            throw new PreException(ResultEnum.ROLE_NOT_EXIST);
        }
        if (existNameExist(roleReq.getName(), roleReq.getId())) {
            throw new PreException(ResultEnum.ROLE_NAME_ERROR);
        }

        SysRole after = SysRole.builder().id(roleReq.getId()).name(roleReq.getName()).status(roleReq.getStatus()).type(roleReq.getType())
                .remark(roleReq.getRemark()).build();
//        after.setOperator(RequestHolder.getCurrentUser().getUsername());
//        after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        sysRoleMapper.updateById(after);
    }

    @Override
    public List<SysRole> getRoleListByUserId(int userId) {
        final List<SysRoleUser> sysRoleUserList = sysRoleUserMapper.selectList(new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, userId));
        if (CollectionUtils.isEmpty(sysRoleUserList)){
            return Lists.newArrayList();
        }

        List<Integer> roleIds = sysRoleUserList.stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());
        return sysRoleMapper.selectList(new LambdaQueryWrapper<SysRole>().in(SysRole::getId,roleIds));
    }

    public boolean existNameExist(String name, Integer id) {
        LambdaQueryWrapper<SysRole> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysRole::getName, name)
                .ne(Objects.nonNull(id), SysRole::getId, id);

        return sysRoleMapper.selectCount(queryWrapper) > 0;
    }
}




