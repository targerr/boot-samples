package ${customFileConfig.serviceImplConfig.packageName};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.phoenix.core.support.ExceptionAssert;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

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
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {


    /**
     * ${table.comment!}
     */
    @Autowired
    private ${entity}Mapper ${entity?uncap_first}Mapper;


    /**
     * 集合条件查询
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return List<${entity}>
     */
    @Override
    public List<${entity}> listEnhance(${entity} ${entity?uncap_first}) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>(${entity?uncap_first});
        buildListQueryWrapper(${entity?uncap_first}, queryWrapper);
        return ${entity?uncap_first}Mapper.selectList(queryWrapper);
    }

    /**
     * 构造list查询条件
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @param queryWrapper: 查询Wrapper
    */
    private void buildListQueryWrapper(${entity} ${entity?uncap_first}, QueryWrapper<${entity}> queryWrapper) {

    }

    /**
     * 分页条件查询
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @param page: 分页入参
     * @return Page<${entity}>
     */
    @Override
    public Page<${entity}> pageEnhance(Page<${entity}> page, ${entity} ${entity?uncap_first}) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>(${entity?uncap_first});
        buildPageQueryWrapper(${entity?uncap_first}, queryWrapper);
        return ${entity?uncap_first}Mapper.selectPage(page, queryWrapper);
    }

    /**
     * 构造分页查询条件
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @param queryWrapper: 查询Wrapper
    */
    private void buildPageQueryWrapper(${entity} ${entity?uncap_first}, QueryWrapper<${entity}> queryWrapper) {

    }

    /**
     * 单条条件查询
     * @author ${author}
     * @since ${date}
     * @param id: id
     * @return ${entity}
     */
    @Override
    public ${entity} getOneEnhanceById(Long id) {
        return assignment(${entity?uncap_first}Mapper.selectById(id));
    }

    /**
     * 根据查询参数查询单条结果
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return ${entity}
     */
    @Override
    public ${entity} getOneByParam(${entity} ${entity?uncap_first}) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>(${entity?uncap_first});
        buildOneQueryWrapper(${entity?uncap_first}, queryWrapper);
        return assignment(${entity?uncap_first}Mapper.selectOne(queryWrapper));
    }

    /**
     * 构造单条查询条件
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @param queryWrapper: 查询Wrapper
    */
    private void buildOneQueryWrapper(${entity} ${entity?uncap_first}, QueryWrapper<${entity}> queryWrapper) {

    }

    /**
     * 总数
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return Long
     */
    @Override
    public Long countEnhance(${entity} ${entity?uncap_first}) {
        QueryWrapper<${entity}> queryWrapper = new QueryWrapper<>(${entity?uncap_first});
        buildCountQueryWrapper(${entity?uncap_first}, queryWrapper);
        return ${entity?uncap_first}Mapper.selectCount(queryWrapper);
    }

    /**
    * 构造单条查询条件
    *
    * @author ${author}
    * @since ${date}
    * @param ${entity?uncap_first}: ${table.comment!}
    * @param queryWrapper: 查询Wrapper
    */
    private void buildCountQueryWrapper(${entity} ${entity?uncap_first}, QueryWrapper<${entity}> queryWrapper) {

    }

    /**
     * 新增
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return Boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long saveEnhance(${entity} ${entity?uncap_first}) {
        boolean result = ${entity?uncap_first}Mapper.insert(${entity?uncap_first}) > 0;
        if(result){
            return ${entity?uncap_first}.getSequenceRecordId();
        }
        ExceptionAssert.argumentError("保存失败");
        return ${entity?uncap_first}.getSequenceRecordId();
    }

    /**
     * 修改
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return Boolean
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 36000, rollbackFor = Exception.class)
    public Long updateEnhance(${entity} ${entity?uncap_first}) {
        LambdaUpdateWrapper<${entity}> updateWrapper = new LambdaUpdateWrapper<>();
        buildUpdateWrapper(${entity?uncap_first}, updateWrapper);
        boolean result = ${entity?uncap_first}Mapper.update(${entity?uncap_first}, updateWrapper) > 0;
        if(result){
            return ${entity?uncap_first}.getSequenceRecordId();
        }
        ExceptionAssert.argumentError("更新失败");
        return ${entity?uncap_first}.getSequenceRecordId();
    }

    /**
     * 构造更新条件
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @param updateWrapper: 更新Wrapper
     */
    private void buildUpdateWrapper(${entity} ${entity?uncap_first}, LambdaUpdateWrapper<${entity}> updateWrapper) {
        // 更新条件以及设值
        updateWrapper.eq(${entity}::getSequenceRecordId,${entity?uncap_first}.getSequenceRecordId());
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
    public Boolean removeEnhanceById(Long id) {
        return ${entity?uncap_first}Mapper.deleteById(id) > 0;
    }

    /**
     * 单条，增强返回参数追加
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}:
     * @return ${entity}
     */
    private ${entity} assignment(${entity} ${entity?uncap_first}) {
        return ${entity?uncap_first};
    }

    /**
     * 分页,增强返回参数追加
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}List:
     * @return Page<${entity}>
     */
    private Page<${entity}> assignment(Page<${entity}> ${entity?uncap_first}List) {
        ${entity?uncap_first}List.getRecords().forEach(${entity?uncap_first} -> {
        });
        return ${entity?uncap_first}List;
    }

    /**
     * 集合,增强返回参数追加
     *
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}List:
     * @return List<${entity}>
     */
    private List<${entity}> assignment(List<${entity}> ${entity?uncap_first}List) {
        ${entity?uncap_first}List.forEach(${entity?uncap_first} -> {
        });
        return ${entity?uncap_first}List;
    }
}
</#if>
