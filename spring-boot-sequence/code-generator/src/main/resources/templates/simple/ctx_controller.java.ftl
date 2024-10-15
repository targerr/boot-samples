package ${customizeConfig.controllerConfig.packageName};

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.example.common.model.PageConfig;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.common.model.Result;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import javax.validation.Valid;
import org.springframework.validation.annotation.Validated;
import io.swagger.annotations.Api;
import ${customizeConfig.voConfig.packageName}.${entity}VO;
import ${customizeConfig.dtoConfig.packageName}.${entity}DTO;
import lombok.RequiredArgsConstructor;

<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${customizeConfig.insideServiceConfig.packageName}.${entity}Service;

/**
 * <p>
 * ${table.comment!} 前端控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Slf4j
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@Valid
@RequiredArgsConstructor
@Api(tags = {"${table.comment!}管理"})
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    private final ${entity}Service ${entity?uncap_first}Service;

    @ApiOperation(value = "${table.comment!}分页查询", httpMethod = "POST", response = ${entity}VO.class)
    @ApiImplicitParams({
        @ApiImplicitParam(name = "current", value = "当前页", dataType = "int", example = "1"),
        @ApiImplicitParam(name = "size", value = "分页显示数量", dataType = "int", example = "10"),
    })
    @PostMapping("/page")
    public Result<Page<${entity}VO>> insidePage${entity}(@RequestParam(value="current") Integer current,@RequestParam(value="size") Integer size,
                                                    @RequestBody ${entity}DTO ${entity?uncap_first}Dto) {
        Integer currentPage = PageConfig.currentPagination(current);
        Integer pageSize = PageConfig.pageSize(size);
        return Result.buildOkData(${entity?uncap_first}Service.insidePageEnhance(new Page<>(currentPage, pageSize), ${entity?uncap_first}Dto));
    }

    @ApiOperation(value = "${table.comment!}列表查询", httpMethod = "POST", response = ${entity}VO.class)
    @PostMapping("/list")
    public Result<List<${entity}VO>> insideList${entity}(@RequestBody ${entity}DTO ${entity?uncap_first}Dto) {
        return Result.buildOkData(${entity?uncap_first}Service.insideListEnhance(${entity?uncap_first}Dto));
    }

    @ApiOperation(value = "${table.comment!}查询详情", httpMethod = "GET", response = ${entity}VO.class)
    @GetMapping("/getById")
    public Result<${entity}VO> insideGet${entity}(@RequestParam(value="id") Long id) {
        return Result.buildOkData(${entity?uncap_first}Service.insideGetEnhanceById(id));
    }

    @ApiOperation(value = "条件查询${table.comment!}详情", httpMethod = "GET", response = ${entity}VO.class)
    @GetMapping("/getByParam")
    public Result<${entity}VO> insideGetOneByParam(@Validated ${entity}DTO ${entity?uncap_first}Dto) {
        return Result.buildOkData(${entity?uncap_first}Service.insideGetOneEnhanceByParam(${entity?uncap_first}Dto));
    }

    @ApiOperation(value = "${table.comment!}新增", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/save")
    public Result<Long> insideSave(@RequestBody ${entity}DTO ${entity?uncap_first}Dto) {
        return Result.buildOkData(${entity?uncap_first}Service.insideSaveEnhance(${entity?uncap_first}Dto));
    }

    @ApiOperation(value = "${table.comment!}修改", httpMethod = "POST", response = Boolean.class)
    @PostMapping("/update")
    public Result<Long> insideUpdate(@RequestBody ${entity}DTO ${entity?uncap_first}Dto) {
        return Result.buildOkData(${entity?uncap_first}Service.insideUpdateEnhance(${entity?uncap_first}Dto));
    }

    @ApiOperation(value = "${table.comment!}删除", httpMethod = "DELETE", response = Boolean.class)
    @DeleteMapping("/delete")
    public Result<Boolean> insideRemoveById(@RequestParam(value="id") Long id) {
        return Result.buildOkData(${entity?uncap_first}Service.insideRemoveEnhanceById(id));
    }

}
</#if>
