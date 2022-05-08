package com.example;

import com.sankuai.inf.leaf.plugin.annotation.EnableLeafServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableLeafServer
public class SpringBootIdLeafApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootIdLeafApplication.class, args);
	}

}
