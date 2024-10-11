package com.example.sequence.convert;

import com.example.sequence.entity.SequenceRole;
import com.example.sequence.api.model.dto.SequenceRoleDTO;
import com.example.sequence.api.model.vo.SequenceRoleVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 流水号规则表 转换类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Mapper
public interface SequenceRoleConvert {

    SequenceRoleConvert INSTANCE = Mappers.getMapper(SequenceRoleConvert.class);

    /**
     * sequenceRoleDTO转换为SequenceRole
     *
     * @param sequenceRoleDTO 流水号规则表DTO对象
     * @return SequenceRole
     */
    SequenceRole convertSequenceRole(SequenceRoleDTO sequenceRoleDTO);

    /**
     * SequenceRole转换为SequenceRoleVO
     *
     * @param sequenceRole 流水号规则表DTO对象
     * @return SequenceRoleVO
     */
    SequenceRoleVO convertSequenceRoleVo(SequenceRole sequenceRole);

    /**
     * sequenceRole列表转换为SequenceRoleVO列表
     *
     * @param sequenceRoleList 流水号规则表列表
     * @return List<SequenceRoleVO>
     */
    List<SequenceRoleVO> convertSequenceRoleVoList(List<SequenceRole> sequenceRoleList);

    /**
     * 流水号规则表分页对象转换为SequenceRoleVO分页对象
     *
     * @param sequenceRolePage 流水号规则表分页对象
     * @return Page<SequenceRoleVO>
     */
    Page<SequenceRoleVO> convertSequenceRolePageVo(Page<SequenceRole> sequenceRolePage);
}
