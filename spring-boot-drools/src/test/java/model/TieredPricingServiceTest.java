package model;

import com.example.springbootdrools.SpringbootDroolsApplication;
import com.example.springbootdrools.model.TieredPricing;
import com.example.springbootdrools.model.TieredPricingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(classes = SpringbootDroolsApplication.class)
@Slf4j
class TieredPricingServiceTest {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Resource
    private TieredPricingService service;
    @Test
    /*夜间免费时段停车*/
    void case1() {
        TieredPricing tp = service.cost(LocalDateTime.parse("2023-10-10 20:10:01", formatter), LocalDateTime.parse("2023-10-10 21:13:58", formatter));
        log.info("本次计价为：{}",tp);
    }
/*    @Test
    *//*有效时间不足半小时*//*
    void case2() {
        TieredPricing tp = service.cost("2023-10-10 04:10:01", "2023-10-10 07:13:58");
        log.info("本次计价为：{}",tp);
    }

    @Test
        *//*超过50元*//*
    void case3() {
        TieredPricing tp = service.cost("2023-10-10 04:10:01", "2023-10-10 23:13:58");
        log.info("本次计价为：{}",tp);
    }*/


}