package com.example.sequence.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.sequence.dao.mapper.SequenceRoleMapper;
import com.example.sequence.entity.SequenceRole;
import com.example.sequence.repository.SequenceRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     *
     * @param sequenceRole: 流水号规则表
     * @return List<SequenceRole>
     * @author wgs
     * @since 2024-09-29
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
     * @param sequenceRole: 流水号规则表
     * @param queryWrapper: 查询Wrapper
     * @author wgs
     * @since 2024-09-29
     */
    private void buildListQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
     * 集合,增强返回参数追加
     *
     * @param sequenceRoleList:
     * @return List<SequenceRole>
     * @author wgs
     * @since 2024-09-29
     */
    private List<SequenceRole> assignment(List<SequenceRole> sequenceRoleList) {
        sequenceRoleList.forEach(sequenceRole -> {
        });
        return sequenceRoleList;
    }

    /**
     * 分页条件查询
     *
     * @param sequenceRole: 流水号规则表
     * @param page:         分页入参
     * @return Page<SequenceRole>
     * @author wgs
     * @since 2024-09-29
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
     * @param sequenceRole: 流水号规则表
     * @param queryWrapper: 查询Wrapper
     * @author wgs
     * @since 2024-09-29
     */
    private void buildPageQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
     * 分页,增强返回参数追加
     *
     * @param sequenceRoleList:
     * @return Page<SequenceRole>
     * @author wgs
     * @since 2024-09-29
     */
    private Page<SequenceRole> assignment(Page<SequenceRole> sequenceRoleList) {
        sequenceRoleList.getRecords().forEach(sequenceRole -> {
        });
        return sequenceRoleList;
    }

    /**
     * 单条条件查询
     *
     * @param id: id
     * @return SequenceRole
     * @author wgs
     * @since 2024-09-29
     */
    @Override
    public SequenceRole getOneEnhanceById(Long id) {
        return assignment(baseMapper.selectById(id));
    }

    /**
     * 根据查询参数查询单条结果
     *
     * @param sequenceRole: 流水号规则表
     * @return SequenceRole
     * @author wgs
     * @since 2024-09-29
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
     * @param sequenceRole: 流水号规则表
     * @param queryWrapper: 查询Wrapper
     * @author wgs
     * @since 2024-09-29
     */
    private void buildOneQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
     * 单条，增强返回参数追加
     *
     * @param sequenceRole:
     * @return SequenceRole
     * @author wgs
     * @since 2024-09-29
     */
    private SequenceRole assignment(SequenceRole sequenceRole) {
        return sequenceRole;
    }

    /**
     * 新增
     *
     * @param sequenceRole: 流水号规则表
     * @return Boolean
     * @author wgs
     * @since 2024-09-29
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long saveEnhance(SequenceRole sequenceRole) {
        boolean result = baseMapper.insert(sequenceRole) > 0;
        return sequenceRole.getId();
    }

    /**
     * 修改
     *
     * @param sequenceRole: 流水号规则表
     * @return Boolean
     * @author wgs
     * @since 2024-09-29
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long updateEnhance(SequenceRole sequenceRole) {
        LambdaUpdateWrapper<SequenceRole> updateWrapper = new LambdaUpdateWrapper<>();
        buildUpdateWrapper(sequenceRole, updateWrapper);
        boolean result = baseMapper.update(sequenceRole, updateWrapper) > 0;
        return sequenceRole.getId();
    }

    /**
     * 构造更新条件
     *
     * @param sequenceRole:  流水号规则表
     * @param updateWrapper: 更新Wrapper
     * @author wgs
     * @since 2024-09-29
     */
    private void buildUpdateWrapper(SequenceRole sequenceRole, LambdaUpdateWrapper<SequenceRole> updateWrapper) {
        // 更新条件以及设值
        updateWrapper.eq(SequenceRole::getId, sequenceRole.getId());
    }

    /**
     * 删除
     *
     * @param id: id
     * @return Boolean
     * @author wgs
     * @since 2024-09-29
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Boolean removeEnhanceById(Long id) {
        return baseMapper.deleteById(id) > 0;
    }
}
