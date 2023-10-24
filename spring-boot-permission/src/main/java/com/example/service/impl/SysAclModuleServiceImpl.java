package com.example.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SysAcl;
import com.example.entity.SysAclModule;
import com.example.enums.ResultEnum;
import com.example.exception.PreException;
import com.example.mapper.SysAclMapper;
import com.example.req.AclModuleReq;
import com.example.service.SysAclModuleService;
import com.example.mapper.SysAclModuleMapper;
import com.example.util.LevelUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author wanggaoshuai
 * @description 针对表【sys_acl_module】的数据库操作Service实现
 * @createDate 2023-10-20 15:45:01
 */
@Service
public class SysAclModuleServiceImpl extends ServiceImpl<SysAclModuleMapper, SysAclModule>
        implements SysAclModuleService {
    @Resource
    private SysAclModuleMapper sysAclModuleMapper;
    @Resource
    private SysAclMapper sysAclMapper;

    @Override
    public void saveAclModule(AclModuleReq aclModule) {
        if (checkNameExist(aclModule)) {
            throw new PreException(ResultEnum.ACL_MODULE_NAME_ERROR);
        }

        SysAclModule module = SysAclModule.builder().name(aclModule.getName()).parentId(aclModule.getParentId()).seq(aclModule.getSeq())
                .status(aclModule.getStatus()).remark(aclModule.getRemark()).build();
        module.setLevel(LevelUtil.calculateLevel(getLevel(aclModule.getParentId()), aclModule.getParentId()));
//        module.setOperator(RequestHolder.getCurrentUser().getUsername());
//        module.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        module.setOperateTime(new Date());
        sysAclModuleMapper.insert(module);
    }

    public boolean checkNameExist(AclModuleReq aclModule) {
        LambdaQueryWrapper<SysAclModule> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysAclModule::getName, aclModule.getName())
                .eq(SysAclModule::getParentId, aclModule.getParentId())
                .ne(Objects.nonNull(aclModule.getId()), SysAclModule::getId, aclModule.getId());

        return sysAclModuleMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAclModule(AclModuleReq aclModule) {
        SysAclModule before = sysAclModuleMapper.selectById(aclModule.getId());
        if (Objects.isNull(before)) {
            throw new PreException(ResultEnum.ACL_MODULE_NAME_ERROR);
        }

        if (checkNameExist(aclModule)) {
            throw new PreException(ResultEnum.ACL_MODULE_NAME_ERROR);
        }

        String afterLevel = LevelUtil.calculateLevel(getLevel(aclModule.getParentId()), aclModule.getParentId());
        SysAclModule after = SysAclModule.builder().name(aclModule.getName()).parentId(aclModule.getParentId()).seq(aclModule.getSeq())
                .status(aclModule.getStatus()).remark(aclModule.getRemark()).build();
        after.setLevel(afterLevel);
//        module.setOperator(RequestHolder.getCurrentUser().getUsername());
//        module.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
        after.setOperateTime(new Date());

        updateWithChild(before, after);
    }

    @Override
    public void deleteAclModule(int id) {
        SysAclModule sysAclModule = sysAclModuleMapper.selectById(id);
        if (Objects.isNull(sysAclModule)) {
            throw new PreException(ResultEnum.ACL_MODULE_NAME_ERROR);
        }

        final List<SysAclModule> childModelListByLevel = getChildModelListByLevel(sysAclModule.getLevel());
        if (CollectionUtils.isNotEmpty(childModelListByLevel)) {
            throw new PreException(ResultEnum.ACL_MODULE_NAME_ERROR);
        }

        if (countByAclModuleId(id)) {
            throw new PreException(ResultEnum.ACL_MODULE_NAME_ERROR);
        }

        sysAclModuleMapper.deleteById(id);
    }

    private boolean countByAclModuleId(int id) {
        LambdaQueryWrapper<SysAcl> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(SysAcl::getAclModuleId, id);
        return sysAclMapper.selectCount(queryWrapper) > 0;
    }

    private void updateWithChild(SysAclModule before, SysAclModule after) {
        String newLevelPrefix = after.getLevel();
        String oldLevelPrefix = before.getLevel();
        if (newLevelPrefix.equals(oldLevelPrefix)) {
            sysAclModuleMapper.updateById(after);
            return;
        }

        List<SysAclModule> sysAclModules = getChildModelListByLevel(oldLevelPrefix);
        if (CollectionUtils.isEmpty(sysAclModules)) {
            sysAclModuleMapper.updateById(after);
        }

        checkLevel(after, sysAclModules);
        for (SysAclModule module : sysAclModules) {
            String level = module.getLevel();
            if (level.indexOf(oldLevelPrefix) == 0) {
                level = newLevelPrefix + level.substring(oldLevelPrefix.length());
                module.setLevel(level);
                sysAclModuleMapper.updateById(module);
            }
        }

        sysAclModuleMapper.updateById(after);
    }

    private void checkLevel(SysAclModule after, List<SysAclModule> sysAclModules) {
        sysAclModules.forEach(dept -> {
            if (StringUtils.contains(dept.getLevel(), Convert.toStr(after.getId()))) {
                throw new PreException(ResultEnum.DEPT_PARENT_ERROR);
            }
        });
    }

    private List<SysAclModule> getChildModelListByLevel(String oldLevelPrefix) {
        LambdaQueryWrapper<SysAclModule> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.likeRight(SysAclModule::getLevel, oldLevelPrefix + ".");

        return sysAclModuleMapper.selectList(queryWrapper);
    }

    private String getLevel(Integer aclModuleId) {
        SysAclModule aclModule = sysAclModuleMapper.selectById(aclModuleId);
        if (aclModule == null) {
            return null;
        }
        return aclModule.getLevel();
    }


}




