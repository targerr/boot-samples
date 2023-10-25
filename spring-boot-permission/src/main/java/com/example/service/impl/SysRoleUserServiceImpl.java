package com.example.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.context.ReqInfoContext;
import com.example.entity.SysRole;
import com.example.entity.SysRoleUser;
import com.example.entity.SysUser;
import com.example.enums.ResultEnum;
import com.example.exception.PreException;
import com.example.mapper.SysRoleMapper;
import com.example.mapper.SysUserMapper;
import com.example.req.RoleUserReq;
import com.example.service.SysRoleUserService;
import com.example.mapper.SysRoleUserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wanggaoshuai
 * @description 针对表【sys_role_user】的数据库操作Service实现
 * @createDate 2023-10-19 14:34:10
 */
@Service
public class SysRoleUserServiceImpl extends ServiceImpl<SysRoleUserMapper, SysRoleUser>
        implements SysRoleUserService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysRoleUserMapper sysRoleUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void changeRoleUsers(RoleUserReq roleUserReq) {
        final SysRole sysRole = sysRoleMapper.selectById(roleUserReq.getRoleId());
        if (Objects.isNull(sysRole)) {
            throw new PreException(ResultEnum.ROLE_NOT_EXIST);
        }

        updateRoleUsers(roleUserReq);
    }

    @Override
    public JSON querySelectedAandUnselectedByRoleId(int roleId) {
        List<SysUser> sysUserList = sysUserMapper.selectList(new LambdaQueryWrapper<SysUser>().eq(SysUser::getStatus, 1));
        List<SysUser> selectUserList = getUserByRoleId(roleId);

        List<Integer> selectUserIds = selectUserList.stream().map(SysUser::getId).collect(Collectors.toList());
        List<SysUser> unselectedList = sysUserList.stream()
                .filter(user -> !selectUserIds.contains(user.getId()))
                .collect(Collectors.toList());

        JSONObject json = new JSONObject();
        json.put("selected", selectUserList);
        json.put("unselected", unselectedList);
        return json;
    }

    private List<SysUser> getUserByRoleId(int roleId) {
        LambdaQueryWrapper<SysRoleUser> query = Wrappers.lambdaQuery();
        query.eq(SysRoleUser::getRoleId, roleId);
        List<SysRoleUser> sysRoleUserList = sysRoleUserMapper.selectList(query);
        if (CollectionUtils.isEmpty(sysRoleUserList)) {
            return new ArrayList<>();
        }

        List<Integer> userIdList = sysRoleUserList.stream().map(SysRoleUser::getUserId).collect(Collectors.toList());
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(SysUser::getId, userIdList);
        List<SysUser> sysUserList = sysUserMapper.selectList(queryWrapper);
        return CollectionUtils.isEmpty(sysUserList) ? new ArrayList<>() : sysUserList;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRoleUsers(RoleUserReq roleUserReq) {
        LambdaQueryWrapper<SysRoleUser> deleteWrapper = Wrappers.lambdaQuery();
        deleteWrapper.eq(SysRoleUser::getRoleId, roleUserReq.getRoleId());
        sysRoleUserMapper.delete(deleteWrapper);
        final ReqInfoContext.ReqInfo reqInfo = ReqInfoContext.getReqInfo();
        List<SysRoleUser> insertList = roleUserReq.getUserIds().stream()
                .map(userId -> SysRoleUser.builder()
                        .roleId(roleUserReq.getRoleId())
                        .userId(userId)
                        .operateTime(new Date())
                        .operateIp(reqInfo.getClientIp())
                        .operator(reqInfo.getUser().getUserName())
                        .build())
                .collect(Collectors.toList());

        insertList.forEach(e -> {
            sysRoleUserMapper.insert(e);
        });
    }
}




