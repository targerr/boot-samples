package com.example.sequence.service;

import com.example.sequence.entity.SequenceRole;
import com.example.sequence.api.model.vo.SequenceRoleVO;
import com.example.sequence.api.model.dto.SequenceRoleDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 * 流水号规则表 服务类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
public interface SequenceRoleService {


    /**
    * 分页条件查询
    * @author wgs
    * @since 2024-09-29
    * @param page: 分页入参
    * @param sequenceRoleDto: 流水号规则表DTO分页查询对象
    * @return Page<SequenceRoleVO>
    */
    Page<SequenceRoleVO> insidePageEnhance(Page<SequenceRole> page, SequenceRoleDTO sequenceRoleDto);


    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO查询对象
     * @return List<SequenceRoleVO>
     */
    List<SequenceRoleVO> insideListEnhance(SequenceRoleDTO sequenceRoleDto);


    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return SequenceRoleVO
     */
    SequenceRoleVO insideGetEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO查询对象
     * @return SequenceRoleVO
     */
    SequenceRoleVO insideGetOneEnhanceByParam(SequenceRoleDTO sequenceRoleDto);

    /**
     * 新增
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表新增DTO
     * @return Long
     */
     Long insideSaveEnhance(SequenceRoleDTO sequenceRoleDto);


    /**
     * 修改
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表修改DTO
     * @return Long
     */
     Long insideUpdateEnhance(SequenceRoleDTO sequenceRoleDto);


    /**
     * 删除
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return Boolean
     */
    Boolean insideRemoveEnhanceById(Long id);
}
