package com.example.springword.word;

import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: wgs
 * @Date 2024/4/24 15:26
 * @Classname SpringSensitiveWordConfig
 * @Description
 */
@Configuration
public class SpringSensitiveWordConfig {
    @Autowired
    private MyDdWordAllow myDdWordAllow;

    @Autowired
    private MyDdWordDeny myDdWordDeny;

    /**
     * 初始化引导类
     * @return 初始化引导类
     * @since 1.0.0
     */
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        IWordDeny wordDeny = WordDenys.chains(WordDenys.defaults(), myDdWordDeny);
        IWordAllow wordAllow = WordAllows.chains(WordAllows.defaults(), myDdWordAllow);
        SensitiveWordBs sensitiveWordBs = SensitiveWordBs.newInstance()
                .wordAllow(wordAllow)
                .wordDeny(wordDeny)
                // 各种其他配置
                .init();

        return sensitiveWordBs;
    }

}
