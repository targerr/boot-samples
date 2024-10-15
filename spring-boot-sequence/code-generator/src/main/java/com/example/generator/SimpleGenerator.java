package com.example.generator;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.ibatis.annotations.Mapper;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * 单模块 代码生成
 */
public class SimpleGenerator {

    public static Map<String, String> getModuleMap() {
        Map<String, String> moduleMap = Maps.newHashMap();
        moduleMap.put("1", "enterprise");
        moduleMap.put("2", "margin");
        moduleMap.put("3", "project");
        moduleMap.put("4", "thirdparty");
        moduleMap.put("5", "workflow");
        moduleMap.put("6", "system");
        moduleMap.put("7", "financial");
        moduleMap.put("8", "construction");
        moduleMap.put("9", "sequence");
        return moduleMap;
    }

    public static void main(String[] args) {
        // 包名
        extractCodeGenerator("com.example");


    }

    private static void extractCodeGenerator(String parentPackage) {
        String projectPath = getString();
        AtomicReference<String> moduleName = new AtomicReference<>("");
        // 代码生成器
        FastAutoGenerator.create(new DataSourceConfig
                        .Builder("jdbc:mysql://localhost:3306/jfinal_demo",
                        "root",
                        "root123456"))
                .globalConfig((scanner, builder) -> builder
                        .author(scanner.apply("请输入作者名称？"))
                        .enableSwagger()
                        .dateType(DateType.TIME_PACK)
                        .commentDate(DatePattern.NORM_DATE_PATTERN)).packageConfig((scanner, builder) -> {
                    String apply = scanner.apply("请输入模块序号？" +
                            "1.企业 2.保证金 3.项目 4.外部对接 5.工作流 6.系统管理 7.金融 8.施工 9.编号规则");
                    boolean number = NumberUtil.isNumber(apply);
                    if (!number) {
                        throw new RuntimeException("输入内容不合法");
                    }
                    moduleName.set(getModuleMap().get(apply));
                    String mapperPath = projectPath.concat("/").concat("src/main/resources/mapper");

                    Map<OutputFile, String> outputFileStringMap = Maps.newHashMap();
                    outputFileStringMap.put(OutputFile.xml, mapperPath);
                    builder.parent(parentPackage)
                            .entity(moduleName.get().concat(".entity"))
                            .service("dao.repository")
                            .serviceImpl("dao.repository.impl")
                            .mapper(moduleName.get().concat(".dao.mapper"))
                            .xml("mapper.xml")
                            .controller(moduleName.get().concat(".controller"))
                            .pathInfo(outputFileStringMap).build();
                })
                .strategyConfig(SimpleGenerator::getStrategyConfig)
                .injectionConfig((scanner, builder) -> getInjectionConfig(parentPackage, moduleName, builder))
                .templateEngine(getTemplateEngine())
                .execute();
    }

    @NotNull
    private static String getString() {
        //        String projectPath = System.getProperty("user.dir") + "/";
        String projectPath = System.getProperty("user.dir") + "/spring-boot-sequence/sequence-role/";
        return projectPath;
    }

    private static FreemarkerTemplateEngine getTemplateEngine() {
        return new FreemarkerTemplateEngine() {
            @Override
            protected void outputCustomFile(@org.jetbrains.annotations.NotNull List<CustomFile> customFiles,
                                            @org.jetbrains.annotations.NotNull TableInfo tableInfo,
                                            @org.jetbrains.annotations.NotNull Map<String, Object> objectMap) {
                String entityName = tableInfo.getEntityName();
                String parentPath = getPathInfo(OutputFile.parent);
                customFiles.forEach(file -> {
                    String filePath = StringUtils.isNotBlank(file.getFilePath()) ? file.getFilePath() : parentPath;
                    String fileName = filePath + File.separator + entityName;
                    if (StringUtils.isNotEmpty(file.getFileName())) {
                        fileName = fileName + file.getFileName();
                    }
                    outputFile(new File(fileName), objectMap, file.getTemplatePath(), file.isFileOverride());
                });
            }
        };
    }

