package com.example.springbootdrools.model;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

@Data
@Slf4j
@Service
public class TieredPricingService {
    @Autowired
    private KieContainer kieContainer;

    public TieredPricing cost(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("开始时间: {}  ---- 结束时间:{}", LocalDateTimeUtil.format(startTime, DatePattern.NORM_DATETIME_FORMATTER), LocalDateTimeUtil.format(endTime, DatePattern.NORM_DATETIME_FORMATTER));

        // 使用日期格式化器将字符串转换为LocalDate对象
        long lStartTime = startTime.getLong(ChronoField.SECOND_OF_DAY);
        long lEndTime = endTime.getLong(ChronoField.SECOND_OF_DAY);
        TieredPricing tp = new TieredPricing();
        tp.setStartTime(lStartTime);
        tp.setEndTime(lEndTime);
        tp.setDuration(new Long(lEndTime - lStartTime).intValue());


        log.info("入参 TieredPricing:{}", JSONUtil.toJsonStr(tp));

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.addEventListener(new AgendaEventListener() {

            @Override
            public void matchCreated(MatchCreatedEvent event) {

            }

            @Override
            public void matchCancelled(MatchCancelledEvent event) {
                Rule rule = event.getMatch().getRule();
                log.debug("Rule cancelled: " + rule.getName());
            }

            @Override
            public void beforeMatchFired(BeforeMatchFiredEvent event) {

            }

            @Override
            public void afterMatchFired(AfterMatchFiredEvent event) {
                Rule rule = event.getMatch().getRule();
                log.debug("Rule fired: {},fact: {}", rule.getName(), event.getMatch().getObjects().get(0));
            }

            @Override
            public void agendaGroupPopped(AgendaGroupPoppedEvent event) {

            }

            @Override
            public void agendaGroupPushed(AgendaGroupPushedEvent event) {

            }

            @Override
            public void beforeRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {

            }

            @Override
            public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent event) {

            }

            @Override
            public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {

            }

            @Override
            public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent event) {

            }
        });
        kieSession.setGlobal("tp", tp);
        kieSession.insert(tp);
        kieSession.fireAllRules();

        kieSession.dispose();
        return tp;
    }
}
