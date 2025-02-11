package com.example.sequence.dao.mapper;

import com.example.sequence.entity.SequenceRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
 import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 流水号表 Mapper 接口
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Mapper
public interface SequenceRecordMapper extends BaseMapper<SequenceRecord> {

}
