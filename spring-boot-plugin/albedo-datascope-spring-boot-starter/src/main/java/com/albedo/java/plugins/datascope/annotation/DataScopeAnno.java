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

package com.albedo.java.plugins.datascope.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解
 * <p>
 * 用于标识需要进行数据权限控制的 Mapper 方法或 Service 方法
 * <p>
 * 注意：此注解为可选注解，主要用于代码提示和文档说明
 * 实际的数据权限控制通过在 Mapper 方法参数中传递 DataScope 对象实现
 *
 * @author somewhere
 * @date 2019/2/1
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScopeAnno {

	/**
	 * 权限字段名称
	 * 默认为 dept_id
	 */
	String scopeName() default "dept_id";

	/**
	 * 创建者字段名称
	 * 默认为 created_by
	 */
	String creatorName() default "created_by";

	/**
	 * 表别名（可选）
	 * 用于复杂查询中的表别名
	 */
	String tableAlias() default "";

	/**
	 * 是否启用数据权限
	 * 默认启用
	 */
	boolean enabled() default true;

}
