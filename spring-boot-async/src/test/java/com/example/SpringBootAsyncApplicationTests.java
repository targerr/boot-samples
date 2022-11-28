package com.example;

import cn.hutool.http.HttpUtil;
import com.example.job.AsyncJob;
import com.example.service.AsyncTaskManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

//这是JUnit的注解，通过这个注解让SpringJUnit4ClassRunner这个类提供Spring测试上下文。
@RunWith(SpringJUnit4ClassRunner.class)
//这是Spring Boot注解，为了进行集成测试，需要通过这个注解加载和配置Spring应用上下
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBootAsyncApplication.class)
@Slf4j
class SpringBootAsyncApplicationTests {

    @Autowired
    private AsyncJob asyncJob;

    @Autowired
    private AsyncTaskManager asyncTaskManager;

    /**
     * 测试异步任务
     */
    @Test
    public void asyncTaskTest() throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        Future<Boolean> asyncTask1 = asyncJob.job1();
        Future<Boolean> asyncTask2 = asyncJob.job2();
        Future<Boolean> asyncTask3 = asyncJob.job3();

        // 获取异步任务的处理结果，异步任务没有处理完成，会一直阻塞，可以设置超时时间，使用 get 的重载方法
        asyncTask1.get();
        asyncTask2.get();
        asyncTask3.get();
        long endTime = System.currentTimeMillis();

        log.info("异步任务，总耗时：{} 毫秒", (endTime - startTime));

    }


    /**
     * 测试同步任务
     */
    @Test
    public void taskTest() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        asyncJob.task1();
        asyncJob.task2();
        asyncJob.task3();
        long endTime = System.currentTimeMillis();

        log.info("同步任务，总耗时：{} 毫秒", (endTime - startTime));

    }


    @Test
    public  void testServiceTask(){
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 100; i++) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    System.out.println(asyncTaskManager.submit());
                }
            });

            threads.add(thread);

        }


        threads.forEach(Thread::start);

        System.out.println("执行完毕");

    }


}
