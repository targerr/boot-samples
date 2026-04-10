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

import com.albedo.java.plugins.datascope.provider.DataScopeProvider;
import com.albedo.java.plugins.datascope.model.DataScope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限提供者自动配置
 * <p>
 * 当用户没有提供自定义的 DataScopeProvider Bean 时，
 * 自动创建一个默认实现（返回 null，表示不进行数据权限过滤）
 *
 * @author somewhere
 * @date 2019/2/1
 */
@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = DataScopeProperties.PREFIX, name = "aop-enabled", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
public class DataScopeProviderAutoConfiguration {

	/**
	 * 默认的数据权限提供者
	 * <p>
	 * 只有在用户没有自定义 DataScopeProvider Bean 时才会创建
	 * 返回 null 表示不进行数据权限过滤
	 *
	 * @return 默认的 DataScopeProvider
	 */
	@Bean
	@ConditionalOnMissingBean(DataScopeProvider.class)
	public DataScopeProvider defaultDataScopeProvider() {
		log.info("未检测到自定义 DataScopeProvider Bean，使用默认实现（不进行数据权限过滤）");
		log.info("如需启用数据权限，请实现 DataScopeProvider 接口并注册为 Spring Bean");
		return () -> null;
	}

}
