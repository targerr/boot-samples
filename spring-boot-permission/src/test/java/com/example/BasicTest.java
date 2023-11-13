package com.example;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author: wgs
 * @Date 2023/10/27
 * @Classname BasicTest
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = SpringBootPermission.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class BasicTest {
}
