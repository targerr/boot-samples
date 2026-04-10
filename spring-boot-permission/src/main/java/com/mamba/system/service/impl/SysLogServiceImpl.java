package com.mamba.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.common.exception.BusinessException;
import com.mamba.common.result.PageResult;
import com.mamba.common.util.IpUtil;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.entity.SysAcl;
import com.mamba.system.entity.SysAclModule;
import com.mamba.system.entity.SysDept;
import com.mamba.system.entity.SysLog;
import com.mamba.system.entity.SysRole;
import com.mamba.system.entity.SysRoleAcl;
import com.mamba.system.entity.SysRoleUser;
import com.mamba.system.entity.SysUser;
import com.mamba.system.mapper.*;
import com.mamba.system.service.SysLogService;
import com.mamba.system.vo.SysLogVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysAclModuleMapper sysAclModuleMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Override
    public PageResult<SysLogVO> page(PageQueryDTO query) {
        LambdaQueryWrapper<SysLog> wrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(query.getType())
                .ifPresent(type -> wrapper.eq(SysLog::getType, type));
        Optional.ofNullable(query.getKeyword())
                .filter(StringUtils::hasText)
                .ifPresent(keyword -> wrapper.like(SysLog::getOperator, keyword));
        wrapper.orderByDesc(SysLog::getOperateTime);

        Page<SysLog> page = sysLogMapper.selectPage(
                new Page<>(query.getPageNum(), query.getPageSize()), wrapper
        );

        List<SysLogVO> voList = page.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        Page<SysLogVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return PageResult.of(voPage);
    }

    @Override
    public void restore(Integer logId) {
        SysLog sysLog = sysLogMapper.selectById(logId);
        Optional.ofNullable(sysLog)
                .orElseThrow(() -> new BusinessException("日志不存在"));
        if (!Integer.valueOf(0).equals(sysLog.getStatus())) {
            throw new BusinessException("该日志已复原，不能重复操作");
        }

        Integer type = sysLog.getType();
        if (type == 1) {
            restoreDept(sysLog);
        } else if (type == 2) {
            restoreUser(sysLog);
        } else if (type == 3) {
            restoreAclModule(sysLog);
        } else if (type == 4) {
            restoreAcl(sysLog);
        } else if (type == 5) {
            restoreRole(sysLog);
        } else if (type == 6) {
            restoreRoleUser(sysLog);
        } else if (type == 7) {
            restoreRoleAcl(sysLog);
        } else {
            throw new BusinessException("未知的日志类型: " + type);
        }

        sysLog.setStatus(1);
        sysLogMapper.updateById(sysLog);
    }

    @Override
    public void saveLog(Integer type, Integer targetId, String oldValue, String newValue) {
        SysLog sysLog = new SysLog();
        sysLog.setType(type);
        sysLog.setTargetId(targetId);
        sysLog.setOldValue(oldValue);
        sysLog.setNewValue(newValue);
        sysLog.setOperateTime(LocalDateTime.now());

        try {
            sysLog.setOperator(StpUtil.getLoginIdAsString());
        } catch (Exception e) {
            sysLog.setOperator("system");
        }

        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                sysLog.setOperateIp(IpUtil.getIpAddr(request));
            }
        } catch (Exception e) {
            sysLog.setOperateIp("unknown");
        }

        sysLog.setStatus(0);
        sysLogMapper.insert(sysLog);
    }

    private void restoreUser(SysLog sysLog) {
        SysUser oldUser = JSON.parseObject(sysLog.getOldValue(), SysUser.class);
        Optional.ofNullable(oldUser)
                .orElseThrow(() -> new BusinessException("旧值数据解析失败"));

        SysUser currentUser = sysUserMapper.selectById(sysLog.getTargetId());
        Optional.ofNullable(currentUser)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        oldUser.setId(sysLog.getTargetId());
        oldUser.setPassword(currentUser.getPassword());
        sysUserMapper.updateById(oldUser);
    }

    private void restoreDept(SysLog sysLog) {
        SysDept oldDept = JSON.parseObject(sysLog.getOldValue(), SysDept.class);
        Optional.ofNullable(oldDept)
                .orElseThrow(() -> new BusinessException("旧值数据解析失败"));

        SysDept currentDept = sysDeptMapper.selectById(sysLog.getTargetId());
        Optional.ofNullable(currentDept)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        String oldLevel = currentDept.getLevel();
        oldDept.setId(sysLog.getTargetId());
        sysDeptMapper.updateById(oldDept);

        String newLevel = oldDept.getLevel();
        if (!newLevel.equals(oldLevel)) {
            List<SysDept> children = sysDeptMapper.selectList(
                    new LambdaQueryWrapper<SysDept>().likeRight(SysDept::getLevel, oldLevel + ".")
            );
            children.stream().forEach(child -> {
                child.setLevel(newLevel + child.getLevel().substring(oldLevel.length()));
                sysDeptMapper.updateById(child);
            });
        }
    }

    private void restoreAclModule(SysLog sysLog) {
        SysAclModule oldModule = JSON.parseObject(sysLog.getOldValue(), SysAclModule.class);
        Optional.ofNullable(oldModule)
                .orElseThrow(() -> new BusinessException("旧值数据解析失败"));
        oldModule.setId(sysLog.getTargetId());
        sysAclModuleMapper.updateById(oldModule);
    }

    private void restoreAcl(SysLog sysLog) {
        SysAcl oldAcl = JSON.parseObject(sysLog.getOldValue(), SysAcl.class);
        Optional.ofNullable(oldAcl)
                .orElseThrow(() -> new BusinessException("旧值数据解析失败"));
        oldAcl.setId(sysLog.getTargetId());
        sysAclMapper.updateById(oldAcl);
    }

    private void restoreRole(SysLog sysLog) {
        SysRole oldRole = JSON.parseObject(sysLog.getOldValue(), SysRole.class);
        Optional.ofNullable(oldRole)
                .orElseThrow(() -> new BusinessException("旧值数据解析失败"));
        oldRole.setId(sysLog.getTargetId());
        sysRoleMapper.updateById(oldRole);
    }

    private void restoreRoleUser(SysLog sysLog) {
        sysRoleUserMapper.delete(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getRoleId, sysLog.getTargetId())
        );

        List<Integer> userIds = JSON.parseArray(sysLog.getOldValue(), Integer.class);
        Optional.ofNullable(userIds).orElse(new ArrayList<>()).stream().forEach(userId -> {
            SysRoleUser roleUser = new SysRoleUser();
            roleUser.setRoleId(sysLog.getTargetId());
            roleUser.setUserId(userId);
            sysRoleUserMapper.insert(roleUser);
        });
    }

    private void restoreRoleAcl(SysLog sysLog) {
        sysRoleAclMapper.delete(
                new LambdaQueryWrapper<SysRoleAcl>().eq(SysRoleAcl::getRoleId, sysLog.getTargetId())
        );

        List<Integer> aclIds = JSON.parseArray(sysLog.getOldValue(), Integer.class);
        Optional.ofNullable(aclIds).orElse(new ArrayList<>()).stream().forEach(aclId -> {
            SysRoleAcl roleAcl = new SysRoleAcl();
            roleAcl.setRoleId(sysLog.getTargetId());
            roleAcl.setAclId(aclId);
            sysRoleAclMapper.insert(roleAcl);
        });
    }

    private SysLogVO convertToVO(SysLog sysLog) {
        SysLogVO vo = new SysLogVO();
        BeanUtils.copyProperties(sysLog, vo);
        vo.setTypeDesc(getTypeDesc(sysLog.getType()));
        vo.setStatusDesc(Integer.valueOf(1).equals(sysLog.getStatus()) ? "已复原" : "未复原");
        return vo;
    }

    private String getTypeDesc(Integer type) {
        if (type == null) return "";
        switch (type) {
            case 1:
                return "部门";
            case 2:
                return "用户";
            case 3:
                return "权限模块";
            case 4:
                return "权限";
            case 5:
                return "角色";
            case 6:
                return "角色用户关系";
            case 7:
                return "角色权限关系";
            default:
                return "未知";
        }
    }
}
