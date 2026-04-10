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

package com.albedo.java.plugins.datascope.model;

import com.google.common.collect.Sets;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author somewhere
 * @date 2019/2/1
 * 数据权限查询参数对象
 */
@Data
public class DataScope implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 限制范围的字段名称（如：dept_id）
	 * 默认为 dept_id
	 */
	private String scopeName = "dept_id";

	/**
	 * 创建者字段名称（如：created_by）
	 * 默认为 created_by
	 */
	private String creatorName = "created_by";

	/**
	 * 部门ID集合
	 */
	private Set<Long> deptIds = Sets.newLinkedHashSet();

	/**
	 * 是否全部数据权限
	 * 设置为 true 时，不进行数据权限过滤
	 */
	private boolean isAll;

	/**
	 * 是否本人数据权限
	 * 设置为 true 时，只查询当前用户创建的数据
	 */
	private boolean isSelf;

	/**
	 * 当前用户ID
	 */
	private Long userId;

	/**
	 * 表别名（可选，用于复杂查询）
	 */
	private String tableAlias;

	/**
	 * 构造函数
	 */
	public DataScope() {
	}

	/**
	 * 构造函数
	 *
	 * @param scopeName 权限字段名称
	 */
	public DataScope(String scopeName) {
		this.scopeName = scopeName;
	}

	/**
	 * 构造函数
	 *
	 * @param scopeName 权限字段名称
	 * @param deptIds   部门ID集合
	 */
	public DataScope(String scopeName, Set<Long> deptIds) {
		this.scopeName = scopeName;
		this.deptIds = deptIds;
	}

	/**
	 * 创建部门权限
	 *
	 * @param deptIds 部门ID集合
	 * @return DataScope
	 */
	public static DataScope ofDept(Set<Long> deptIds) {
		DataScope dataScope = new DataScope();
		dataScope.setDeptIds(deptIds);
		return dataScope;
	}

	/**
	 * 创建全部数据权限
	 *
	 * @return DataScope
	 */
	public static DataScope ofAll() {
		DataScope dataScope = new DataScope();
		dataScope.setAll(true);
		return dataScope;
	}

	/**
	 * 创建本人数据权限
	 *
	 * @param userId 用户ID
	 * @return DataScope
	 */
	public static DataScope ofSelf(Long userId) {
		DataScope dataScope = new DataScope();
		dataScope.setSelf(true);
		dataScope.setUserId(userId);
		return dataScope;
	}

	/**
	 * 判断是否需要进行数据权限过滤
	 *
	 * @return true-需要过滤，false-不需要过滤
	 */
	public boolean needFilter() {
		return !isAll && (isSelf || (deptIds != null && !deptIds.isEmpty()));
	}

}
