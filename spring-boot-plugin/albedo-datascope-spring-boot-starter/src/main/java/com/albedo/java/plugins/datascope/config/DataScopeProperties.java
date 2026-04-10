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

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据权限配置属性
 *
 * @author somewhere
 * @date 2019/2/1
 */
@Data
@ConfigurationProperties(prefix = DataScopeProperties.PREFIX)
public class DataScopeProperties {

	/**
	 * 配置前缀
	 */
	public static final String PREFIX = "albedo.datascope";

	/**
	 * 是否启用数据权限
	 * 默认启用
	 */
	private boolean enabled = true;

	/**
	 * 默认权限字段名称
	 * 默认为 dept_id
	 */
	private String defaultScopeName = "dept_id";

	/**
	 * 默认创建者字段名称
	 * 默认为 created_by
	 */
	private String defaultCreatorName = "created_by";

	/**
	 * 是否启用 SQL 日志
	 * 默认关闭
	 */
	private boolean sqlLogEnabled = false;

	/**
	 * 是否忽略未配置表别名的 SQL
	 * 默认不忽略，会尝试处理
	 */
	private boolean ignoreNoAlias = false;

	/**
	 * 拦截器顺序
	 * 数字越小优先级越高
	 * 默认为 -10，确保在分页拦截器之前执行
	 */
	private int interceptorOrder = -10;

	/**
	 * 是否启用 AOP 切面支持
	 * 默认关闭，需要引入 spring-boot-starter-aop 依赖
	 */
	private boolean aopEnabled = false;

}
