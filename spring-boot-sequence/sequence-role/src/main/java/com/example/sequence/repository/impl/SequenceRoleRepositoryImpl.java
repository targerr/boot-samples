package com.example.sequence.repository.impl;

import com.example.sequence.entity.SequenceRole;
import com.example.sequence.dao.mapper.SequenceRoleMapper;
import com.example.sequence.repository.SequenceRoleRepository;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.example.common.exception.ArgumentAssert;

import java.util.List;

/**
* <p>
* 流水号规则表 仓储实现类
* </p>
*
* @author wgs
* @since 2024-09-29
*/
@Slf4j
@Service
public class SequenceRoleRepositoryImpl extends ServiceImpl<SequenceRoleMapper, SequenceRole> implements SequenceRoleRepository {


    /**
    * 集合条件查询
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @return List<SequenceRole>
    */
    @Override
    public List<SequenceRole> listEnhance(SequenceRole sequenceRole) {
    QueryWrapper<SequenceRole> queryWrapper = new QueryWrapper<>(sequenceRole);
    buildListQueryWrapper(sequenceRole, queryWrapper);
    return baseMapper.selectList(queryWrapper);
    }

    /**
    * 构造list查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildListQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
    * 集合,增强返回参数追加
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRoleList:
    * @return List<SequenceRole>
    */
    private List<SequenceRole> assignment(List<SequenceRole> sequenceRoleList) {
    sequenceRoleList.forEach(sequenceRole -> {
    });
    return sequenceRoleList;
    }

    /**
    * 分页条件查询
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @param page: 分页入参
    * @return Page<SequenceRole>
    */
    @Override
    public Page<SequenceRole> pageEnhance(Page<SequenceRole> page, SequenceRole sequenceRole) {
    QueryWrapper<SequenceRole> queryWrapper = new QueryWrapper<>(sequenceRole);
    buildPageQueryWrapper(sequenceRole, queryWrapper);
    return baseMapper.selectPage(page, queryWrapper);
    }

    /**
    * 构造分页查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildPageQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
    * 分页,增强返回参数追加
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRoleList:
    * @return Page<SequenceRole>
    */
    private Page<SequenceRole> assignment(Page<SequenceRole> sequenceRoleList) {
    sequenceRoleList.getRecords().forEach(sequenceRole -> {
    });
    return sequenceRoleList;
    }

    /**
    * 单条条件查询
    * @author wgs
    * @since 2024-09-29
    * @param id: id
    * @return SequenceRole
    */
    @Override
    public SequenceRole getOneEnhanceById(Long id) {
    return assignment(baseMapper.selectById(id));
    }

    /**
    * 根据查询参数查询单条结果
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @return SequenceRole
    */
    @Override
    public SequenceRole getOneByParam(SequenceRole sequenceRole) {
    QueryWrapper<SequenceRole> queryWrapper = new QueryWrapper<>(sequenceRole);
    buildOneQueryWrapper(sequenceRole, queryWrapper);
    return assignment(baseMapper.selectOne(queryWrapper));
    }

    /**
    * 构造单条查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildOneQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
    * 单条，增强返回参数追加
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole:
    * @return SequenceRole
    */
    private SequenceRole assignment(SequenceRole sequenceRole) {
    return sequenceRole;
    }

    /**
    * 新增
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @return Boolean
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long saveEnhance(SequenceRole sequenceRole) {
    boolean result = baseMapper.insert(sequenceRole) > 0;
    ArgumentAssert.isTrue(result,"保存失败");
    return sequenceRole.getId();
    }

    /**
    * 修改
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @return Boolean
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long updateEnhance(SequenceRole sequenceRole) {
    LambdaUpdateWrapper<SequenceRole> updateWrapper = new LambdaUpdateWrapper<>();
    buildUpdateWrapper(sequenceRole, updateWrapper);
    boolean result = baseMapper.update(sequenceRole, updateWrapper) > 0;
    ArgumentAssert.isTrue(result,"更新失败");
    return sequenceRole.getId();
    }

    /**
    * 构造更新条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @param updateWrapper: 更新Wrapper
    */
    private void buildUpdateWrapper(SequenceRole sequenceRole, LambdaUpdateWrapper<SequenceRole> updateWrapper) {
    // 更新条件以及设值
    updateWrapper.eq(SequenceRole::getId,sequenceRole.getId());
    }

    /**
    * 删除
    * @author wgs
    * @since 2024-09-29
    * @param id: id
    * @return Boolean
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Boolean removeEnhanceById(Long id) {
    return baseMapper.deleteById(id) > 0;
    }
    }
