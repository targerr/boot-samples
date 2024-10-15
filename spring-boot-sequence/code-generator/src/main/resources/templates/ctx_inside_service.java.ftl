package ${customizeConfig.insideServiceConfig.packageName};

import ${package.Entity}.${entity};
import ${customizeConfig.voConfig.packageName}.${entity}VO;
import ${customizeConfig.dtoConfig.packageName}.${entity}DTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;


/**
 * <p>
 * ${table.comment!} 服务类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
interface ${table.serviceName}
<#else>
public interface ${entity}Service {


    /**
    * 分页条件查询
    * @author ${author}
    * @since ${date}
    * @param page: 分页入参
    * @param ${entity?uncap_first}Dto: ${table.comment!}DTO分页查询对象
    * @return Page<${entity}VO>
    */
    Page<${entity}VO> insidePageEnhance(Page<${entity}> page, ${entity}DTO ${entity?uncap_first}Dto);


    /**
     * 集合条件查询
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO查询对象
     * @return List<${entity}VO>
     */
    List<${entity}VO> insideListEnhance(${entity}DTO ${entity?uncap_first}Dto);


    /**
     * 单条条件查询
     * @author ${author}
     * @since ${date}
     * @param id: id
     * @return ${entity}VO
     */
    ${entity}VO insideGetEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}DTO查询对象
     * @return ${entity}VO
     */
    ${entity}VO insideGetOneEnhanceByParam(${entity}DTO ${entity?uncap_first}Dto);

    /**
     * 新增
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}新增DTO
     * @return Long
     */
     Long insideSaveEnhance(${entity}DTO ${entity?uncap_first}Dto);


    /**
     * 修改
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}Dto: ${table.comment!}修改DTO
     * @return Long
     */
     Long insideUpdateEnhance(${entity}DTO ${entity?uncap_first}Dto);


    /**
     * 删除
     * @author ${author}
     * @since ${date}
     * @param id: id
     * @return Boolean
     */
    Boolean insideRemoveEnhanceById(Long id);
}
</#if>
