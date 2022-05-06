package com.example.config;

/**
 * @Author: wgs
 * @Date 2022/5/5 17:24
 * @Classname HelloProperties
 * @Description
 */

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 读取配置文件转化bean
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
