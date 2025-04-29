package com.example;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan({"com.example.*.*.mapper"})
@Slf4j
public class StartApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(StartApplication.class);
        final ApplicationContext applicationContext = app.run(args);
        Environment env = applicationContext.getEnvironment();
        String applicationName = env.getProperty("spring.application.name");
        int port = Convert.toInt(env.getProperty("server.port"), -1);
        String url = UrlBuilder.create()
                .setHost(NetUtil.getLocalhostStr())
                .setPort(port).build();
        log.info("\n______________________________________________________________\n\t" +
                        "Java Version: {} \n\t" +
                        "Operating System: {} \n\t" +
                        "Application: {} \n\t" +
                        "API接口文档：{}doc.html\n\t" +
                        "Health Check Endpoint：{}actuator/health\n" +
                        "______________________________________________________________",
                SystemUtil.getJavaInfo().getVersion(),
                SystemUtil.getOsInfo().getName(),
                applicationName,
                url,
                url);
    }
}
