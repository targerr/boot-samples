package ${customizeConfig.convertConfig.packageName};

import ${package.Entity}.${entity};
import ${customizeConfig.dtoConfig.packageName}.${entity}DTO;
import ${customizeConfig.voConfig.packageName}.${entity}VO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * <p>
 * ${table.comment!} 转换类
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if kotlin>
@Mapper
interface ${entity}Convert
<#else>
@Mapper
public interface ${entity}Convert {

    ${entity}Convert INSTANCE = Mappers.getMapper(${entity}Convert.class);

    /**
     * ${entity?uncap_first}DTO转换为${entity}
     *
     * @param ${entity?uncap_first}DTO ${table.comment!}DTO对象
     * @return ${entity}
     */
    ${entity} convert${entity}(${entity}DTO ${entity?uncap_first}DTO);

    /**
     * ${entity}转换为${entity}VO
     *
     * @param ${entity?uncap_first} ${table.comment!}DTO对象
     * @return ${entity}VO
     */
    ${entity}VO convert${entity}Vo(${entity} ${entity?uncap_first});

    /**
     * ${entity?uncap_first}列表转换为${entity}VO列表
     *
     * @param ${entity?uncap_first}List ${table.comment!}列表
     * @return List<${entity}VO>
     */
    List<${entity}VO> convert${entity}VoList(List<${entity}> ${entity?uncap_first}List);

    /**
     * ${table.comment!}分页对象转换为${entity}VO分页对象
     *
     * @param ${entity?uncap_first}Page ${table.comment!}分页对象
     * @return Page<${entity}VO>
     */
    Page<${entity}VO> convert${entity}PageVo(Page<${entity}> ${entity?uncap_first}Page);
}
</#if>
