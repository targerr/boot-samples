package com.example.sequence.convert;

import com.example.sequence.entity.SequenceRecord;
import com.example.sequence.api.model.dto.SequenceRecordDTO;
import com.example.sequence.api.model.vo.SequenceRecordVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * 流水号表 转换类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Mapper
public interface SequenceRecordConvert {

    SequenceRecordConvert INSTANCE = Mappers.getMapper(SequenceRecordConvert.class);

    /**
     * sequenceRecordDTO转换为SequenceRecord
     *
     * @param sequenceRecordDTO 流水号表DTO对象
     * @return SequenceRecord
     */
    SequenceRecord convertSequenceRecord(SequenceRecordDTO sequenceRecordDTO);

    /**
     * SequenceRecord转换为SequenceRecordVO
     *
     * @param sequenceRecord 流水号表DTO对象
     * @return SequenceRecordVO
     */
    SequenceRecordVO convertSequenceRecordVo(SequenceRecord sequenceRecord);

    /**
     * sequenceRecord列表转换为SequenceRecordVO列表
     *
     * @param sequenceRecordList 流水号表列表
     * @return List<SequenceRecordVO>
     */
    List<SequenceRecordVO> convertSequenceRecordVoList(List<SequenceRecord> sequenceRecordList);

    /**
     * 流水号表分页对象转换为SequenceRecordVO分页对象
     *
     * @param sequenceRecordPage 流水号表分页对象
     * @return Page<SequenceRecordVO>
     */
    Page<SequenceRecordVO> convertSequenceRecordPageVo(Page<SequenceRecord> sequenceRecordPage);
}
