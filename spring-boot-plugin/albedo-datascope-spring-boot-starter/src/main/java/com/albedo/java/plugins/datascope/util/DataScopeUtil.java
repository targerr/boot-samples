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

package com.albedo.java.plugins.datascope.util;

import com.albedo.java.plugins.datascope.enums.DataScopeType;
import com.albedo.java.plugins.datascope.model.DataScope;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据权限工具类
 *
 * @author somewhere
 * @date 2019/2/1
 */
public class DataScopeUtil {

	/**
	 * 创建全部数据权限
	 *
	 * @return DataScope
	 */
	public static DataScope createAll() {
		return DataScope.ofAll();
	}

	/**
	 * 创建部门数据权限
	 *
	 * @param deptIds 部门ID集合
	 * @return DataScope
	 */
	public static DataScope createDept(Set<Long> deptIds) {
		return DataScope.ofDept(deptIds);
	}

	/**
	 * 创建部门数据权限
	 *
	 * @param deptIds 部门ID数组
	 * @return DataScope
	 */
	public static DataScope createDept(Long... deptIds) {
		return DataScope.ofDept(Arrays.stream(deptIds).collect(Collectors.toSet()));
	}

	/**
	 * 创建本人数据权限
	 *
	 * @param userId 用户ID
	 * @return DataScope
	 */
	public static DataScope createSelf(Long userId) {
		return DataScope.ofSelf(userId);
	}

	/**
	 * 根据数据权限类型创建 DataScope
	 *
	 * @param dataScopeType 数据权限类型
	 * @param userId        用户ID
	 * @param deptIds       部门ID集合
	 * @return DataScope
	 */
	public static DataScope createByType(DataScopeType dataScopeType, Long userId, Set<Long> deptIds) {
		if (dataScopeType == null) {
			return new DataScope();
		}

		switch (dataScopeType) {
			case ALL:
				return DataScope.ofAll();
			case THIS_LEVEL:
			case THIS_LEVEL_CHILDREN:
			case CUSTOMIZE:
				if (deptIds != null && !deptIds.isEmpty()) {
					return DataScope.ofDept(deptIds);
				}
				return new DataScope();
			case SELF:
				if (userId != null) {
					return DataScope.ofSelf(userId);
				}
				return new DataScope();
			case NONE:
			default:
				return new DataScope();
		}
	}

	/**
	 * 判断是否需要进行数据权限过滤
	 *
	 * @param dataScope DataScope 对象
	 * @return true-需要过滤，false-不需要过滤
	 */
	public static boolean needFilter(DataScope dataScope) {
		return dataScope != null && dataScope.needFilter();
	}

	/**
	 * 创建空的 DataScope（不过滤）
	 *
	 * @return DataScope
	 */
	public static DataScope createEmpty() {
		DataScope dataScope = new DataScope();
		dataScope.setAll(true);
		return dataScope;
	}

}
