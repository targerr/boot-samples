package com.github.echo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: wgs
 * @Date 2024/9/21 15:46
 * @Classname HelloProperties
 * @Description 读取配置文件转化bean
 */

@ConfigurationProperties(prefix = "hello")
public class HelloProperties {
    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "HelloProperties{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

}

