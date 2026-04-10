/*
 *  Copyright (c) 2019-2023  <a href="https://github.com/somowhere/albedo">Albedo</a>, somewhere (somewhere0813@gmail.com).
 *  <p>
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 * https://www.gnu.org/licenses/lgpl.html
 *  <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.albedo.java.plugins.datascope.config;

import com.albedo.java.plugins.datascope.interceptor.DataScopeInterceptor;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限自动配置类
 *
 * @author somewhere
 * @date 2019/2/1
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(DataScopeProperties.class)
@ConditionalOnClass(MybatisPlusInterceptor.class)
@ConditionalOnProperty(prefix = DataScopeProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class DataScopeAutoConfiguration {

	/**
	 * 配置数据权限拦截器
	 * <p>
	 * 注意：如果项目中已经存在 MybatisPlusInterceptor Bean，
	 * 则不会创建新的 Bean，需要手动将 DataScopeInterceptor 添加到现有的拦截器中
	 *
	 * @param properties 数据权限配置属性
	 * @return MybatisPlusInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor(DataScopeProperties properties) {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

		// 添加数据权限拦截器
		DataScopeInterceptor dataScopeInterceptor = new DataScopeInterceptor();
		interceptor.addInnerInterceptor(dataScopeInterceptor);

		log.info("DataScopeInterceptor 初始化成功");
		log.info("数据权限配置：enabled={}, defaultScopeName={}, defaultCreatorName={}",
			properties.isEnabled(),
			properties.getDefaultScopeName(),
			properties.getDefaultCreatorName());

		return interceptor;
	}

	/**
	 * 配置数据权限拦截器 Bean
	 * <p>
	 * 如果项目中已有 MybatisPlusInterceptor Bean，
	 * 可以直接注入此拦截器并添加到现有的拦截器链中
	 *
	 * @return DataScopeInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean(DataScopeInterceptor.class)
	public DataScopeInterceptor dataScopeInterceptor() {
		log.info("DataScopeInterceptor Bean 创建成功");
		return new DataScopeInterceptor();
	}

}
