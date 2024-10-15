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

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

/**
 * 多模块 代码生成
 */
public class MybatisPlusGenerator {

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
//        String projectPath = System.getProperty("user.dir");
//        System.out.println(projectPath);

        String projectPath = System.getProperty("user.dir") + "/sequence";
        System.err.println(projectPath);
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
//                    String mapperPath = projectPath.concat("/").concat("apollo-dataflow-dao").concat("/src/main/resources/mapper");
                    String mapperPath = projectPath.concat("/").concat("src/main/resources/mapper");
                    Map<OutputFile, String> outputFileStringMap = Maps.newHashMap();
                    outputFileStringMap.put(OutputFile.xml, mapperPath);
                    builder.parent(parentPackage)
                            .entity("entity.".concat(moduleName.get()))
                            .service("dao.repository")
                            .serviceImpl("dao.repository.impl")
                            .mapper("dao.mapper.".concat(moduleName.get()))
                            .xml("mapper.xml")
                            .controller("controller.".concat(moduleName.get()))
                            .pathInfo(outputFileStringMap).build();
                })
                .strategyConfig(MybatisPlusGenerator::getStrategyConfig)
                .injectionConfig((scanner, builder) -> getInjectionConfig(parentPackage, moduleName, builder))
                .templateEngine(getTemplateEngine())
                .execute();
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
        String projectPath = System.getProperty("user.dir");
        String packageName = parentPackage.replaceAll("\\.", "/");
        System.out.println("packageName: " + packageName);
//        String apiPath = projectPath.concat("/").concat("apollo-dataflow-api").concat("/src/main/java/").concat(packageName);
        String apiPath = projectPath.concat("/sequence").concat("/src/main/java/").concat(packageName);
//        String daoPath = projectPath.concat("/").concat("apollo-dataflow-dao").concat("/src/main/java/").concat(packageName);
        String daoPath = projectPath.concat("/sequence").concat("/src/main/java/").concat(packageName);
//        String webPath = projectPath.concat("/").concat("apollo-dataflow-web").concat("/src/main/java/").concat(packageName);
        String webPath = projectPath.concat("/sequence").concat("/src/main/java/").concat(packageName);
