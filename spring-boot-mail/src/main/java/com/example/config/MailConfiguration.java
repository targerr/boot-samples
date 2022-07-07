package com.example.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.mail.MailAccount;
import com.example.config.properties.MailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/7/6 10:31
 * @Classname MailConfiguration
 * @Description
 */
@Configuration
public class MailConfiguration {
    @Autowired
    private MailProperties mailProperties;

    @Bean
    public MailAccount mailAccount() {
        MailAccount account = new MailAccount();
        account.setHost(mailProperties.getHost());
        account.setPort(mailProperties.getPort());
        account.setAuth(mailProperties.getAuth());
        account.setFrom(mailProperties.getFrom());
        account.setUser(mailProperties.getUser());
        account.setPass(mailProperties.getPass());
        account.setSocketFactoryPort(mailProperties.getPort());
        account.setStarttlsEnable(mailProperties.getStarttlsEnable());
        account.setSslEnable(mailProperties.getSslEnable());
        if (ObjectUtil.isNotNull(mailProperties.getTimeout())) {
            account.setTimeout(mailProperties.getTimeout());
        }
        if (ObjectUtil.isNotNull(mailProperties.getConnectionTimeout())) {
            account.setConnectionTimeout(mailProperties.getConnectionTimeout());
        }
        return account;
    }
}
