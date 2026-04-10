package com.mamba.system.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.system.dto.RoleUserDTO;
import com.mamba.system.entity.SysRoleUser;
import com.mamba.system.mapper.SysRoleUserMapper;
import com.mamba.system.service.SysLogService;
import com.mamba.system.service.SysRoleUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser> implements SysRoleUserService {

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public List<Integer> getUserIdsByRoleId(Integer roleId) {
        List<SysRoleUser> roleUsers = sysRoleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getRoleId, roleId)
        );
        List<Integer> userIds = roleUsers.stream().map(SysRoleUser::getUserId).collect(Collectors.toList());
        log.info("查询角色下的用户ID列表: roleId={}, userIds={}", roleId, userIds);
        return userIds;
    }

    @Override
    public List<Integer> getRoleIdsByUserId(Integer userId) {
        return sysRoleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, userId)
        ).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());
    }

    @Override
    public void assignUsers(RoleUserDTO dto) {
        List<Integer> oldUserIds = getUserIdsByRoleId(dto.getRoleId());

        sysRoleUserMapper.delete(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getRoleId, dto.getRoleId())
        );

        dto.getUserIds().stream().forEach(userId -> {
            SysRoleUser roleUser = new SysRoleUser();
            roleUser.setRoleId(dto.getRoleId());
            roleUser.setUserId(userId);
            sysRoleUserMapper.insert(roleUser);
        });

        sysLogService.saveLog(6, dto.getRoleId(),
                JSON.toJSONString(oldUserIds), JSON.toJSONString(dto.getUserIds()));
    }
}
