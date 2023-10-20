package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.util.IpUtil;
import com.example.entity.SysDept;
import com.example.entity.SysUser;
import com.example.enums.ResultEnum;
import com.example.exception.PreException;
import com.example.mapper.SysDeptMapper;
import com.example.mapper.SysUserMapper;
import com.example.req.DeptReq;
import com.example.service.SysDeptService;
import com.example.util.LevelUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.SocketException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wanggaoshuai
 * @description 针对表【sys_dept】的数据库操作Service实现
 * @createDate 2023-10-19 14:33:09
 */
@Service
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept>
        implements SysDeptService {
    @Resource
    private SysDeptMapper sysDeptMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public void saveDept(DeptReq deptReq)   {
        if (checkNameExists(deptReq)) {
            throw new PreException(ResultEnum.DEPT_NAME_ERROR);
        }

        SysDept sysDept = new SysDept();
        BeanUtil.copyProperties(deptReq, sysDept);
        sysDept.setLevel(
                LevelUtil.calculateLevel(getLevel(deptReq.getParentId()), deptReq.getParentId())
        );
        try {
            sysDept.setOperateIp(IpUtil.getLocalIp4Address());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        sysDept.setOperateTime(new Date());

        sysDeptMapper.insert(sysDept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(DeptReq param) {
        SysDept before = queryDeptById(param.getId());
        if (Objects.isNull(before)) {
            throw new PreException(ResultEnum.DEPT_NOT_EXIST);
        }

        if (checkNameExists(param)) {
            throw new PreException(ResultEnum.DEPT_NAME_ERROR);
        }

        String afterLevel = LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId());
        SysDept after = SysDept.builder().id(param.getId()).name(param.getName()).parentId(param.getParentId())
                .seq(param.getSeq()).remark(param.getRemark()).build();
        after.setLevel(afterLevel);
        after.setOperateTime(new Date());
        if (StringUtils.equals(afterLevel, "0")) {
            throw new PreException(ResultEnum.DEPT_ROOT_ERROR);
        }

        updateWithChild(before, after);
    }

    private void updateWithChild(SysDept before, SysDept after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (!newLevelPrefix.equals(oldLevelPrefix)) {
            List<SysDept> deptList = getChildDeptListByLevel(oldLevelPrefix);
            checkLevel(after, deptList);

            for (SysDept dept : deptList) {
                String level = dept.getLevel();
                if (level.startsWith(oldLevelPrefix)) {
                    level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                    dept.setLevel(level);
                    sysDeptMapper.updateById(dept);
                }
            }
        }

        // 更新当前部门信息
        sysDeptMapper.updateById(after);
    }

    private void checkLevel(SysDept after, List<SysDept> deptList) {
        if (CollectionUtils.isEmpty(deptList)) {
            return;
        }
        deptList.forEach(dept -> {
            if (StringUtils.contains(dept.getLevel(), Convert.toStr(after.getId()))) {
                throw new PreException(ResultEnum.DEPT_PARENT_ERROR);
            }
        });
    }

    private List<SysDept> getChildDeptListByLevel(String oldLevelPrefix) {
        QueryWrapper<SysDept> queryWrapper = new QueryWrapper<>();
        // 构建 LIKE CONCAT(#{level,jdbcType=VARCHAR}, ',%') 查询条件
        queryWrapper.likeRight("level", oldLevelPrefix + ".");

        return sysDeptMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteDept(Integer id) {
        SysDept before = queryDeptById(id);
        if (Objects.isNull(before)) {
            throw new PreException(ResultEnum.DEPT_NOT_EXIST);
        }
        if (countByParentId(id)) {
            throw new PreException(ResultEnum.DEPT_SUBSECTOR_ERROR);
        }
        if (countByDeptId(id)) {
            throw new PreException(ResultEnum.DEPT_USER_ERROR);
        }

        sysDeptMapper.deleteById(id);
    }

    private boolean countByParentId(Integer id) {
        LambdaQueryWrapper<SysDept> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysDept::getParentId, id);

        return sysDeptMapper.selectCount(queryWrapper) > 0;
    }

    private boolean countByDeptId(Integer id) {
        LambdaQueryWrapper<SysUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysUser::getDeptId, id);
        return sysUserMapper.selectCount(queryWrapper) > 0;
    }

    public boolean checkNameExists(DeptReq deptReq) {
        LambdaQueryWrapper<SysDept> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysDept::getName, deptReq.getName())
                .eq(SysDept::getParentId, deptReq.getParentId())
                .ne(Objects.nonNull(deptReq.getId()), SysDept::getId, deptReq.getId());

        return sysDeptMapper.selectCount(queryWrapper) > 0;

    }

    private String getLevel(Integer deptId) {
        SysDept dept = queryDeptById(deptId);
        if (dept == null) return null;
        return dept.getLevel();
    }

    private SysDept queryDeptById(Integer deptId) {
        SysDept dept = sysDeptMapper.selectById(deptId);
        if (Objects.isNull(dept)) {
            return null;
        }
        return dept;
    }
}