//        String servicePath = projectPath.concat("/").concat("apollo-dataflow-service").concat("/src/main/java/").concat(packageName);
        String servicePath = projectPath.concat("/sequence").concat("/src/main/java/").concat(packageName);

        // dto模板配置
        CustomFile.Builder dtoFileConfigBuilder = new CustomFile.Builder();
        dtoFileConfigBuilder.fileName("DTO.java");
        dtoFileConfigBuilder.packageName(parentPackage.concat(".api.model.dto.").concat(moduleName.get()));
        dtoFileConfigBuilder.filePath(apiPath.concat("/api/model/dto/").concat(moduleName.get()));
        dtoFileConfigBuilder.templatePath("/templates/ctx_dto.java.ftl");
        customFileList.add(dtoFileConfigBuilder.build());
        filePathMap.put("dtoConfig", dtoFileConfigBuilder.build());

        // VO模板配置
        CustomFile.Builder voFileConfigBuilder = new CustomFile.Builder();
        voFileConfigBuilder.fileName("VO.java");
        voFileConfigBuilder.packageName(parentPackage.concat(".api.model.vo.").concat(moduleName.get()));
        voFileConfigBuilder.filePath(apiPath.concat("/api/model/vo/").concat(moduleName.get()));
        voFileConfigBuilder.templatePath("/templates/ctx_vo.java.ftl");
        customFileList.add(voFileConfigBuilder.build());
        filePathMap.put("voConfig", voFileConfigBuilder.build());

        // convert模板配置
        CustomFile.Builder convertFileConfigBuilder = new CustomFile.Builder();
        convertFileConfigBuilder.fileName("Convert.java");
        convertFileConfigBuilder.packageName(parentPackage.concat(".convert.").concat(moduleName.get()));
        convertFileConfigBuilder.filePath(apiPath.concat("/convert/").concat(moduleName.get()));
        convertFileConfigBuilder.templatePath("/templates/ctx_convert.java.ftl");
        customFileList.add(convertFileConfigBuilder.build());
        filePathMap.put("convertConfig", convertFileConfigBuilder.build());

        // insideService模板配置
        CustomFile.Builder insideServiceConfigBuilder = new CustomFile.Builder();
        insideServiceConfigBuilder.fileName("Service.java");
        insideServiceConfigBuilder.packageName(parentPackage.concat(".service.").concat(moduleName.get()));
        insideServiceConfigBuilder.filePath(apiPath.concat("/service/").concat(moduleName.get()));
        insideServiceConfigBuilder.templatePath("/templates/ctx_inside_service.java.ftl");
        customFileList.add(insideServiceConfigBuilder.build());
        filePathMap.put("insideServiceConfig", insideServiceConfigBuilder.build());

        // insideServiceImpl模板配置
        CustomFile.Builder insideServiceImplConfigBuilder = new CustomFile.Builder();
        insideServiceImplConfigBuilder.fileName("ServiceImpl.java");
        // packageName import com.example.service.construction.DocumentService;
        insideServiceImplConfigBuilder.packageName(parentPackage.concat(".service.impl.").concat(moduleName.get()));
        insideServiceImplConfigBuilder.filePath(servicePath.concat("/service/impl/").concat(moduleName.get()));
        insideServiceImplConfigBuilder.templatePath("/templates/ctx_inside_serviceImpl.java.ftl");
        customFileList.add(insideServiceImplConfigBuilder.build());
        filePathMap.put("insideServiceImplConfig", insideServiceImplConfigBuilder.build());

        // entity模板配置
        CustomFile.Builder entityConfigBuilder = new CustomFile.Builder();
        entityConfigBuilder.fileName(".java");
        entityConfigBuilder.packageName(parentPackage.concat(".entity.").concat(moduleName.get()));
        entityConfigBuilder.filePath(daoPath.concat("/entity/").concat(moduleName.get()));
        entityConfigBuilder.templatePath("/templates/ctx_entity.java.ftl");
        customFileList.add(entityConfigBuilder.build());
        filePathMap.put("entityConfig", entityConfigBuilder.build());

        // mapper模板配置
        CustomFile.Builder mapperConfigBuilder = new CustomFile.Builder();
        mapperConfigBuilder.fileName("Mapper.java");
        mapperConfigBuilder.packageName(parentPackage.concat(".dao.mapper.").concat(moduleName.get()));
        mapperConfigBuilder.filePath(daoPath.concat("/dao/mapper/").concat(moduleName.get()));
        mapperConfigBuilder.templatePath("/templates/ctx_mapper.java.ftl");
        customFileList.add(mapperConfigBuilder.build());
        filePathMap.put("mapperConfig", mapperConfigBuilder.build());

        // Repository模板配置
        CustomFile.Builder repositoryConfigBuilder = new CustomFile.Builder();
        repositoryConfigBuilder.fileName("Repository.java");
        repositoryConfigBuilder.packageName(parentPackage.concat(".repository.").concat(moduleName.get()));
        repositoryConfigBuilder.filePath(daoPath.concat("/repository/").concat(moduleName.get()));
        repositoryConfigBuilder.templatePath("/templates/ctx_repository.java.ftl");
        customFileList.add(repositoryConfigBuilder.build());
        filePathMap.put("repositoryConfig", repositoryConfigBuilder.build());

        // RepositoryImpl模板配置
        CustomFile.Builder repositoryImplConfigBuilder = new CustomFile.Builder();
        repositoryImplConfigBuilder.fileName("RepositoryImpl.java");
        repositoryImplConfigBuilder.packageName(parentPackage.concat(".repository.").concat(moduleName.get()).concat(".impl"));
        repositoryImplConfigBuilder.filePath(daoPath.concat("/repository/").concat(moduleName.get()).concat("/impl"));
        repositoryImplConfigBuilder.templatePath("/templates/ctx_repositoryImpl.java.ftl");
        customFileList.add(repositoryImplConfigBuilder.build());
        filePathMap.put("repositoryImplConfig", repositoryImplConfigBuilder.build());

        // controller模板配置
        CustomFile.Builder controllerConfigBuilder = new CustomFile.Builder();
        controllerConfigBuilder.fileName("Controller.java");
        controllerConfigBuilder.packageName(parentPackage.concat(".controller.").concat(moduleName.get()));
        controllerConfigBuilder.filePath(webPath.concat("/controller/").concat(moduleName.get()));
        controllerConfigBuilder.templatePath("/templates/ctx_controller.java.ftl");
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
