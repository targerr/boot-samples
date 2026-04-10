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

package com.albedo.java.plugins.datascope.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

/**
 * <p>
 * 数据权限类型枚举
 * </p>
 *
 * @author somewhere
 * @date 2019-07-21
 */
@Getter
@AllArgsConstructor
public enum DataScopeType {

	/**
	 * 全部数据权限
	 */
	ALL("全部", 5),

	/**
	 * 本级以及子级
	 */
	THIS_LEVEL_CHILDREN("本级以及子级", 2),

	/**
	 * 本级
	 */
	THIS_LEVEL("本级", 3),

	/**
	 * 个人
	 */
	SELF("个人", 4),

	/**
	 * 自定义
	 */
	CUSTOMIZE("自定义", 5),

	/**
	 * 未设置
	 */
	NONE("未设置", 0);

	/**
	 * 描述
	 */
	private final String description;

	/**
	 * 权限值
	 */
	private final int value;


	public static DataScopeType match(String name, DataScopeType defaultType) {
		return Stream.of(values())
			.parallel()
			.filter((item) -> item.name().equalsIgnoreCase(name))
			.findFirst()
			.orElse(defaultType);
	}

	public static DataScopeType get(String name) {
		return match(name, null);
	}

	public boolean eq(DataScopeType type) {
		return type != null && this.name().equalsIgnoreCase(type.name());
	}

}
