package com.example.datascope.config;

import com.example.datascope.security.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security 配置
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailsService userDetailsService;

	/**
	 * 密码编码器
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * 配置认证管理器
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}

	/**
	 * 暴露 AuthenticationManager 为 Bean
	 * 用于在自定义登录接口中进行认证
	 */
	@Bean
	@Override
	public org.springframework.security.authentication.AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
	 * 配置 HTTP 安全
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// 关闭 CSRF（演示用）
			.csrf().disable()

			// 配置请求授权
			.authorizeRequests()
			// 登录和初始化接口允许匿名访问
			.antMatchers("/api/auth/login", "/api/init/**").permitAll()
			// 管理员接口需要管理员权限
			.antMatchers("/api/admin/**").hasRole("ADMIN")
			// 其他请求需要认证
			.anyRequest().authenticated()
			.and()

			// 启用 HTTP Basic 认证（用于 API 调用）
			.httpBasic()
			.and()

			// 配置表单登录（可选，用于浏览器访问）
			.formLogin()
			.loginPage("/login")
			.loginProcessingUrl("/api/auth/form-login")
			.usernameParameter("username")
			.passwordParameter("password")
			.defaultSuccessUrl("/api/home", true)
			.permitAll()
			.and()

			// 配置登出
			.logout()
			.logoutUrl("/api/auth/logout")
			.logoutSuccessUrl("/")
			.permitAll();

		log.info("Spring Security 配置完成");
	}

}
