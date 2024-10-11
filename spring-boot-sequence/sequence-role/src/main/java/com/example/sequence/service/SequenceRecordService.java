package com.example.sequence.service;

import com.example.sequence.entity.SequenceRecord;
import com.example.sequence.api.model.vo.SequenceRecordVO;
import com.example.sequence.api.model.dto.SequenceRecordDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.sequence.enums.SequenceKeyEnum;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 流水号表 服务类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
public interface SequenceRecordService {

    /**
     * 获取序列码
     *
     * @param keyEnum 关键字枚举
     * @param date    日期
     * @return {@link String}
     */
    public String getSequenceCode(SequenceKeyEnum keyEnum, Date date);

    /**
    * 分页条件查询
    * @author wgs
    * @since 2024-09-29
    * @param page: 分页入参
    * @param sequenceRecordDto: 流水号表DTO分页查询对象
    * @return Page<SequenceRecordVO>
    */
    Page<SequenceRecordVO> insidePageEnhance(Page<SequenceRecord> page, SequenceRecordDTO sequenceRecordDto);


    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO查询对象
     * @return List<SequenceRecordVO>
     */
    List<SequenceRecordVO> insideListEnhance(SequenceRecordDTO sequenceRecordDto);


    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return SequenceRecordVO
     */
    SequenceRecordVO insideGetEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO查询对象
     * @return SequenceRecordVO
     */
    SequenceRecordVO insideGetOneEnhanceByParam(SequenceRecordDTO sequenceRecordDto);

    /**
     * 新增
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表新增DTO
     * @return Long
     */
     Long insideSaveEnhance(SequenceRecordDTO sequenceRecordDto);


    /**
     * 修改
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表修改DTO
     * @return Long
     */
     Long insideUpdateEnhance(SequenceRecordDTO sequenceRecordDto);


    /**
     * 删除
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return Boolean
     */
    Boolean insideRemoveEnhanceById(Long id);
}
