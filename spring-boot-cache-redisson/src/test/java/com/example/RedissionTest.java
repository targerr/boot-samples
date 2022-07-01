package com.example;

import cn.hutool.core.thread.ThreadUtil;
import com.example.redisson.LockUtil;
import com.example.redisson.RedissonLocker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: wgs
 * @Date 2022/5/11 10:55
 * @Classname RedisTest
 * @Description
 */
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootCacheRedissonApplication.class)
public class RedissionTest {

    public static final String KEY = "TESTLOCK_1";

    @Test
    public void redisUtilTest() {
        List<Thread> threadList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(this::test3);
            threadList.add(thread);
        }
        // 启动十个线程
        for (Thread thread : threadList) {
            thread.start();
        }

        // 多线程测试redisson实现分布式锁出现org.redisson.RedissonShutdownException: Redisson is shutdown。
        // 原因：多线程还没跑完，主线程就跑完了。主线程走完，关闭了资源。redisson关闭，多线程操作redisson报错：Redisson is shutdown。
        ThreadUtil.sleep(5000);
    }

    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
            ThreadUtil.sleep(1000);
        }
    }

    // 要锁的方法
    public void test1() {
        try {
            LockUtil.lock(KEY, TimeUnit.SECONDS, 1);
            for (int i = 0; i < 10; i++) {
                System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
                ThreadUtil.sleep(1000);
            }
        } finally {
            LockUtil.unlock(KEY);
        }

    }

    // 要锁的方法
    public void test2() {
        // 尝试加锁
        boolean lock = LockUtil.tryLock(KEY);
        if (!lock) {
            System.out.println("太火爆了。线程 ID：" + Thread.currentThread().getId());
            return;
        }

        for (int i = 0; i < 10; i++) {
            System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
            ThreadUtil.sleep(500);
        }

    }

    // 要锁的方法
    public void test3() {
        // 尝试加锁
        boolean lock = false;
        try {
            lock = LockUtil.tryLock(KEY, 0, 3, TimeUnit.SECONDS);

            if (!lock) {
                System.out.println("太火爆了。线程 ID：" + Thread.currentThread().getId());
                return;
            }

            for (int i = 0; i < 10; i++) {
                System.out.println("执行后续代码。线程 ID：" + Thread.currentThread().getId() + " >> " + i);
                ThreadUtil.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
