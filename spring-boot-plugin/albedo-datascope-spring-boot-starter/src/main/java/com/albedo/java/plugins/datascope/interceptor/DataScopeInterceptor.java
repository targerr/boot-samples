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

package com.albedo.java.plugins.datascope.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author somewhere
 * @date 2019/2/1
 * <p>
 * MyBatis Plus 数据权限拦截器
 * 通过在 SQL 执行前动态修改 WHERE 条件实现数据权限控制
 */
@Slf4j
public class DataScopeInterceptor implements InnerInterceptor {

	/**
	 * 点号
	 */
	private static final String DOT = ".";

	@Override
	public void beforeQuery(Executor executor, MappedStatement mappedStatement, Object parameter,
							RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
		// 只处理 SELECT 语句
		if (SqlCommandType.SELECT == mappedStatement.getSqlCommandType()
			&& StatementType.CALLABLE != mappedStatement.getStatementType()) {

			// 1. 优先从参数中查找 DataScope 对象
			DataScope dataScope = findDataScopeObject(parameter);

			// 2. 如果参数中没有，尝试从 AOP ThreadLocal 获取
			if (dataScope == null) {
				dataScope = getDataScopeFromAspect();
			}

			// 如果找到 DataScope 对象且需要过滤
			if (dataScope != null && dataScope.needFilter()) {
				try {
					processDataScope(boundSql, dataScope);
				} catch (Exception e) {
					log.error("数据权限 SQL 处理失败", e);
				}
			}
		}
	}

	/**
	 * 处理数据权限过滤
	 *
	 * @param boundSql   BoundSql 对象
	 * @param dataScope DataScope 对象
	 * @throws Exception 解析异常
	 */
	private void processDataScope(BoundSql boundSql, DataScope dataScope) throws Exception {
		String originalSql = boundSql.getSql();
		Select selectStatement = (Select) CCJSqlParserUtil.parse(originalSql);
		SelectBody selectBody = selectStatement.getSelectBody();

		if (selectBody instanceof PlainSelect) {
			PlainSelect plainSelect = (PlainSelect) selectBody;
			Expression expression = buildExpression(plainSelect, dataScope);

			if (expression != null) {
				// 将数据权限条件添加到 WHERE 子句
				Expression where = plainSelect.getWhere();
				if (where == null) {
					plainSelect.setWhere(expression);
				} else {
					AndExpression andExpression = new AndExpression(where, expression);
					plainSelect.setWhere(andExpression);
				}

				// 更新 SQL
				PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
				mpBoundSql.sql(plainSelect.toString());

				if (log.isDebugEnabled()) {
					log.debug("数据权限 SQL 原始: {}", originalSql);
					log.debug("数据权限 SQL 修改后: {}", plainSelect.toString());
				}
			}
		} else {
			log.warn("不支持的 SQL 类型，无法进行数据权限过滤: {}", selectBody.getClass().getSimpleName());
		}
	}

	/**
	 * 构建数据权限过滤表达式
	 *
	 * @param plainSelect PlainSelect 对象
	 * @param dataScope   DataScope 对象
	 * @return Expression 对象
	 */
	private Expression buildExpression(PlainSelect plainSelect, DataScope dataScope) {
		Expression expression = null;

		// 获取表别名
		String aliasName = getAliasName(plainSelect);

		String scopeName = dataScope.getScopeName();
		String creatorName = dataScope.getCreatorName();
		Long userId = dataScope.getUserId();
		Set<Long> deptIds = dataScope.getDeptIds();

		// 部门数据权限
		if (StrUtil.isNotBlank(scopeName) && CollectionUtil.isNotEmpty(deptIds)) {
			ItemsList itemsList = new ExpressionList(
				deptIds.stream()
					.map(LongValue::new)
					.collect(Collectors.toList())
			);
			expression = new InExpression(new Column(aliasName + scopeName), itemsList);
		}
		// 本人数据权限
		else if (StrUtil.isNotBlank(creatorName) && dataScope.isSelf() && userId != null) {
			EqualsTo equalsTo = new EqualsTo();
			equalsTo.setLeftExpression(new Column(aliasName + creatorName));
			equalsTo.setRightExpression(new LongValue(userId));
			expression = equalsTo;
		}

		return expression;
	}

	/**
	 * 获取表别名
	 *
	 * @param plainSelect PlainSelect 对象
	 * @return 表别名（如果有），否则返回空字符串
	 */
	private String getAliasName(PlainSelect plainSelect) {
		Alias alias = plainSelect.getFromItem().getAlias();
		if (alias != null && StrUtil.isNotEmpty(alias.getName())) {
			return alias.getName() + DOT;
		}
		// 支持自定义表别名
		return "";
	}

	/**
	 * 查找参数中是否包括 DataScope 对象
	 *
	 * @param parameterObj 参数列表
	 * @return DataScope 对象，如果不存在返回 null
	 */
	private DataScope findDataScopeObject(Object parameterObj) {
		if (parameterObj instanceof DataScope) {
			return (DataScope) parameterObj;
		} else if (parameterObj instanceof Map) {
			Map<?, ?> parameterMap = (Map<?, ?>) parameterObj;
			for (Object val : parameterMap.values()) {
				if (val instanceof DataScope) {
					return (DataScope) val;
				}
			}
		}
		return null;
	}

	/**
	 * 从 AOP 切面的 ThreadLocal 中获取 DataScope 对象
	 * <p>
	 * 使用反射获取，避免直接依赖 Aspect 类
	 *
	 * @return DataScope 对象，如果不存在返回 null
	 */
	private DataScope getDataScopeFromAspect() {
		try {
			// 使用反射获取 DataScopeAspect 类
			Class<?> aspectClass = Class.forName("com.albedo.java.plugins.datascope.aspect.DataScopeAspect");
			// 调用静态方法 getDataScope()
			java.lang.reflect.Method method = aspectClass.getMethod("getDataScope");
			Object result = method.invoke(null);
			if (result instanceof DataScope) {
				return (DataScope) result;
			}
		} catch (Exception e) {
			log.debug("无法从 AOP 切面获取 DataScope: {}", e.getMessage());
		}
		return null;
	}

}
