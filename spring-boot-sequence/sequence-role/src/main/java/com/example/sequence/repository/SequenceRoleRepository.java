package com.example.sequence.repository;

import com.example.sequence.entity.SequenceRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 * 流水号规则表 仓储类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
public interface SequenceRoleRepository extends IService<SequenceRole> {


    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRole: 流水号规则表
     * @return List<SequenceRole>
     */
    List<SequenceRole> listEnhance(SequenceRole sequenceRole);


    /**
     * 分页条件查询
     * @author wgs
     * @since 2024-09-29
     * @param page: 分页入参
     * @param sequenceRole: 流水号规则表
     * @return Page<SequenceRole>
     */
    Page<SequenceRole> pageEnhance(Page<SequenceRole> page, SequenceRole sequenceRole);


    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return SequenceRole
     */
    SequenceRole getOneEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRole: 流水号规则表
     * @return SequenceRole
     */
    SequenceRole getOneByParam(SequenceRole sequenceRole);

    /**
     * 新增
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRole: 流水号规则表
     * @return Long
     */
    Long saveEnhance(SequenceRole sequenceRole);


    /**
     * 修改
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRole: 流水号规则表
     * @return Long
     */
    Long updateEnhance(SequenceRole sequenceRole);


    /**
     * 删除
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return Boolean
     */
    Boolean removeEnhanceById(Long id);
}
