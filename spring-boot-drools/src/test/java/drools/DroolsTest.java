package drools;

import cn.hutool.json.JSONUtil;
import com.example.springbootdrools.SpringbootDroolsApplication;
import com.example.springbootdrools.drools.ConditionResponse;
import com.example.springbootdrools.drools.Order;
import com.example.springbootdrools.drools.OrderItem;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.base.RuleNameEqualsAgendaFilter;
import org.drools.core.base.RuleNameStartsWithAgendaFilter;
import org.junit.jupiter.api.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.definition.rule.Rule;
import org.kie.api.event.rule.*;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: wgs
 * @Date 2024/11/18 16:01
 * @Classname DroolsTest
 * @Description https://blog.csdn.net/weixin_47533244/article/details/128237383
 * https://www.cnblogs.com/huan1993/p/16284781.html
 */
@SpringBootTest(classes = SpringbootDroolsApplication.class)
@Slf4j
public class DroolsTest {
    @Autowired
    private KieBase kieBase;

    @Test
    public void droolsTest() {
        int amount = 2200;
        Order order = new Order();
        order.setAmount(amount);
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.insert(order);
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
        System.out.println("录入金额: " + amount);
        System.out.println("订单金额 order.getAmount() ： " + order.getAmount() + ",\n订单积分order.getScore()：" + order.getScore());
    }

    @Test
    public void droolsTest2() {
        // contains
//        System.out.println("------------contains-------------");
//         testContains();

        // not contains
//         System.out.println("------------not contains-------------");
//         notContainsTest();


        //matchesTest();

        Order order = new Order();
        OrderItem orderItem = new OrderItem();
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.insert(order);
        kieSession.insert(orderItem);
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("memberOf-rule6"));
        //关闭会话
        kieSession.dispose();

    }


    @Test
    public void test6() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配


        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("insert-rule7"));
        //关闭会话
        kieSession.dispose();
    }

    @Test
    public void test66() {
        ConditionResponse response = new ConditionResponse();
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配

        kieSession.insert(response);

        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("insert-rule77"));

        System.out.println("insert-rule77");
        System.out.println(JSONUtil.toJsonStr(response));

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
                System.err.println(JSONUtil.toJsonStr(rule));

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
            public void afterRuleFlowGroupActivated(RuleFlowGroupActivatedEvent ruleFlowGroupActivatedEvent) {

            }

            @Override
            public void beforeRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

            }

            @Override
            public void afterRuleFlowGroupDeactivated(RuleFlowGroupDeactivatedEvent ruleFlowGroupDeactivatedEvent) {

            }


        });

        //关闭会话
        kieSession.dispose();
        System.out.println(JSONUtil.toJsonStr(response));
    }



    @Test
    public void test8() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配

        Order order = new Order();
        order.setAmount(100);
        kieSession.insert(order);

        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("insert-rule8"));
        //关闭会话
        kieSession.dispose();
    }


    @Test
    public void test9() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配

        Order order = new Order();
        order.setAmount(100);
        kieSession.insert(order);

        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
    }


    @Test
    public void test10() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        Order order = new Order();
        order.setAmount(100);
        kieSession.insert(order);
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("rule3"));
        //关闭会话
        kieSession.dispose();
    }


    @Test
    public void test11() {
        // 设置时间格式

//        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
//        KieSession kieSession = kieBase.newKieSession();
//        // 只匹配规则名称是已 date_effective_ 开头的规则，忽略其余的规则
//        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("date_effective_"));
//        kieSession.dispose();

        // 设置日期格式，否则可能会报错(Wrong date-effective value: Invalid date input format: [2022-05-18 10:54:26] it should follow: [d-MMM-yyyy]]])
        System.setProperty("drools.dateformat", "yyyy-MM-dd HH:mm:ss");
        KieServices kieServices = KieServices.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();
        KieSession kieSession = kieContainer.newKieSession("rule-attributes-ksession");
        // 只匹配规则名称是已 date_effective_ 开头的规则，忽略其余的规则
        kieSession.fireAllRules(new RuleNameStartsWithAgendaFilter("date_effective_"));
        kieSession.dispose();
    }

    @Test
    public void test12() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        //获取执行焦点
        kieSession.getAgenda().getAgendaGroup("002").setFocus();
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("group-rule2"));

        //关闭会话
        kieSession.dispose();
    }

    @Test
    public void test13() throws InterruptedException {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        //启动规则引擎进行规则匹配，知道调用halt方法才结束引擎
        new Thread(kieSession::fireUntilHalt).start();
        //当前线程睡眠10s
        Thread.sleep(10000L);
        //调用halt
        kieSession.halt();
        //关闭会话
        kieSession.dispose();
    }

    @Test
    public void test14() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.setGlobal("name", "李祥");
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
    }


    @Test
    public void test15() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配


        //添加到工作空间
        kieSession.insert(Order.builder().amount(100).build());
