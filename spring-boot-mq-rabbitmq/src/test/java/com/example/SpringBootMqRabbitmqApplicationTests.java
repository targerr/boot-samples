package com.example;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class SpringBootMqRabbitmqApplicationTests {

    public static void main(String[] args) {
        System.out.println("~~~~~");
        File file = FileUtil.file("/Users/wanggaoshuai/Downloads/锦泰-保险对接.pdf");

        System.out.println(file.getAbsolutePath());
        System.out.println(file.getName());
        System.out.println(file.getTotalSpace());





    }

}
