package com.example.config;

import com.example.filter.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2022/8/29 09:53
 * @Classname FilterConfig
 * @Description
 */
@Configuration
public class FilterConfig {
    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
