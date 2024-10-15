package ${customizeConfig.insideServiceImplConfig.packageName};

import lombok.RequiredArgsConstructor;
import ${package.Entity}.${entity};
import ${customizeConfig.convertConfig.packageName}.${entity}Convert;
import ${customizeConfig.voConfig.packageName}.${entity}VO;
import ${customizeConfig.dtoConfig.packageName}.${entity}DTO;
import ${customizeConfig.repositoryConfig.packageName}.${entity}Repository;
import ${customizeConfig.insideServiceConfig.packageName}.${entity}Service;
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
 * ${table.comment!} 服务实现类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
@Service
@RequiredArgsConstructor
<#if kotlin>
open class ${entity}ServiceImpl implements ${entity}Service {

}
<#else>
public class ${entity}ServiceImpl implements ${entity}Service {


    private final ${entity}Repository ${entity?uncap_first}Repository;

    /**
    * 分页条件查询
    * @author ${author}
    * @since ${date}
    * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
    * @param page: 分页入参
    * @return Page<${entity}VO>
    */
    @Override
    public Page<${entity}VO> insidePageEnhance(Page<${entity}> page, ${entity}DTO ${entity?uncap_first}Dto) {
        queryPageCheck(${entity?uncap_first}Dto);
        ${entity} ${entity?uncap_first} = convertPageQuery${entity}(${entity?uncap_first}Dto);
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>(${entity?uncap_first});
        buildPageQueryWrapper(${entity?uncap_first}, queryWrapper);
        Page<${entity}> ${entity?uncap_first}Page = ${entity?uncap_first}Repository.page(page, queryWrapper);
        return assignment(convert${entity}PageVo(${entity?uncap_first}Page));
    }

    /**
     * 分页条件检查
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     */
    private void queryPageCheck(${entity}DTO ${entity?uncap_first}Dto) {

    }

    /**
     * 分页查询模型转换
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     * @return ${entity}
     */
        private ${entity} convertPageQuery${entity}(${entity}DTO ${entity?uncap_first}Dto){
        return ${entity}Convert.INSTANCE.convert${entity}(${entity?uncap_first}Dto);
    }

    /**
     * 构建分页查询条件
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @param queryWrapper: 查询Wrapper
     */
    private void buildPageQueryWrapper(${entity} ${entity?uncap_first}, QueryWrapper<${entity}> queryWrapper) {

    }

    /**
     * 分页,增强返回参数追加
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}VoPage: 分页显示VO
     * @return Page<${entity}>
     */
    private Page<${entity}VO> assignment(Page<${entity}VO> ${entity?uncap_first}VoPage) {
        ${entity?uncap_first}VoPage.getRecords().forEach(${entity?uncap_first}VO -> {
        });
        return ${entity?uncap_first}VoPage;
    }

    /**
     * 分页显示模型转换
     *
     * @author ${author}
     * @since ${date}
     * @param page: 分页显示VO
     * @return Page<${entity}>
     */
     private Page<${entity}VO> convert${entity}PageVo(Page<${entity}> page){
        return ${entity}Convert.INSTANCE.convert${entity}PageVo(page);
    }

    /**
     * 集合条件查询
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     * @return List<${entity}VO>
     */
    @Override
    public List<${entity}VO> insideListEnhance(${entity}DTO ${entity?uncap_first}Dto) {
        queryListCheck(${entity?uncap_first}Dto);
        ${entity} ${entity?uncap_first} = convert${entity}ListQuery(${entity?uncap_first}Dto);
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>(${entity?uncap_first});
        buildListQueryWrapper(${entity?uncap_first}, queryWrapper);
        List<${entity}> ${entity?uncap_first}List = ${entity?uncap_first}Repository.list(queryWrapper);
        List<${entity}VO> ${entity?uncap_first}VOList = convert${entity}VoList(${entity?uncap_first}List);
        return assignment(${entity?uncap_first}VOList);
    }

    /**
     * 集合条件检查
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     */
     private void queryListCheck(${entity}DTO ${entity?uncap_first}Dto) {

    }

    /**
    * 列表查询模型转换
    *
    * @author ${author}
    * @since ${date}
    * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
    * @return Page<${entity}>
    */
    private ${entity} convert${entity}ListQuery(${entity}DTO ${entity?uncap_first}Dto){
        return ${entity}Convert.INSTANCE.convert${entity}(${entity?uncap_first}Dto);
    }

    /**
    * 构建列表查询条件
    *
    * @author ${author}
    * @since ${date}
    * @param ${entity?uncap_first}: ${table.comment!}
    * @param queryWrapper: 查询Wrapper
    */
    private void buildListQueryWrapper(${entity} ${entity?uncap_first}, QueryWrapper<${entity}> queryWrapper) {

    }

    /**
     * 列表显示模型转换
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}List: ${table.comment!}列表
     * @return List<${entity}VO>
     */
     public List<${entity}VO> convert${entity}VoList(List<${entity}> ${entity?uncap_first}List){
         return ${entity}Convert.INSTANCE.convert${entity}VoList(${entity?uncap_first}List);
     }

     /**
      * 集合,增强返回参数追加
      *
      * @author ${author}
      * @since ${date}
      * @param ${entity?uncap_first}VOList: ${table.comment!}VO列表
      * @return List<${entity}>
      */
     private List<${entity}VO> assignment(List<${entity}VO> ${entity?uncap_first}VOList) {
        ${entity?uncap_first}VOList.forEach(${entity?uncap_first}VO -> {
        });
        return  ${entity?uncap_first}VOList;
     }

