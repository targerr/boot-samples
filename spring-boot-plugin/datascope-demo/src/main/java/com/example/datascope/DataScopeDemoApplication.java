package com.example.datascope;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 数据权限示例应用
 */
@SpringBootApplication
@MapperScan("com.example.datascope.mapper")
public class DataScopeDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataScopeDemoApplication.class, args);
		System.out.println("""

			====================================================
			   数据权限示例应用启动成功！
			   访问地址: http://localhost:8080

			   测试账号:
			   - admin / admin123   (管理员，全部数据权限)
			   - manager / mgr123   (部门经理，部门数据权限)
			   - user1 / user123    (普通用户，本人数据权限)
			====================================================
			""");
	}

}
