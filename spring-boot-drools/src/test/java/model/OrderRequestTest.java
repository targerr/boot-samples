package model;

import com.example.springbootdrools.SpringbootDroolsApplication;
import com.example.springbootdrools.model.OrderService;
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

    @Test
    void createOrderCase4() {
        //计费停时14分钟 ，不足半小时0元
        Float cost = orderService.createOrder(LocalDateTime.parse("2024-11-18 10:00:00", formatter),
                LocalDateTime.parse("2024-11-19 10:00:00", formatter));
        System.out.println(cost);
//        Assertions.assertEquals(15f,cost);
    }

    /**
     * 1.20:00至次日7时不收费
     * 2.白天7:00-20:00每小时5元，每半个小时计费一次
     * 3.进场30分钟内不收费，但计入时间
     * 4.每天最高收费50元
     */

}