    /**
     * 单条条件查询
     * @author ${author}
     * @since ${date}
     * @param id: id
     * @return ${entity}VO
     */
    @Override
    public ${entity}VO insideGetEnhanceById(Long id) {
        ${entity} ${entity?uncap_first} = ${entity?uncap_first}Repository.getById(id);
        ${entity}VO ${entity?uncap_first}VO = convert${entity}Vo(${entity?uncap_first});
        return assignment(${entity?uncap_first}VO);
    }

    /**
     * 根据查询参数查询单条结果
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     * @return ${entity}VO
    */
    @Override
    public ${entity}VO insideGetOneEnhanceByParam(${entity}DTO ${entity?uncap_first}Dto) {
        queryParamCheck(${entity?uncap_first}Dto);
        ${entity} ${entity?uncap_first} = convert${entity}ParamQuery(${entity?uncap_first}Dto);
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>(${entity?uncap_first});
        buildParamQueryWrapper(${entity?uncap_first}, queryWrapper);
        ${entity} ${entity?uncap_first}Result = ${entity?uncap_first}Repository.getOne(queryWrapper);
        ${entity}VO ${entity?uncap_first}VO = convert${entity}Vo(${entity?uncap_first}Result);
        return assignment(${entity?uncap_first}VO);
    }

    /**
    * 单条结果参数查询条件检查
    * @author ${author}
    * @since ${date}
    * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
    */
    private void queryParamCheck(${entity}DTO ${entity?uncap_first}Dto) {

    }

    /**
    * 构建单条查询条件
    *
    * @author ${author}
    * @since ${date}
    * @param ${entity?uncap_first}: ${table.comment!}
    * @param queryWrapper: 查询Wrapper
    */
    private void buildParamQueryWrapper(${entity} ${entity?uncap_first}, QueryWrapper<${entity}> queryWrapper) {

    }

    /**
     * 单条查询模型转换
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     * @return ${entity}
     */
    public ${entity} convert${entity}ParamQuery(${entity}DTO ${entity?uncap_first}Dto){
        return ${entity}Convert.INSTANCE.convert${entity}(${entity?uncap_first}Dto);
    }

    /**
     * 详情显示模型转换
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return ${entity}VO
     */
     private ${entity}VO convert${entity}Vo(${entity} ${entity?uncap_first}){
        return ${entity}Convert.INSTANCE.convert${entity}Vo(${entity?uncap_first});
    }

    /**
     * 单条，增强返回参数追加
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}VO: ${table.comment!}VO
     * @return ${entity}
     */
    private ${entity}VO assignment(${entity}VO ${entity?uncap_first}VO) {
        return ${entity?uncap_first}VO;
    }

    /**
     * 新增
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     * @return Long
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long insideSaveEnhance(${entity}DTO ${entity?uncap_first}Dto) {
        saveCheck(${entity?uncap_first}Dto);
        ${entity} ${entity?uncap_first} = convert${entity}SaveModel(${entity?uncap_first}Dto);
        boolean result = ${entity?uncap_first}Repository.save(${entity?uncap_first});
        ArgumentAssert.isTrue(result,"保存失败");
        return ${entity?uncap_first}.getId();
    }

    /**
     * 保存检查
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     */
    private void saveCheck(${entity}DTO ${entity?uncap_first}Dto) {

    }

    /**
     * 新增模型转换
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     * @return ${entity}
     */
     private ${entity} convert${entity}SaveModel(${entity}DTO ${entity?uncap_first}Dto){
        return ${entity}Convert.INSTANCE.convert${entity}(${entity?uncap_first}Dto);
    }

    /**
     * 修改
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}修改DTO
     * @return Long
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long insideUpdateEnhance(${entity}DTO ${entity?uncap_first}Dto) {
        updateCheck(${entity?uncap_first}Dto);
        ${entity} ${entity?uncap_first} = convertUpdate${entity}Model(${entity?uncap_first}Dto);
        LambdaUpdateWrapper<${entity}> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        buildUpdateWrapper(${entity?uncap_first},lambdaUpdateWrapper);
        boolean result = ${entity?uncap_first}Repository.update(${entity?uncap_first}, lambdaUpdateWrapper);
        ArgumentAssert.isTrue(result,"更新失败");
        return ${entity?uncap_first}.getId();
    }

    /**
     * 更新检查
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     */
     private void updateCheck(${entity}DTO ${entity?uncap_first}Dto) {

    }

    /**
     * 构建修改条件
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @param updateWrapper: 更新Wrapper
     */
    private void buildUpdateWrapper(${entity} ${entity?uncap_first}, LambdaUpdateWrapper<${entity}> updateWrapper) {
        // 更新条件以及设值
        updateWrapper.eq(${entity}::getId,${entity?uncap_first}.getId());
    }

    /**
     * 修改模型转换
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO对象
     * @return ${entity}
     */
    public ${entity} convertUpdate${entity}Model(${entity}DTO ${entity?uncap_first}Dto){
        ArgumentAssert.notNull(${entity?uncap_first}Dto.getId(),"id不能为空");
        return ${entity}Convert.INSTANCE.convert${entity}(${entity?uncap_first}Dto);
    }

    /**
     * 删除
     * @author ${author}
     * @since ${date}
     * @param id: id
     * @return Boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Boolean insideRemoveEnhanceById(Long id) {
        return ${entity?uncap_first}Repository.removeById(id);
    }
}

</#if>