    /**
     * 注入配置
     *
     * @param parentPackage 父包名
     * @param moduleName    模块名称
     * @param builder       注入构造
     */
    private static void getInjectionConfig(String parentPackage, AtomicReference<String> moduleName, InjectionConfig.Builder builder) {
        List<CustomFile> customFileList = Lists.newArrayList();
        Map<String, CustomFile> filePathMap = Maps.newHashMap();
        String projectPath = getString();
        String packageName = parentPackage.replaceAll("\\.", "/");

        // 项目路径
//        String projectPatch = "/sequence";
        String projectPatch = "";
        String apiPath = projectPath.concat(projectPatch).concat("src/main/java/").concat(packageName);


        String basePackage = parentPackage.concat(".").concat(moduleName.get());
        String basePackagePath = apiPath.concat("/").concat(moduleName.get());

        System.err.println("basePackage: " + basePackage);
        System.err.println("basePackagePath: " + basePackagePath);

        // dto模板配置
        CustomFile.Builder dtoFileConfigBuilder = new CustomFile.Builder();
        dtoFileConfigBuilder.fileName("DTO.java");
        dtoFileConfigBuilder.packageName(basePackage.concat(".api.model.dto"));
        dtoFileConfigBuilder.filePath(basePackagePath.concat("/api/model/dto/"));
        dtoFileConfigBuilder.templatePath("/templates/simple/ctx_dto.java.ftl");
        customFileList.add(dtoFileConfigBuilder.build());
        filePathMap.put("dtoConfig", dtoFileConfigBuilder.build());


        System.err.println("DTO packageName: " + basePackage.concat(".api.model.dto"));
        System.err.println("DTO filePath: " + basePackagePath.concat("/api/model/dto/"));

        // VO模板配置
        CustomFile.Builder voFileConfigBuilder = new CustomFile.Builder();
        voFileConfigBuilder.fileName("VO.java");
        voFileConfigBuilder.packageName(basePackage.concat(".api.model.vo"));
        voFileConfigBuilder.filePath(basePackagePath.concat("/api/model/vo/"));
        voFileConfigBuilder.templatePath("/templates/simple/ctx_vo.java.ftl");
        customFileList.add(voFileConfigBuilder.build());
        filePathMap.put("voConfig", voFileConfigBuilder.build());

        // convert模板配置
        CustomFile.Builder convertFileConfigBuilder = new CustomFile.Builder();
        convertFileConfigBuilder.fileName("Convert.java");
        convertFileConfigBuilder.packageName(basePackage.concat(".convert"));
        convertFileConfigBuilder.filePath(basePackagePath.concat("/convert/"));
        convertFileConfigBuilder.templatePath("/templates/simple/ctx_convert.java.ftl");
        customFileList.add(convertFileConfigBuilder.build());
        filePathMap.put("convertConfig", convertFileConfigBuilder.build());

        // insideService模板配置
        CustomFile.Builder insideServiceConfigBuilder = new CustomFile.Builder();
        insideServiceConfigBuilder.fileName("Service.java");
        insideServiceConfigBuilder.packageName(basePackage.concat(".service"));
        insideServiceConfigBuilder.filePath(basePackagePath.concat("/service/"));
        insideServiceConfigBuilder.templatePath("/templates/simple/ctx_inside_service.java.ftl");
        customFileList.add(insideServiceConfigBuilder.build());
        filePathMap.put("insideServiceConfig", insideServiceConfigBuilder.build());

        // insideServiceImpl模板配置
        CustomFile.Builder insideServiceImplConfigBuilder = new CustomFile.Builder();
        insideServiceImplConfigBuilder.fileName("ServiceImpl.java");
        insideServiceImplConfigBuilder.packageName(basePackage.concat(".service.impl"));
        insideServiceImplConfigBuilder.filePath(basePackagePath.concat("/service/impl/"));
        insideServiceImplConfigBuilder.templatePath("/templates/simple/ctx_inside_serviceImpl.java.ftl");
        customFileList.add(insideServiceImplConfigBuilder.build());
        filePathMap.put("insideServiceImplConfig", insideServiceImplConfigBuilder.build());

        // entity模板配置
        CustomFile.Builder entityConfigBuilder = new CustomFile.Builder();
        entityConfigBuilder.fileName(".java");
        entityConfigBuilder.packageName(basePackage.concat(".entity"));
        entityConfigBuilder.filePath(basePackagePath.concat("/entity/"));
        entityConfigBuilder.templatePath("/templates/simple/ctx_entity.java.ftl");
        customFileList.add(entityConfigBuilder.build());
        filePathMap.put("entityConfig", entityConfigBuilder.build());

        // mapper模板配置
        CustomFile.Builder mapperConfigBuilder = new CustomFile.Builder();
        mapperConfigBuilder.fileName("Mapper.java");
        mapperConfigBuilder.packageName(basePackage.concat(".dao.mapper"));
        mapperConfigBuilder.filePath(basePackagePath.concat("/dao/mapper/"));
        mapperConfigBuilder.templatePath("/templates/simple/ctx_mapper.java.ftl");
        customFileList.add(mapperConfigBuilder.build());
        filePathMap.put("mapperConfig", mapperConfigBuilder.build());

        // Repository模板配置
        CustomFile.Builder repositoryConfigBuilder = new CustomFile.Builder();
        repositoryConfigBuilder.fileName("Repository.java");
        repositoryConfigBuilder.packageName(basePackage.concat(".repository"));
        repositoryConfigBuilder.filePath(basePackagePath.concat("/repository/"));
        repositoryConfigBuilder.templatePath("/templates/simple/ctx_repository.java.ftl");
        customFileList.add(repositoryConfigBuilder.build());
        filePathMap.put("repositoryConfig", repositoryConfigBuilder.build());

        // RepositoryImpl模板配置
        CustomFile.Builder repositoryImplConfigBuilder = new CustomFile.Builder();
        repositoryImplConfigBuilder.fileName("RepositoryImpl.java");
        repositoryImplConfigBuilder.packageName(basePackage.concat(".repository").concat(".impl"));
        repositoryImplConfigBuilder.filePath(basePackagePath.concat("/repository/").concat("/impl"));
        repositoryImplConfigBuilder.templatePath("/templates/simple/ctx_repositoryImpl.java.ftl");
        customFileList.add(repositoryImplConfigBuilder.build());
        filePathMap.put("repositoryImplConfig", repositoryImplConfigBuilder.build());

        // controller模板配置
        CustomFile.Builder controllerConfigBuilder = new CustomFile.Builder();
        controllerConfigBuilder.fileName("Controller.java");
        controllerConfigBuilder.packageName(basePackage.concat(".controller"));
        controllerConfigBuilder.filePath(basePackagePath.concat("/controller/"));
        controllerConfigBuilder.templatePath("/templates/simple/ctx_controller.java.ftl");
        customFileList.add(controllerConfigBuilder.build());

        filePathMap.put("controllerConfig", controllerConfigBuilder.build());


        builder.beforeOutputFile((tableInfo, objectMap) -> {
                    System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                    objectMap.put("customizeConfig", filePathMap);
                })
                .customFile(customFileList)
                .build();
    }

