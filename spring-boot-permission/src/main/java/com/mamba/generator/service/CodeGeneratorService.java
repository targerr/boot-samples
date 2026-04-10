package com.mamba.generator.service;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.mamba.generator.vo.GeneratorConfigVO;
import com.mamba.generator.vo.TableInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成器服务
 */
@Slf4j
@Service
public class CodeGeneratorService {

    @Autowired
    private DataSource dataSource;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 查询所有表信息
     */
    public List<TableInfoVO> listTables() {
        List<TableInfoVO> tables = new ArrayList<>();
        String sql = "SELECT TABLE_NAME, TABLE_COMMENT, ENGINE, CREATE_TIME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = DATABASE() ORDER BY TABLE_NAME";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TableInfoVO vo = new TableInfoVO();
                vo.setTableName(rs.getString("TABLE_NAME"));
                vo.setTableComment(rs.getString("TABLE_COMMENT"));
                vo.setEngine(rs.getString("ENGINE"));
                vo.setCreateTime(rs.getTimestamp("CREATE_TIME").toString());
                tables.add(vo);
            }
        } catch (SQLException e) {
            log.error("查询表信息失败", e);
        }
        return tables;
    }

    /**
     * 执行代码生成
     */
    public void generate(GeneratorConfigVO config) {
        String projectPath = System.getProperty("user.dir");
        String outputDir = projectPath + "/src/main/java";
        String xmlOutputDir = projectPath + "/src/main/resources/mapper";

        // 使用配置文件中的数据库信息创建代码生成器
        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> builder
                        .author(config.getAuthor())
                        .outputDir(outputDir)
                        .dateType(DateType.TIME_PACK)
                        .disableOpenDir()
                )
                .packageConfig(builder -> {
                    builder.parent("com.mamba")
                            .moduleName(config.getModuleName())
                            .entity("entity")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputDir));
                })
                .strategyConfig(builder -> builder
                        .addInclude(config.getTableName())
                        .addTablePrefix(config.getTablePrefix())
                        .entityBuilder()
                            .enableLombok()
                            .enableChainModel()
                            .logicDeleteColumnName("status")
//                            .naming(cn.hutool.core.util.NamingCase.toCamelCase)
                        .mapperBuilder()
                            .enableMapperAnnotation()
                        .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                        .controllerBuilder()
                            .enableRestStyle()
                            .formatFileName("%sController")
                )
                .templateConfig(builder -> {
                    // 使用自定义模板（如果存在），否则使用默认
//                    builder.disable(TemplateConfig.ENTITY)
//                            .disable(TemplateConfig.CONTROLLER)
//                            .disable(TemplateConfig.SERVICE)
//                            .disable(TemplateConfig.SERVICE_IMPL)
//                            .disable(TemplateConfig.MAPPER)
//                            .disable(TemplateConfig.XML);
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();

        log.info("代码生成完成: table={}", config.getTableName());
    }
}
