package com.example.sequence.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sequence.entity.SequenceRole;
import com.example.sequence.enums.SequenceKeyEnum;
import com.example.sequence.repository.SequenceRoleRepository;
import lombok.RequiredArgsConstructor;
import com.example.sequence.entity.SequenceRecord;
import com.example.sequence.convert.SequenceRecordConvert;
import com.example.sequence.api.model.vo.SequenceRecordVO;
import com.example.sequence.api.model.dto.SequenceRecordDTO;
import com.example.sequence.repository.SequenceRecordRepository;
import com.example.sequence.service.SequenceRecordService;
import com.example.common.exception.ArgumentAssert;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * <p>
 * 流水号表 服务实现类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceRecordServiceImpl implements SequenceRecordService {
    private final SequenceRecordRepository sequenceRecordRepository;
    private final SequenceRoleRepository sequenceRoleRepository;


    private static final String CHAT_A = "65";

    @Override
    public String getSequenceCode(SequenceKeyEnum keyEnum, Date date) {
        return generateSequenceCode(keyEnum, Optional.ofNullable(date).orElse(new Date()));
    }

    /**
    * 分页条件查询
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecordDto: 流水号表DTO对象
    * @param page: 分页入参
    * @return Page<SequenceRecordVO>
    */
    @Override
    public Page<SequenceRecordVO> insidePageEnhance(Page<SequenceRecord> page, SequenceRecordDTO sequenceRecordDto) {
        queryPageCheck(sequenceRecordDto);
        SequenceRecord sequenceRecord = convertPageQuerySequenceRecord(sequenceRecordDto);
        QueryWrapper<SequenceRecord> queryWrapper = new QueryWrapper<>(sequenceRecord);
        buildPageQueryWrapper(sequenceRecord, queryWrapper);
        Page<SequenceRecord> sequenceRecordPage = sequenceRecordRepository.page(page, queryWrapper);
        return assignment(convertSequenceRecordPageVo(sequenceRecordPage));
    }

    /**
     * 分页条件检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     */
    private void queryPageCheck(SequenceRecordDTO sequenceRecordDto) {

    }

    /**
     * 分页查询模型转换
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     * @return SequenceRecord
     */
        private SequenceRecord convertPageQuerySequenceRecord(SequenceRecordDTO sequenceRecordDto){
        return SequenceRecordConvert.INSTANCE.convertSequenceRecord(sequenceRecordDto);
    }

    /**
     * 构建分页查询条件
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
     * @param sequenceRecordVoPage: 分页显示VO
     * @return Page<SequenceRecord>
     */
    private Page<SequenceRecordVO> assignment(Page<SequenceRecordVO> sequenceRecordVoPage) {
        sequenceRecordVoPage.getRecords().forEach(sequenceRecordVO -> {
        });
        return sequenceRecordVoPage;
    }

    /**
     * 分页显示模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param page: 分页显示VO
     * @return Page<SequenceRecord>
     */
     private Page<SequenceRecordVO> convertSequenceRecordPageVo(Page<SequenceRecord> page){
        return SequenceRecordConvert.INSTANCE.convertSequenceRecordPageVo(page);
    }

    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     * @return List<SequenceRecordVO>
     */
    @Override
    public List<SequenceRecordVO> insideListEnhance(SequenceRecordDTO sequenceRecordDto) {
        queryListCheck(sequenceRecordDto);
        SequenceRecord sequenceRecord = convertSequenceRecordListQuery(sequenceRecordDto);
        QueryWrapper<SequenceRecord> queryWrapper = new QueryWrapper<>(sequenceRecord);
        buildListQueryWrapper(sequenceRecord, queryWrapper);
        List<SequenceRecord> sequenceRecordList = sequenceRecordRepository.list(queryWrapper);
        List<SequenceRecordVO> sequenceRecordVOList = convertSequenceRecordVoList(sequenceRecordList);
        return assignment(sequenceRecordVOList);
    }

    /**
     * 集合条件检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     */
     private void queryListCheck(SequenceRecordDTO sequenceRecordDto) {

    }

    /**
    * 列表查询模型转换
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecordDto: 流水号表DTO对象
    * @return Page<SequenceRecord>
    */
    private SequenceRecord convertSequenceRecordListQuery(SequenceRecordDTO sequenceRecordDto){
        return SequenceRecordConvert.INSTANCE.convertSequenceRecord(sequenceRecordDto);
    }

    /**
    * 构建列表查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildListQueryWrapper(SequenceRecord sequenceRecord, QueryWrapper<SequenceRecord> queryWrapper) {

    }

    /**
     * 列表显示模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordList: 流水号表列表
     * @return List<SequenceRecordVO>
     */
     public List<SequenceRecordVO> convertSequenceRecordVoList(List<SequenceRecord> sequenceRecordList){
         return SequenceRecordConvert.INSTANCE.convertSequenceRecordVoList(sequenceRecordList);
     }

     /**
      * 集合,增强返回参数追加
      *
      * @author wgs
      * @since 2024-09-29
      * @param sequenceRecordVOList: 流水号表VO列表
      * @return List<SequenceRecord>
      */
     private List<SequenceRecordVO> assignment(List<SequenceRecordVO> sequenceRecordVOList) {
        sequenceRecordVOList.forEach(sequenceRecordVO -> {
        });
        return  sequenceRecordVOList;
     }

    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return SequenceRecordVO
     */
    @Override
    public SequenceRecordVO insideGetEnhanceById(Long id) {
        SequenceRecord sequenceRecord = sequenceRecordRepository.getById(id);
        SequenceRecordVO sequenceRecordVO = convertSequenceRecordVo(sequenceRecord);
        return assignment(sequenceRecordVO);
    }

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     * @return SequenceRecordVO
    */
    @Override
    public SequenceRecordVO insideGetOneEnhanceByParam(SequenceRecordDTO sequenceRecordDto) {
        queryParamCheck(sequenceRecordDto);
        SequenceRecord sequenceRecord = convertSequenceRecordParamQuery(sequenceRecordDto);
        QueryWrapper<SequenceRecord> queryWrapper = new QueryWrapper<>(sequenceRecord);
        buildParamQueryWrapper(sequenceRecord, queryWrapper);
        SequenceRecord sequenceRecordResult = sequenceRecordRepository.getOne(queryWrapper);
        SequenceRecordVO sequenceRecordVO = convertSequenceRecordVo(sequenceRecordResult);
        return assignment(sequenceRecordVO);
    }

    /**
    * 单条结果参数查询条件检查
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecordDto: 流水号表DTO对象
    */
    private void queryParamCheck(SequenceRecordDTO sequenceRecordDto) {

    }

    /**
    * 构建单条查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRecord: 流水号表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildParamQueryWrapper(SequenceRecord sequenceRecord, QueryWrapper<SequenceRecord> queryWrapper) {

    }

    /**
     * 单条查询模型转换
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     * @return SequenceRecord
     */
    public SequenceRecord convertSequenceRecordParamQuery(SequenceRecordDTO sequenceRecordDto){
        return SequenceRecordConvert.INSTANCE.convertSequenceRecord(sequenceRecordDto);
    }

    /**
     * 详情显示模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecord: 流水号表
     * @return SequenceRecordVO
     */
     private SequenceRecordVO convertSequenceRecordVo(SequenceRecord sequenceRecord){
        return SequenceRecordConvert.INSTANCE.convertSequenceRecordVo(sequenceRecord);
    }

    /**
     * 单条，增强返回参数追加
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordVO: 流水号表VO
     * @return SequenceRecord
     */
    private SequenceRecordVO assignment(SequenceRecordVO sequenceRecordVO) {
        return sequenceRecordVO;
    }

    /**
     * 新增
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     * @return Long
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long insideSaveEnhance(SequenceRecordDTO sequenceRecordDto) {
        saveCheck(sequenceRecordDto);
        SequenceRecord sequenceRecord = convertSequenceRecordSaveModel(sequenceRecordDto);
        boolean result = sequenceRecordRepository.save(sequenceRecord);
        ArgumentAssert.isTrue(result,"保存失败");
        return sequenceRecord.getId();
    }

    /**
     * 保存检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     */
    private void saveCheck(SequenceRecordDTO sequenceRecordDto) {

    }

    /**
     * 新增模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     * @return SequenceRecord
     */
     private SequenceRecord convertSequenceRecordSaveModel(SequenceRecordDTO sequenceRecordDto){
        return SequenceRecordConvert.INSTANCE.convertSequenceRecord(sequenceRecordDto);
    }

    /**
     * 修改
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表修改DTO
     * @return Long
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long insideUpdateEnhance(SequenceRecordDTO sequenceRecordDto) {
        updateCheck(sequenceRecordDto);
        SequenceRecord sequenceRecord = convertUpdateSequenceRecordModel(sequenceRecordDto);
        LambdaUpdateWrapper<SequenceRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        buildUpdateWrapper(sequenceRecord,lambdaUpdateWrapper);
        boolean result = sequenceRecordRepository.update(sequenceRecord, lambdaUpdateWrapper);
        ArgumentAssert.isTrue(result,"更新失败");
        return sequenceRecord.getId();
    }

    /**
     * 更新检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     */
     private void updateCheck(SequenceRecordDTO sequenceRecordDto) {

    }

    /**
     * 构建修改条件
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
     * 修改模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRecordDto: 流水号表DTO对象
     * @return SequenceRecord
     */
    public SequenceRecord convertUpdateSequenceRecordModel(SequenceRecordDTO sequenceRecordDto){
        ArgumentAssert.notNull(sequenceRecordDto.getId(),"id不能为空");
        return SequenceRecordConvert.INSTANCE.convertSequenceRecord(sequenceRecordDto);
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
    public Boolean insideRemoveEnhanceById(Long id) {
        return sequenceRecordRepository.removeById(id);
    }


    @Transactional(rollbackFor = Exception.class)
    public synchronized String generateSequenceCode(SequenceKeyEnum keyEnum, Date date) {
        SequenceRole role = sequenceRoleRepository.getOne(new LambdaQueryWrapper<SequenceRole>()
                .eq(SequenceRole::getSequenceKey, keyEnum.name()));
        ArgumentAssert.notNull(role);

        String formatDate = formatDate(role.getTimeFormat(), date);
        String prefix = role.getSequencePrefix();
        String seqName =  Stream.of(prefix, formatDate)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(""));
        log.info("seqName: {}", seqName);

        if (sequenceRecordRepository.getOne(new LambdaQueryWrapper<SequenceRecord>()
                .eq(SequenceRecord::getSequenceRecordName, seqName)) == null) {
            insertSequence(seqName, role);
        }

        String nextValue = getNextValue(seqName);
        log.info("nextValue: {}", nextValue);

        char letter = (char) Integer.parseInt(nextValue.substring(0, 2));
        String code = nextValue.substring(2, 2 + role.getSequenceLength());

        return String.format("%s%c%s", seqName, letter, code);
    }

    private String formatDate(String timeFormat, Date date) {
        SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(date);
    }

    private String buildSequenceName(String prefix, String time) {
        return Stream.of(prefix, time)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(""));
    }


    private String getNextValue(String seqName) {
        Integer currentValue = getCurrentValue(seqName);
        if (currentValue != null) {
            incrementCurrentValue(seqName);
        }
        return Convert.toStr(currentValue);
    }

    private Integer getCurrentValue(String sequenceRecordName) {
        return Optional.ofNullable(sequenceRecordRepository.getOne(new LambdaQueryWrapper<SequenceRecord>()
                        .eq(SequenceRecord::getSequenceRecordName, sequenceRecordName)
                        .eq(SequenceRecord::getDelFlag, 0)))
                .map(SequenceRecord::getCurrentValue)
                .orElse(null);
    }

    private void incrementCurrentValue(String sequenceRecordName) {
        LambdaUpdateWrapper<SequenceRecord> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SequenceRecord::getSequenceRecordName, sequenceRecordName)
                .eq(SequenceRecord::getDelFlag, 0)
                .setSql("current_value = current_value + sequence_interval");
        sequenceRecordRepository.update(null, updateWrapper);
    }

    private void insertSequence(String seqName, SequenceRole role) {
        SequenceRecord entity = new SequenceRecord();
        entity.setSequenceRecordId(IdUtil.getSnowflakeNextId());
        entity.setSequenceRecordName(seqName);
        String format = "%0" + role.getSequenceLength() + "d";
        String currentValue = CHAT_A + String.format(format, role.getSequenceBegin());
        entity.setCurrentValue(Integer.parseInt(currentValue) - role.getSequenceInterval());
        entity.setSequenceInterval(role.getSequenceInterval());
        sequenceRecordRepository.save(entity);
    }


}

