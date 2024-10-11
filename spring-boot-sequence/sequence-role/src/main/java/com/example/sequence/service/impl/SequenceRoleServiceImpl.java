package com.example.sequence.service.impl;

import lombok.RequiredArgsConstructor;
import com.example.sequence.entity.SequenceRole;
import com.example.sequence.convert.SequenceRoleConvert;
import com.example.sequence.api.model.vo.SequenceRoleVO;
import com.example.sequence.api.model.dto.SequenceRoleDTO;
import com.example.sequence.repository.SequenceRoleRepository;
import com.example.sequence.service.SequenceRoleService;
import com.example.common.exception.ArgumentAssert;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * <p>
 * 流水号规则表 服务实现类
 * </p>
 *
 * @author wgs
 * @since 2024-09-29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SequenceRoleServiceImpl implements SequenceRoleService {


    private final SequenceRoleRepository sequenceRoleRepository;

    /**
    * 分页条件查询
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRoleDto: 流水号规则表DTO对象
    * @param page: 分页入参
    * @return Page<SequenceRoleVO>
    */
    @Override
    public Page<SequenceRoleVO> insidePageEnhance(Page<SequenceRole> page, SequenceRoleDTO sequenceRoleDto) {
        queryPageCheck(sequenceRoleDto);
        SequenceRole sequenceRole = convertPageQuerySequenceRole(sequenceRoleDto);
        QueryWrapper<SequenceRole> queryWrapper = new QueryWrapper<>(sequenceRole);
        buildPageQueryWrapper(sequenceRole, queryWrapper);
        Page<SequenceRole> sequenceRolePage = sequenceRoleRepository.page(page, queryWrapper);
        return assignment(convertSequenceRolePageVo(sequenceRolePage));
    }

    /**
     * 分页条件检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     */
    private void queryPageCheck(SequenceRoleDTO sequenceRoleDto) {

    }

    /**
     * 分页查询模型转换
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     * @return SequenceRole
     */
        private SequenceRole convertPageQuerySequenceRole(SequenceRoleDTO sequenceRoleDto){
        return SequenceRoleConvert.INSTANCE.convertSequenceRole(sequenceRoleDto);
    }

    /**
     * 构建分页查询条件
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRole: 流水号规则表
     * @param queryWrapper: 查询Wrapper
     */
    private void buildPageQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
     * 分页,增强返回参数追加
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleVoPage: 分页显示VO
     * @return Page<SequenceRole>
     */
    private Page<SequenceRoleVO> assignment(Page<SequenceRoleVO> sequenceRoleVoPage) {
        sequenceRoleVoPage.getRecords().forEach(sequenceRoleVO -> {
        });
        return sequenceRoleVoPage;
    }

    /**
     * 分页显示模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param page: 分页显示VO
     * @return Page<SequenceRole>
     */
     private Page<SequenceRoleVO> convertSequenceRolePageVo(Page<SequenceRole> page){
        return SequenceRoleConvert.INSTANCE.convertSequenceRolePageVo(page);
    }

    /**
     * 集合条件查询
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     * @return List<SequenceRoleVO>
     */
    @Override
    public List<SequenceRoleVO> insideListEnhance(SequenceRoleDTO sequenceRoleDto) {
        queryListCheck(sequenceRoleDto);
        SequenceRole sequenceRole = convertSequenceRoleListQuery(sequenceRoleDto);
        QueryWrapper<SequenceRole> queryWrapper = new QueryWrapper<>(sequenceRole);
        buildListQueryWrapper(sequenceRole, queryWrapper);
        List<SequenceRole> sequenceRoleList = sequenceRoleRepository.list(queryWrapper);
        List<SequenceRoleVO> sequenceRoleVOList = convertSequenceRoleVoList(sequenceRoleList);
        return assignment(sequenceRoleVOList);
    }

    /**
     * 集合条件检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     */
     private void queryListCheck(SequenceRoleDTO sequenceRoleDto) {

    }

    /**
    * 列表查询模型转换
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRoleDto: 流水号规则表DTO对象
    * @return Page<SequenceRole>
    */
    private SequenceRole convertSequenceRoleListQuery(SequenceRoleDTO sequenceRoleDto){
        return SequenceRoleConvert.INSTANCE.convertSequenceRole(sequenceRoleDto);
    }

    /**
    * 构建列表查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildListQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
     * 列表显示模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleList: 流水号规则表列表
     * @return List<SequenceRoleVO>
     */
     public List<SequenceRoleVO> convertSequenceRoleVoList(List<SequenceRole> sequenceRoleList){
         return SequenceRoleConvert.INSTANCE.convertSequenceRoleVoList(sequenceRoleList);
     }

     /**
      * 集合,增强返回参数追加
      *
      * @author wgs
      * @since 2024-09-29
      * @param sequenceRoleVOList: 流水号规则表VO列表
      * @return List<SequenceRole>
      */
     private List<SequenceRoleVO> assignment(List<SequenceRoleVO> sequenceRoleVOList) {
        sequenceRoleVOList.forEach(sequenceRoleVO -> {
        });
        return  sequenceRoleVOList;
     }

    /**
     * 单条条件查询
     * @author wgs
     * @since 2024-09-29
     * @param id: id
     * @return SequenceRoleVO
     */
    @Override
    public SequenceRoleVO insideGetEnhanceById(Long id) {
        SequenceRole sequenceRole = sequenceRoleRepository.getById(id);
        SequenceRoleVO sequenceRoleVO = convertSequenceRoleVo(sequenceRole);
        return assignment(sequenceRoleVO);
    }

    /**
     * 根据查询参数查询单条结果
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     * @return SequenceRoleVO
    */
    @Override
    public SequenceRoleVO insideGetOneEnhanceByParam(SequenceRoleDTO sequenceRoleDto) {
        queryParamCheck(sequenceRoleDto);
        SequenceRole sequenceRole = convertSequenceRoleParamQuery(sequenceRoleDto);
        QueryWrapper<SequenceRole> queryWrapper = new QueryWrapper<>(sequenceRole);
        buildParamQueryWrapper(sequenceRole, queryWrapper);
        SequenceRole sequenceRoleResult = sequenceRoleRepository.getOne(queryWrapper);
        SequenceRoleVO sequenceRoleVO = convertSequenceRoleVo(sequenceRoleResult);
        return assignment(sequenceRoleVO);
    }

    /**
    * 单条结果参数查询条件检查
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRoleDto: 流水号规则表DTO对象
    */
    private void queryParamCheck(SequenceRoleDTO sequenceRoleDto) {

    }

    /**
    * 构建单条查询条件
    *
    * @author wgs
    * @since 2024-09-29
    * @param sequenceRole: 流水号规则表
    * @param queryWrapper: 查询Wrapper
    */
    private void buildParamQueryWrapper(SequenceRole sequenceRole, QueryWrapper<SequenceRole> queryWrapper) {

    }

    /**
     * 单条查询模型转换
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     * @return SequenceRole
     */
    public SequenceRole convertSequenceRoleParamQuery(SequenceRoleDTO sequenceRoleDto){
        return SequenceRoleConvert.INSTANCE.convertSequenceRole(sequenceRoleDto);
    }

    /**
     * 详情显示模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRole: 流水号规则表
     * @return SequenceRoleVO
     */
     private SequenceRoleVO convertSequenceRoleVo(SequenceRole sequenceRole){
        return SequenceRoleConvert.INSTANCE.convertSequenceRoleVo(sequenceRole);
    }

    /**
     * 单条，增强返回参数追加
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleVO: 流水号规则表VO
     * @return SequenceRole
     */
    private SequenceRoleVO assignment(SequenceRoleVO sequenceRoleVO) {
        return sequenceRoleVO;
    }

    /**
     * 新增
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     * @return Long
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long insideSaveEnhance(SequenceRoleDTO sequenceRoleDto) {
        saveCheck(sequenceRoleDto);
        SequenceRole sequenceRole = convertSequenceRoleSaveModel(sequenceRoleDto);
        boolean result = sequenceRoleRepository.save(sequenceRole);
        ArgumentAssert.isTrue(result,"保存失败");
        return sequenceRole.getId();
    }

    /**
     * 保存检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     */
    private void saveCheck(SequenceRoleDTO sequenceRoleDto) {

    }

    /**
     * 新增模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     * @return SequenceRole
     */
     private SequenceRole convertSequenceRoleSaveModel(SequenceRoleDTO sequenceRoleDto){
        return SequenceRoleConvert.INSTANCE.convertSequenceRole(sequenceRoleDto);
    }

    /**
     * 修改
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表修改DTO
     * @return Long
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long insideUpdateEnhance(SequenceRoleDTO sequenceRoleDto) {
        updateCheck(sequenceRoleDto);
        SequenceRole sequenceRole = convertUpdateSequenceRoleModel(sequenceRoleDto);
        LambdaUpdateWrapper<SequenceRole> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        buildUpdateWrapper(sequenceRole,lambdaUpdateWrapper);
        boolean result = sequenceRoleRepository.update(sequenceRole, lambdaUpdateWrapper);
        ArgumentAssert.isTrue(result,"更新失败");
        return sequenceRole.getId();
    }

    /**
     * 更新检查
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     */
     private void updateCheck(SequenceRoleDTO sequenceRoleDto) {

    }

    /**
     * 构建修改条件
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRole: 流水号规则表
     * @param updateWrapper: 更新Wrapper
     */
    private void buildUpdateWrapper(SequenceRole sequenceRole, LambdaUpdateWrapper<SequenceRole> updateWrapper) {
        // 更新条件以及设值
        updateWrapper.eq(SequenceRole::getId,sequenceRole.getId());
    }

    /**
     * 修改模型转换
     *
     * @author wgs
     * @since 2024-09-29
     * @param sequenceRoleDto: 流水号规则表DTO对象
     * @return SequenceRole
     */
    public SequenceRole convertUpdateSequenceRoleModel(SequenceRoleDTO sequenceRoleDto){
        ArgumentAssert.notNull(sequenceRoleDto.getId(),"id不能为空");
        return SequenceRoleConvert.INSTANCE.convertSequenceRole(sequenceRoleDto);
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
        return sequenceRoleRepository.removeById(id);
    }
}

