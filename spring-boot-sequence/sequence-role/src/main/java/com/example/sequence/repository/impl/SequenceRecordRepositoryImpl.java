package com.example.sequence.repository.impl;

import com.example.sequence.entity.SequenceRecord;
import com.example.sequence.dao.mapper.SequenceRecordMapper;
import com.example.sequence.repository.SequenceRecordRepository;
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
* 流水号表 仓储实现类
* </p>
*
* @author wgs
* @since 2024-09-29
*/
@Slf4j
@Service
public class SequenceRecordRepositoryImpl extends ServiceImpl<SequenceRecordMapper, SequenceRecord> implements SequenceRecordRepository {


    /**
    * 集合条件查询
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @return List<SequenceRecord>
    */
    @Override
    public List<SequenceRecord> listEnhance(SequenceRecord sequenceRecord) {
    QueryWrapper<SequenceRecord> queryWrapper = new QueryWrapper<>(sequenceRecord);
    buildListQueryWrapper(sequenceRecord, queryWrapper);
    return baseMapper.selectList(queryWrapper);
    }

    /**
    * 构造list查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildListQueryWrapper(SequenceRecord sequenceRecord, QueryWrapper<SequenceRecord> queryWrapper) {

    }

    /**
    * 集合,增强返回参数追加
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecordList:
    * @return List<SequenceRecord>
    */
    private List<SequenceRecord> assignment(List<SequenceRecord> sequenceRecordList) {
    sequenceRecordList.forEach(sequenceRecord -> {
    });
    return sequenceRecordList;
    }

    /**
    * 分页条件查询
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @param page: 分页入参
    * @return Page<SequenceRecord>
    */
    @Override
    public Page<SequenceRecord> pageEnhance(Page<SequenceRecord> page, SequenceRecord sequenceRecord) {
    QueryWrapper<SequenceRecord> queryWrapper = new QueryWrapper<>(sequenceRecord);
    buildPageQueryWrapper(sequenceRecord, queryWrapper);
    return baseMapper.selectPage(page, queryWrapper);
    }

    /**
    * 构造分页查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildPageQueryWrapper(SequenceRecord sequenceRecord, QueryWrapper<SequenceRecord> queryWrapper) {

    }

    /**
    * 分页,增强返回参数追加
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecordList:
    * @return Page<SequenceRecord>
    */
    private Page<SequenceRecord> assignment(Page<SequenceRecord> sequenceRecordList) {
    sequenceRecordList.getRecords().forEach(sequenceRecord -> {
    });
    return sequenceRecordList;
    }

    /**
    * 单条条件查询
    * @author wgs
    * @since 2024-09-29
    * @param id: id
    * @return SequenceRecord
    */
    @Override
    public SequenceRecord getOneEnhanceById(Long id) {
    return assignment(baseMapper.selectById(id));
    }

    /**
    * 根据查询参数查询单条结果
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @return SequenceRecord
    */
    @Override
    public SequenceRecord getOneByParam(SequenceRecord sequenceRecord) {
    QueryWrapper<SequenceRecord> queryWrapper = new QueryWrapper<>(sequenceRecord);
    buildOneQueryWrapper(sequenceRecord, queryWrapper);
    return assignment(baseMapper.selectOne(queryWrapper));
    }

    /**
    * 构造单条查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildOneQueryWrapper(SequenceRecord sequenceRecord, QueryWrapper<SequenceRecord> queryWrapper) {

    }

    /**
    * 单条，增强返回参数追加
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord:
    * @return SequenceRecord
    */
    private SequenceRecord assignment(SequenceRecord sequenceRecord) {
    return sequenceRecord;
    }

    /**
    * 新增
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @return Boolean
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long saveEnhance(SequenceRecord sequenceRecord) {
    boolean result = baseMapper.insert(sequenceRecord) > 0;
    ArgumentAssert.isTrue(result,"保存失败");
    return sequenceRecord.getId();
    }

    /**
    * 修改
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @return Boolean
    */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long updateEnhance(SequenceRecord sequenceRecord) {
    LambdaUpdateWrapper<SequenceRecord> updateWrapper = new LambdaUpdateWrapper<>();
    buildUpdateWrapper(sequenceRecord, updateWrapper);
    boolean result = baseMapper.update(sequenceRecord, updateWrapper) > 0;
    ArgumentAssert.isTrue(result,"更新失败");
    return sequenceRecord.getId();
    }

    /**
    * 构造更新条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @param updateWrapper: 更新Wrapper
    */
    private void buildUpdateWrapper(SequenceRecord sequenceRecord, LambdaUpdateWrapper<SequenceRecord> updateWrapper) {
    // 更新条件以及设值
    updateWrapper.eq(SequenceRecord::getId,sequenceRecord.getId());
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
