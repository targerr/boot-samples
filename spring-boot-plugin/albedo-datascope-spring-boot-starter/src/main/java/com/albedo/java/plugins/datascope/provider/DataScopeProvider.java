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

package com.albedo.java.plugins.datascope.provider;

import com.albedo.java.plugins.datascope.model.DataScope;

/**
 * 数据权限提供者接口
 * <p>
 * 用于提供当前用户的数据权限信息，支持任意认证方式（JWT、Session、自定义等）
 * <p>
 * 实现此接口并注册为 Spring Bean，即可在使用 @DataScopeAnno 注解时自动获取数据权限
 *
 * @author somewhere
 * @date 2019/2/1
 */
@FunctionalInterface
public interface DataScopeProvider {

	/**
	 * 获取当前用户的数据权限对象
	 * <p>
	 * 当使用 @DataScopeAnno 注解时，AOP 切面会调用此方法获取数据权限
	 *
	 * @return DataScope 对象，如果返回 null 则不进行数据权限过滤
	 */
	DataScope getDataScope();

}
