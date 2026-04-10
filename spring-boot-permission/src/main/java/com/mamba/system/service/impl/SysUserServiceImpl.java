package com.mamba.system.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mamba.common.exception.BusinessException;
import com.mamba.common.result.PageResult;
import com.mamba.system.dto.PageQueryDTO;
import com.mamba.system.dto.SysUserDTO;
import com.mamba.system.entity.SysAcl;
import com.mamba.system.entity.SysDept;
import com.mamba.system.entity.SysRole;
import com.mamba.system.entity.SysRoleAcl;
import com.mamba.system.entity.SysRoleUser;
import com.mamba.system.entity.SysUser;
import com.mamba.system.mapper.SysAclMapper;
import com.mamba.system.mapper.SysDeptMapper;
import com.mamba.system.mapper.SysRoleAclMapper;
import com.mamba.system.mapper.SysRoleMapper;
import com.mamba.system.mapper.SysRoleUserMapper;
import com.mamba.system.mapper.SysUserMapper;
import com.mamba.system.service.SysLogService;
import com.mamba.system.service.SysUserService;
import com.mamba.system.vo.SysRoleVO;
import com.mamba.system.vo.SysUserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysDeptMapper sysDeptMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysRoleUserMapper sysRoleUserMapper;

    @Resource
    private SysRoleAclMapper sysRoleAclMapper;

    @Resource
    private SysAclMapper sysAclMapper;

    @Resource
    private SysLogService sysLogService;

    @Override
    public SysUserVO login(String username, String password) {
        String md5Password = DigestUtil.md5Hex(password);
        SysUser user = sysUserMapper.selectOne(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, username)
                        .eq(SysUser::getPassword, md5Password)
        );
        Optional.ofNullable(user)
                .orElseThrow(() -> new BusinessException("用户名或密码错误"));
        if (!Integer.valueOf(1).equals(user.getStatus())) {
            throw new BusinessException("用户已被冻结");
        }
        StpUtil.login(user.getId());

        SysUserVO vo = convertToVO(user);
        List<Integer> roleIds = sysRoleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, user.getId())
        ).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());

        List<SysRoleVO> roleList = Collections.emptyList();
        if (!roleIds.isEmpty()) {
            roleList = sysRoleMapper.selectBatchIds(roleIds).stream()
                    .map(role -> {
                        SysRoleVO roleVO = new SysRoleVO();
                        BeanUtils.copyProperties(role, roleVO);
                        roleVO.setTypeDesc(Integer.valueOf(1).equals(role.getType()) ? "管理员角色" : "其他");
                        roleVO.setStatusDesc(Integer.valueOf(1).equals(role.getStatus()) ? "可用" : "冻结");
                        return roleVO;
                    }).collect(Collectors.toList());
        }
        vo.setRoleIds(roleIds);
        vo.setRoleList(roleList);
        return vo;
    }

    @Override
    public SysUserVO getUserInfo() {
        int userId = StpUtil.getLoginIdAsInt();
        SysUser user = sysUserMapper.selectById(userId);
        Optional.ofNullable(user)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        SysUserVO vo = convertToVO(user);

        Optional.ofNullable(sysDeptMapper.selectById(user.getDeptId()))
                .ifPresent(dept -> vo.setDeptName(dept.getName()));

        List<Integer> roleIds = sysRoleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, user.getId())
        ).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());

        List<SysRoleVO> roleList = Collections.emptyList();
        if (!roleIds.isEmpty()) {
            roleList = sysRoleMapper.selectBatchIds(roleIds).stream()
                    .map(role -> {
                        SysRoleVO roleVO = new SysRoleVO();
                        BeanUtils.copyProperties(role, roleVO);
                        roleVO.setTypeDesc(Integer.valueOf(1).equals(role.getType()) ? "管理员角色" : "其他");
                        roleVO.setStatusDesc(Integer.valueOf(1).equals(role.getStatus()) ? "可用" : "冻结");
                        return roleVO;
                    }).collect(Collectors.toList());
        }
        vo.setRoleIds(roleIds);
        vo.setRoleList(roleList);
        return vo;
    }

    @Override
    public Map<String, Object> getUserPermissions() {
        int userId = StpUtil.getLoginIdAsInt();

        // 获取用户角色
        List<Integer> roleIds = sysRoleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getUserId, userId)
        ).stream().map(SysRoleUser::getRoleId).collect(Collectors.toList());

        if (roleIds.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("menus", Collections.emptyList());
            map.put("permissions", Collections.emptyList());
            return map;
        }

        // 获取角色权限ID
        List<Integer> aclIds = sysRoleAclMapper.selectList(
                new LambdaQueryWrapper<SysRoleAcl>().in(SysRoleAcl::getRoleId, roleIds)
        ).stream().map(SysRoleAcl::getAclId).distinct().collect(Collectors.toList());

        if (aclIds.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            map.put("menus", Collections.emptyList());
            map.put("permissions", Collections.emptyList());
            return map;
        }

        // 获取所有权限
        List<SysAcl> allAcls = sysAclMapper.selectList(
                new LambdaQueryWrapper<SysAcl>()
                        .in(SysAcl::getId, aclIds)
                        .eq(SysAcl::getStatus, 1)
        );

        // 菜单权限（type=1，URL列表）
        List<String> menus = allAcls.stream()
                .filter(acl -> Integer.valueOf(1).equals(acl.getType()))
                .map(SysAcl::getUrl)
                .filter(url -> url != null && !url.isEmpty())
                .collect(Collectors.toList());

        // 按钮权限（所有权限的code列表）
        List<String> permissions = allAcls.stream()
                .map(SysAcl::getCode)
                .filter(code -> code != null && !code.isEmpty())
                .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("menus", menus);
        result.put("permissions", permissions);
        return result;
    }

    @Override
    public PageResult<SysUserVO> page(PageQueryDTO query) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        Optional.ofNullable(query.getKeyword())
                .filter(StringUtils::hasText)
                .ifPresent(keyword -> wrapper.and(w -> w
                        .like(SysUser::getUsername, keyword)
                        .or().like(SysUser::getTelephone, keyword)
                        .or().like(SysUser::getMail, keyword)
                ));
        Optional.ofNullable(query.getStatus())
                .ifPresent(status -> wrapper.eq(SysUser::getStatus, status));

        Page<SysUser> page = sysUserMapper.selectPage(
                new Page<>(query.getPageNum(), query.getPageSize()), wrapper
        );

        log.info("分页查询用户: pageNum={}, pageSize={}, total={}", query.getPageNum(), query.getPageSize(), page.getTotal());

        List<Integer> deptIds = page.getRecords().stream()
                .map(SysUser::getDeptId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.toList());

        Map<Integer, String> deptNameMap;
        if (!deptIds.isEmpty()) {
            deptNameMap = sysDeptMapper.selectBatchIds(deptIds).stream()
                    .collect(Collectors.toMap(SysDept::getId, SysDept::getName, (a, b) -> a));
        } else {
            deptNameMap = Collections.emptyMap();
        }

        List<SysUserVO> voList = page.getRecords().stream()
                .map(user -> {
                    SysUserVO vo = convertToVO(user);
                    vo.setDeptName(deptNameMap.getOrDefault(user.getDeptId(), ""));
                    return vo;
                }).collect(Collectors.toList());

        Page<SysUserVO> voPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        voPage.setRecords(voList);
        return PageResult.of(voPage);
    }

    @Override
    public void saveUser(SysUserDTO dto) {
        long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, dto.getUsername())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = new SysUser();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(StringUtils.hasText(dto.getPassword())
                ? DigestUtil.md5Hex(dto.getPassword())
                : DigestUtil.md5Hex("12345678"));
        user.setStatus(1);
        sysUserMapper.insert(user);
    }

    @Override
    public void updateUser(SysUserDTO dto) {
        Optional.ofNullable(dto.getId())
                .orElseThrow(() -> new BusinessException("用户ID不能为空"));

        long count = sysUserMapper.selectCount(
                new LambdaQueryWrapper<SysUser>()
                        .eq(SysUser::getUsername, dto.getUsername())
                        .ne(SysUser::getId, dto.getId())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        SysUser user = sysUserMapper.selectById(dto.getId());
        Optional.ofNullable(user)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        BeanUtils.copyProperties(dto, user);
        if (!StringUtils.hasText(dto.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(DigestUtil.md5Hex(dto.getPassword()));
        }
        sysUserMapper.updateById(user);
    }

    @Override
    public void changeStatus(Integer userId, Integer status) {
        SysUser user = sysUserMapper.selectById(userId);
        Optional.ofNullable(user)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        String oldValue = JSON.toJSONString(user);
        user.setStatus(status);
        sysUserMapper.updateById(user);
        String newValue = JSON.toJSONString(user);
        sysLogService.saveLog(2, userId, oldValue, newValue);
    }

    @Override
    public void deleteUsers(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        ids.stream().forEach(id -> {
            SysUser user = sysUserMapper.selectById(id);
            if (user != null) {
                String oldValue = JSON.toJSONString(user);
                user.setStatus(2);
                sysUserMapper.updateById(user);
                sysLogService.saveLog(2, id, oldValue, JSON.toJSONString(user));
            }
        });
    }

    @Override
    public List<SysUserVO> listByRoleId(Integer roleId) {
        List<Integer> userIds = sysRoleUserMapper.selectList(
                new LambdaQueryWrapper<SysRoleUser>().eq(SysRoleUser::getRoleId, roleId)
        ).stream().map(SysRoleUser::getUserId).collect(Collectors.toList());

        if (userIds.isEmpty()) {
            return Collections.emptyList();
        }

        Map<Integer, String> deptNameMap = sysUserMapper.selectBatchIds(userIds).stream()
                .map(SysUser::getDeptId)
                .filter(id -> id != null)
                .distinct()
                .collect(Collectors.collectingAndThen(Collectors.toList(), deptIds -> {
                    if (deptIds.isEmpty()) return Collections.<Integer, String>emptyMap();
                    return sysDeptMapper.selectBatchIds(deptIds).stream()
                            .collect(Collectors.toMap(SysDept::getId, SysDept::getName, (a, b) -> a));
                }));

        return sysUserMapper.selectBatchIds(userIds).stream()
                .map(user -> {
                    SysUserVO vo = convertToVO(user);
                    vo.setDeptName(deptNameMap.getOrDefault(user.getDeptId(), ""));
                    return vo;
                }).collect(Collectors.toList());
    }

    @Override
    public List<SysUserVO> listByDeptId(Integer deptId) {
        List<SysUser> users = sysUserMapper.selectList(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeptId, deptId)
        );
        Map<Integer, String> deptNameMap = Optional.ofNullable(sysDeptMapper.selectById(deptId))
                .map(dept -> Collections.singletonMap(dept.getId(), dept.getName()))
                .orElse(Collections.emptyMap());

        return users.stream()
                .map(user -> {
                    SysUserVO vo = convertToVO(user);
                    vo.setDeptName(deptNameMap.getOrDefault(user.getDeptId(), ""));
                    return vo;
                }).collect(Collectors.toList());
    }

    private SysUserVO convertToVO(SysUser user) {
        SysUserVO vo = new SysUserVO();
        BeanUtils.copyProperties(user, vo);
        vo.setStatusDesc(Integer.valueOf(1).equals(user.getStatus()) ? "正常"
                : Integer.valueOf(0).equals(user.getStatus()) ? "冻结" : "删除");
        return vo;
    }
}
