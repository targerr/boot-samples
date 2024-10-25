/*
 *  Copyright (c) 2019-2020, somowhere (somewhere0813@gmail.com).
 *  <p>
 *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
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

package com.github.common.log.annotation;


import com.github.common.log.enums.BusinessType;
import com.github.common.log.enums.OperatorType;

import java.lang.annotation.*;

/**
 * @Author: wgs
 * @Date 2024/9/26 16:17
 * @Classname LogOperate
 * @Description 操作日志注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperate {

	/**
	 * 描述
	 *
	 * @return {String}
	 */
	String value();


	/**
	 * 功能
	 */
	BusinessType businessType() default BusinessType.OTHER;


	/**
	 * 操作人类别
	 */
	OperatorType operatorType() default OperatorType.WEB;

	/**
	 * 是否保存请求的参数
	 */
	boolean saveRequestData() default true;
}
