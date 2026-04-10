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

import com.albedo.java.plugins.datascope.aspect.DataScopeAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * 数据权限 AOP 自动配置类
 * <p>
 * 需要引入 spring-boot-starter-aop 依赖才会生效
 *
 * @author somewhere
 * @date 2019/2/1
 */
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(name = "org.aspectj.lang.annotation.Aspect")
@ConditionalOnProperty(prefix = DataScopeProperties.PREFIX, name = "aop-enabled", havingValue = "true", matchIfMissing = false)
public class DataScopeAspectAutoConfiguration {

	/**
	 * 配置数据权限切面
	 *
	 * @return DataScopeAspect
	 */
	@Bean
	public DataScopeAspect dataScopeAspect() {
		return new DataScopeAspect();
	}

}
