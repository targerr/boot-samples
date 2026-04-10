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

package com.albedo.java.plugins.datascope.aspect;

import com.albedo.java.plugins.datascope.annotation.DataScopeAnno;
import com.albedo.java.plugins.datascope.model.DataScope;
import com.albedo.java.plugins.datascope.provider.DataScopeProvider;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * 数据权限切面
 * <p>
 * 用于自动处理带有 @DataScopeAnno 注解的方法
 * 从 Spring 容器中的 DataScopeProvider Bean 获取当前用户的数据权限
 * <p>
 * 支持任意认证方式：JWT、Session、自定义认证等
 * 只需实现 {@link DataScopeProvider} 接口并注册为 Spring Bean 即可
 *
 * @author somewhere
 * @date 2019/2/1
 */
@Slf4j
@Aspect
@Order(1)
public class DataScopeAspect {

	/**
	 * DataScope 上下文 Holder
	 * 用于存储和获取当前线程的数据权限对象
	 */
	private static final ThreadLocal<DataScope> DATA_SCOPE_CONTEXT = new ThreadLocal<>();

	/**
	 * Spring 应用上下文（用于获取 DataScopeProvider Bean）
	 */
	@Autowired(required = false)
	private ApplicationContext applicationContext;

	/**
	 * 数据权限提供者（延迟加载，避免循环依赖）
	 */
	private DataScopeProvider dataScopeProvider;

	/**
	 * 设置当前线程的数据权限对象
	 *
	 * @param dataScope DataScope 对象
	 */
	public static void setDataScope(DataScope dataScope) {
		DATA_SCOPE_CONTEXT.set(dataScope);
	}

	/**
	 * 获取当前线程的数据权限对象
	 *
	 * @return DataScope 对象，如果不存在返回 null
	 */
	public static DataScope getDataScope() {
		return DATA_SCOPE_CONTEXT.get();
	}

	/**
	 * 清除当前线程的数据权限对象
	 */
	public static void clearDataScope() {
		DATA_SCOPE_CONTEXT.remove();
	}

	/**
	 * 环绕通知：处理带有 @DataScopeAnno 注解的方法
	 *
	 * @param joinPoint 连接点
	 * @return 方法执行结果
	 * @throws Throwable 执行异常
	 */
	@Around("@annotation(com.albedo.java.plugins.datascope.annotation.DataScopeAnno)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		DataScopeAnno annotation = method.getAnnotation(DataScopeAnno.class);

		if (annotation == null || !annotation.enabled()) {
			return joinPoint.proceed();
		}

		// 获取当前用户的数据权限
		DataScope dataScope = getCurrentUserDataScope();
		if (dataScope == null) {
			// 如果无法获取数据权限，直接执行方法
			return joinPoint.proceed();
		}

		// 设置注解中的配置
		if (dataScope.getScopeName() == null || "dept_id".equals(dataScope.getScopeName())) {
			dataScope.setScopeName(annotation.scopeName());
		}
		if (dataScope.getCreatorName() == null || "created_by".equals(dataScope.getCreatorName())) {
			dataScope.setCreatorName(annotation.creatorName());
		}

		// 将数据权限设置到上下文中
		setDataScope(dataScope);

		try {
			// 执行方法
			return joinPoint.proceed();
		} finally {
			// 清除上下文
			clearDataScope();
		}
	}

	/**
	 * 获取当前用户的数据权限
	 * <p>
	 * 从 Spring 容器中获取 DataScopeProvider Bean 并调用其 getDataScope() 方法
	 * 支持任意认证方式：JWT、Session、自定义认证等
	 *
	 * @return DataScope 对象，如果无法获取则返回 null
	 */
	private DataScope getCurrentUserDataScope() {
		try {
			// 延迟获取 DataScopeProvider，避免循环依赖
			if (dataScopeProvider == null && applicationContext != null) {
				dataScopeProvider = applicationContext.getBean(DataScopeProvider.class);
			}
			if (dataScopeProvider != null) {
				return dataScopeProvider.getDataScope();
			}
		} catch (Exception e) {
			log.debug("无法从 DataScopeProvider 获取数据权限: {}", e.getMessage());
		}
		return null;
	}

}
