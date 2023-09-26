package com.example.config;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SomeConf {

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    private static final String FLAG = "REQUEST_ID";

    @Bean
    public RestTemplate restTemplate() {

        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> list = new ArrayList<>();

        list.add(((request, body, execution) -> {
            String traceId = MDC.get(FLAG);
            if (StringUtils.isNotEmpty(traceId)) {
                request.getHeaders().add(FLAG, traceId);
            }
            return execution.execute(request, body);
        }));

        restTemplate.setInterceptors(list);

        return restTemplate;
    }
}
