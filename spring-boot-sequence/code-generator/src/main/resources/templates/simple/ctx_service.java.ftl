package ${customFileConfig.serviceConfig.packageName};

import ${package.Entity}.${entity};
import ${superServiceClassPackage};
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
interface ${table.serviceName} : ${superServiceClass}<${entity}>
<#else>
public interface ${table.serviceName} extends ${superServiceClass}<${entity}> {


    /**
     * 集合条件查询
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return List<${entity}>
     */
    List<${entity}> listEnhance(${entity} ${entity?uncap_first});


    /**
     * 分页条件查询
     * @author ${author}
     * @since ${date}
     * @param page: 分页入参
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return Page<${entity}>
     */
    Page<${entity}> pageEnhance(Page<${entity}> page, ${entity} ${entity?uncap_first});


    /**
     * 单条条件查询
     * @author ${author}
     * @since ${date}
     * @param id: id
     * @return ${entity}
     */
    ${entity} getOneEnhanceById(Long id);

    /**
     * 根据查询参数查询单条结果
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return ${entity}
     */
    ${entity} getOneByParam(${entity} ${entity?uncap_first});

    /**
     * 总数
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return Long
     */
    Long countEnhance(${entity} ${entity?uncap_first});


    /**
     * 新增
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return Long
     */
    Long saveEnhance(${entity} ${entity?uncap_first});


    /**
     * 修改
     * @author ${author}
     * @since ${date}
     * @param ${entity?uncap_first}: ${table.comment!}
     * @return Long
     */
    Long updateEnhance(${entity} ${entity?uncap_first});


    /**
     * 删除
     * @author ${author}
     * @since ${date}
     * @param id: id
     * @return Boolean
     */
    Boolean removeEnhanceById(Long id);
}
</#if>
