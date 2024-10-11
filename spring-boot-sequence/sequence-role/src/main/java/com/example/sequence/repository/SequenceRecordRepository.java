package com.example.sequence.repository;

import com.example.sequence.entity.SequenceRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 * 流水号表 仓储类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
public interface SequenceRecordRepository extends IService<SequenceRecord> {


    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecord: 流水号表
     * @return List<SequenceRecord>
     */
    List<SequenceRecord> listEnhance(SequenceRecord sequenceRecord);


    /**
     * 分页条件查询
     * @author wgs
     * @since 2024-09-29
     * @param page: 分页入参
     * @param sequenceRecord: 流水号表
     * @return Page<SequenceRecord>
     */
    Page<SequenceRecord> pageEnhance(Page<SequenceRecord> page, SequenceRecord sequenceRecord);


    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return SequenceRecord
     */
    SequenceRecord getOneEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecord: 流水号表
     * @return SequenceRecord
     */
    SequenceRecord getOneByParam(SequenceRecord sequenceRecord);

    /**
     * 新增
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecord: 流水号表
     * @return Long
     */
    Long saveEnhance(SequenceRecord sequenceRecord);


    /**
     * 修改
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecord: 流水号表
     * @return Long
     */
    Long updateEnhance(SequenceRecord sequenceRecord);


    /**
     * 删除
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return Boolean
     */
    Boolean removeEnhanceById(Long id);
}