    /**
     * 策略配置
     *
     * @param scanner 输入
     * @param builder 策略构造
     */
    private static void getStrategyConfig(Function<String, String> scanner, StrategyConfig.Builder builder) {
        List<IFill> propertyList = Lists.newArrayList();
        Property areaCodeProperty = new Property("areaCode", FieldFill.INSERT);
        propertyList.add(areaCodeProperty);
        Property tenantIdProperty = new Property("tenantId", FieldFill.INSERT);
        propertyList.add(tenantIdProperty);
        Property createDateTimeProperty = new Property("createDateTime", FieldFill.INSERT);
        propertyList.add(createDateTimeProperty);
        Property createNameProperty = new Property("createName", FieldFill.INSERT);
        propertyList.add(createNameProperty);
        Property modifyDateTimeProperty = new Property("modifyDateTime", FieldFill.INSERT_UPDATE);
        propertyList.add(modifyDateTimeProperty);
        Property modifyNameProperty = new Property("modifyName", FieldFill.INSERT_UPDATE);
        propertyList.add(modifyNameProperty);
        Property sourceOrgIdProperty = new Property("sourceOrgId", FieldFill.INSERT);
        propertyList.add(sourceOrgIdProperty);
        builder.addInclude(getTables(scanner.apply("请输入表名，多个英文逗号分隔？所有输入 all")))
                .addTablePrefix("sys_", "et_", "gt_", "pt_", "sc_", "wf_", "ins_", "rc_", "cns_", "tr_")
                .entityBuilder()
                .enableLombok()
                .enableChainModel()
                .enableTableFieldAnnotation()
                .logicDeleteColumnName("is_delete")
                .logicDeletePropertyName("isDelete")
                .addTableFills(propertyList)
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .idType(IdType.ASSIGN_ID)
                .controllerBuilder()
                .enableRestStyle()
                .enableHyphenStyle()
                .enableFileOverride()
                .mapperBuilder()
                .mapperAnnotation(Mapper.class)
                .build();
    }

    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
