### 实战

#### 业务规则

1. 20:00至次日7时不收费
2. 白天7:00-20:00每小时5元，每半个小时计费一次
3. 进场30分钟内不收费，但计入时间
4. 每天最高收费50元

#### pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.17</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.example</groupId>
    <artifactId>springboot-drools</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>springboot-drools</name>
    <description>springboot-drools</description>
    <properties>
        <java.version>1.8</java.version>
        <drools.version>7.59.0.Final</drools.version>
    </properties>
    <dependencies>
        <!--<dependency>
            <groupId>org.kie</groupId>
            <artifactId>kie-spring</artifactId>
            <version>${drools.version}</version>
        </dependency>-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.drools</groupId>
            <artifactId>drools-core</artifactId>
            <version>${drools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.drools</groupId>
            <artifactId>drools-compiler</artifactId>
            <version>${drools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.drools</groupId>
            <artifactId>drools-decisiontables</artifactId>
            <version>${drools.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

#### 配置文件

```yaml
logging:
  level:
    root: info
    com.example.springbootdrools.model: debug
```

#### drl

```plaintext
import com.example.springbootdrools.model.TieredPricing;
global com.example.springbootdrools.model.TieredPricing tp;

dialect "mvel"

rule "从早七点开始计费"
    salience 1000
    when
        $tp:TieredPricing(startTime < 25200)
    then
        Integer dur = tp.endTime - 25200;
        modify($tp){
            startTime = 25200;
            duration = dur
        }
end


rule "计费截止到晚八点"
    salience 1000
    when
        $tp:TieredPricing(endTime > 72000)
    then
        Integer dur = 72000 - tp.startTime;
        modify($tp){
            endTime = 72000;
            duration = dur;
        }
end

rule "超过半小时，每半个小时计费一次"
    activation-group "cost"
    salience 3
    when
        $tp:TieredPricing(duration > 1800 )
    then
        Double c = Math.ceil((tp.endTime - tp.startTime) / 1800f) * 2.5;
        modify($tp){
            cost=c.floatValue();
        }
end

rule "进场30分钟内不收费，但计入时间"
    activation-group "cost"
    salience 4
    when
        $tp:TieredPricing(endTime-startTime <= 1800)
    then
        modify($tp){
            cost=0;
        }
end

rule "晚20时后不计费"
    activation-group "cost"
    salience 5
    when
        $tp:TieredPricing(startTime >= 72000 && endTime <=86400 )
    then
        modify($tp){
            cost=0;
        }
end
rule "早7时前不计费"
    activation-group "cost"
    salience 6
    when
        $tp:TieredPricing(startTime >= 0 && endTime <=25200 )
    then
        modify($tp){
            cost=0;
        }
end

rule "每天最高收费50元"
    activation-group "cost"
    salience 7
    when
        $c:TieredPricing(cost > 50)
    then
        modify($c){
            cost=50
        }
end
```
#### 配置类 
```java
package com.example.springbootdrools;
 
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
@Configuration
public class DroolsConfig {
 
    private static final String RULES_CUSTOMER_RULES_DRL = "rules/parking.drl";
    private static final KieServices kieServices = KieServices.Factory.get();
 
    @Bean
    public KieContainer kieContainer() {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory.newClassPathResource(RULES_CUSTOMER_RULES_DRL));
        KieBuilder kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();
        KieModule kieModule = kb.getKieModule();
        KieContainer kieContainer = kieServices.newKieContainer(kieModule.getReleaseId());
        return kieContainer;
    }
}

```

#### 业务类
```java
package com.example.springbootdrools.model;

import lombok.Data;

@Data
public class TieredPricing {
    private Long startTime; //起始时间
    private Long endTime; //结束时间
    private Integer duration; //停车时长
    private Float cost; //费用
}
```

```java
package com.example.springbootdrools.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;

@Data
@Slf4j
@Service
public class TieredPricingService {
    @Autowired
    private KieContainer kieContainer;
    public TieredPricing cost(LocalDateTime startTime,LocalDateTime endTime) {
        log.info("{}-{}",startTime,endTime);
// 要转换的日期字符串
        // 创建日期格式化器，以匹配输入的日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 使用日期格式化器将字符串转换为LocalDate对象
        long lStartTime = startTime.getLong(ChronoField.SECOND_OF_DAY);
        long lEndTime = endTime.getLong(ChronoField.SECOND_OF_DAY);
        TieredPricing tp = new TieredPricing();
        tp.setStartTime(lStartTime);
        tp.setEndTime(lEndTime);
        tp.setDuration(new Long(lEndTime-lStartTime).intValue());
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
                log.debug("Rule fired: {},fact: {}" , rule.getName(),event.getMatch().getObjects().get(0));
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
```

#### 服务类
```java

package com.example.springbootdrools.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class OrderService {
    @Resource
    private TieredPricingService tieredPricingService;

    /**
     * 计算停车总金额
     * @param entryTime 入场时间
     * @param departureTime 离场时间
     * @Return 停车总费用
     */
    public Float createOrder(LocalDateTime entryTime,LocalDateTime departureTime){
        //计算有多少个自然天，如 22-10-01 23:24:10 到 22-10-03 07:11:02 就是三个自然天，需要常见三个TP对象分别计算费用
        long daysBetween = ChronoUnit.DAYS.between(entryTime.toLocalDate(), departureTime.toLocalDate().plusDays(1));
        Float cost = 0f;
        for(int i = 0 ; i < daysBetween ; i++){
            LocalDate day = entryTime.toLocalDate().plusDays(i);
            //计算每天停车的自然时间
            LocalDateTime startTime = null;
            LocalDateTime endTime = null;
            if(day.equals(entryTime.toLocalDate())){
                startTime = entryTime;
            }else{
                startTime = day.atTime(LocalTime.of(0, 0, 0));
            }
            
            if(day.equals(departureTime.toLocalDate())){
                endTime = departureTime;
            }else{
                endTime = day.atTime(LocalTime.of(23, 59, 59));
            }
            TieredPricing tp = tieredPricingService.cost(startTime, endTime);
            log.info("{}",tp);
            cost += tp.getCost();
        }
        return cost;
    }
}
```

### 测试
```java
package com.example.springbootdrools.model;

import com.example.springbootdrools.SpringbootDroolsApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest(classes = SpringbootDroolsApplication.class)
class OrderRequestTest {
    @Resource
    private OrderService orderService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Test
    void createOrderCase1() {
        //第一天计费停时50分钟 5元
        //第二天停全天：50元
        //第三天停全天：50元
        //第四天计费停时74分钟：7.5元
        //合计：112.5元
        Float cost = orderService.createOrder(LocalDateTime.parse("2023-10-10 19:10:01", formatter),
                LocalDateTime.parse("2023-10-13 08:13:58", formatter));
        Assertions.assertEquals(112.5f,cost);
    }

    @Test
    void createOrderCase2() {
        //计费停时8小时3分钟 37.5元
        Float cost = orderService.createOrder(LocalDateTime.parse("2023-10-10 04:10:01", formatter),
                LocalDateTime.parse("2023-10-10 15:13:58", formatter));
        Assertions.assertEquals(42.5f,cost);
    }

    @Test
    void createOrderCase3() {
        //计费停时14分钟 ，不足半小时0元
        Float cost = orderService.createOrder(LocalDateTime.parse("2023-10-10 15:00:00", formatter),
                LocalDateTime.parse("2023-10-10 15:13:58", formatter));
        Assertions.assertEquals(0f,cost);
    }

}
```