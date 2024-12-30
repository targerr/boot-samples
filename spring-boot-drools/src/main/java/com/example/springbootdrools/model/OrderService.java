package com.example.springbootdrools.model;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OrderService {
    @Resource
    private TieredPricingService tieredPricingService;

    /**
     * 计算停车总金额
     *
     * @param entryTime     入场时间
     * @param departureTime 离场时间
     * @Return 停车总费用
     */
    public Float createOrder(LocalDateTime entryTime, LocalDateTime departureTime) {
        System.out.println("入场时间: " + LocalDateTimeUtil.format(entryTime, DatePattern.NORM_DATETIME_FORMATTER));
        System.out.println("离场时间: " + LocalDateTimeUtil.format(departureTime, DatePattern.NORM_DATETIME_FORMATTER));
        //计算有多少个自然天，如 22-10-01 23:24:10 到 22-10-03 07:11:02 就是三个自然天，需要常见三个TP对象分别计算费用
        long daysBetween = ChronoUnit.DAYS.between(entryTime.toLocalDate(), departureTime.toLocalDate().plusDays(1));

        Float cost = 0f;
        for (int i = 0; i < daysBetween; i++) {
            LocalDate day = entryTime.toLocalDate().plusDays(i);
            //计算每天停车的自然时间
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            if (day.equals(entryTime.toLocalDate())) {
                startTime = entryTime;
            } else {
                startTime = LocalDateTimeUtil.beginOfDay(day);
            }

            if (day.equals(departureTime.toLocalDate())) {
                endTime = departureTime;
            } else {
                endTime = LocalDateTimeUtil.endOfDay(day);
            }

            log.info("计费时间: {}", LocalDateTimeUtil.formatNormal(day));
            log.info("计费开始时间: {}", LocalDateTimeUtil.format(startTime, DatePattern.NORM_DATETIME_FORMATTER));
            log.info("计费结束时间: {}", LocalDateTimeUtil.format(endTime, DatePattern.NORM_DATETIME_FORMATTER));


            TieredPricing tp = tieredPricingService.cost(startTime, endTime);


            log.info("计费时间分割 TieredPricing: {}", JSONUtil.toJsonStr(tp));
            cost += tp.getCost();
            long milliseconds = tp.getDuration();

            final long hour = TimeUnit.SECONDS.toHours(milliseconds);
            log.info(milliseconds + " 秒 等于 " + hour + BetweenFormatter.Level.HOUR.getName());
            log.info(milliseconds + " 秒 等于 " + TimeUnit.SECONDS.toMinutes(milliseconds) + BetweenFormatter.Level.MINUTE.getName());

        }
        return cost;
    }


}