//        kieSession.insert(Order.builder().amount(100).build());

        kieSession.fireAllRules();


        /**
         * //不带参数的进行查询
         * query "query-1"
         *     $order : Order(amount == 100)
         * end
         * //带参数的进行查询
         * query "query-2"(Integer amountParam)
         *     $order : Order(amount == amountParam)
         * end
         */

        QueryResults queryResults = kieSession.getQueryResults("query-1");
        System.out.println("无查询参数：result size is " + queryResults.size());
        for (QueryResultsRow queryResult : queryResults) {
            Order order = (Order) queryResult.get("$order");
            System.out.println("查询出来的结果：" + order);
        }

        System.out.println("------------------------------------------");

//        QueryResults queryResults1 = kieSession.getQueryResults("query-2", 1000);
//        System.out.println("有查询参数：result size is " + queryResults1.size());
//        for (QueryResultsRow queryResult : queryResults1) {
//            Order order = (Order) queryResult.get("$order");
//            System.out.println("查询出来的结果：" + order);
//        }

        //关闭会话
        kieSession.dispose();
    }


    @Test
    public void test16() {
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("function_rule"));
        //关闭会话
        kieSession.dispose();
    }


    @Test
    public void test17() {

        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(OrderItem.builder().name("iphone").amount(8000).build());
        orderItemList.add(OrderItem.builder().name("xiaomi").amount(6500).build());
        Order order = new Order();
        order.setAmount(200);
        order.setOrderItemList(orderItemList);

        System.out.println(JSONUtil.toJsonStr(order));
        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配

        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.insert(order);
        kieSession.insert(orderItemList);
        kieSession.fireAllRules(new RuleNameEqualsAgendaFilter("for_rule"));
        //关闭会话
        kieSession.dispose();
    }

    private void matchesTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setAmount(16000);
        // matches
        // System.out.println("------------matches-------------");
        // orderItem.setName("iphone16pro");

        // not matches
        System.out.println("------------not matches-------------");
        orderItem.setName("xiaomi");
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);


        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.insert(orderItem);
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
    }

    private void notContainsTest() {
        Order order = new Order();
        order.setAmount(200);

        OrderItem orderItem = new OrderItem();
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);

        // 不赋值  not contains
        // order.setOrderItemList(orderItemList);

        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.insert(order);
        kieSession.insert(orderItem);
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
    }

    private void testContains() {
        Order order = new Order();
        order.setAmount(200);

        OrderItem orderItem = new OrderItem();
        orderItem.setName("iphone16pro");
        orderItem.setAmount(16000);
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemList.add(orderItem);
        order.setOrderItemList(orderItemList);

        //创建会话对象，用于和规则交互的引擎
        KieSession kieSession = kieBase.newKieSession();
        //讲数据交给规则引擎，规则迎请会根据提供的数据进行规则匹配
        kieSession.insert(order);
        kieSession.insert(orderItem);
        //激活规则引擎，如果匹配成功则执行
        kieSession.fireAllRules();
        //关闭会话
        kieSession.dispose();
    }
}
