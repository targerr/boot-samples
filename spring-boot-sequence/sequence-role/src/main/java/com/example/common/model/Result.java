
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

package com.example.common.model;


import com.example.common.exception.code.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author somewhere
 */
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class Result<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 调用是否成功标识，1：成功，0:失败，此时请开发者稍候再试 详情见[ExceptionCode]
	 */
	@Getter
	@Setter
	private int code = 1;

	@Getter
	@Setter
	private T data;
	/**
	 * 提示消息
	 * 结果消息，如果调用成功，消息通常为空T
	 */
	@Getter
	@Setter
	private String message;

	/**
	 * 请求路径
	 */
	@Getter
	@Setter
	private String path;
	/**
	 * 附加数据
	 */
	@Getter
	@Setter
	private Map<Object, Object> extra;

	/**
	 * 响应时间
	 */
	@Getter
	private long timestamp = System.currentTimeMillis();

	/**
	 * 异常消息
	 * 系统报错时，抛出的原生信息
	 */
	@Getter
	@Setter
	private String errorMsg = "";

	public Result() {
		super();
	}

	public Result(T data) {
		super();
		this.data = data;
	}

	public Result(String message) {
		super();
		this.message = message;
	}

	public Result(T data, String message) {
		super();
		this.data = data;
		this.message = message;
	}

	public Result(T data, int code, String message) {
		super();
		this.data = data;
		this.code = code;
		this.message = message;
	}

	public Result(T data, int code, String message, String errorMsg) {
		super();
		this.data = data;
		this.code = code;
		this.message = message;
		this.errorMsg = errorMsg;
	}


	public Result(Throwable e) {
		super();
		setMessage(e.getMessage());
		this.code = ResponseCode.FAIL.getCode();
	}

	public static Result<?> buildOk(String... messages) {
		return new Result<>(toAppendStr(messages));
	}

	public static Result<Boolean> buildByFlag(boolean flag) {
		return new Result<>(flag, flag ? 1 : ResponseCode.FAIL.getCode(), flag ? "操作成功" : "系统繁忙，请稍候再试");
	}

	public static <T> Result<T> buildOkData(T data, String message) {
		return new Result<>(data, message);
	}

	public static <T> Result<T> buildOkData(T data) {
		return new Result<>(data);
	}

	public static <T> Result<T> buildFailData(T data, String message) {
		return new Result<>(data, ResponseCode.FAIL.getCode(), message);
	}

	public static Result<?> buildFail(String message) {
		return new Result<>(null, ResponseCode.FAIL.getCode(), message);
	}

	public static <T> Result<T> build(T data, int code, String message) {
		return new Result<>(data, code, message);
	}

	public static <T> Result<T> build(int code, String message) {
		return new Result<>(null, code, message);
	}

	public static <T> Result<T> build(int code, String message, String errorMsg) {
		return new Result<>(null, code, message, errorMsg);
	}


	public static Result<?> timeout() {
		return build(ResponseCode.SYSTEM_TIMEOUT);
	}

	public static Result<?> build(ResponseCode responseCode) {
		return build(responseCode.getCode(), responseCode.getMessage());
	}

	public static Result<?> build(ResponseCode responseCode, String errorMsg) {
		return build(responseCode.getCode(), responseCode.getMessage(), errorMsg);
	}

	public Result<T> putExtra(String key, Object value) {
		if (this.extra == null) {
			this.extra = new HashMap<>(16);
		}
		this.extra.put(key, value);
		return this;
	}

	public Result<T> putAllExtra(Map<Object, Object> extra) {
		if (this.extra == null) {
			this.extra = new HashMap<>(16);
		}
		this.extra.putAll(extra);
		return this;
	}

	public boolean isSuccess() {
		return code == 1;
	}

	/**
	 * 拼接字符串
	 *
	 * @return
	 */
	public static String toAppendStr(Object... strings) {
		StringBuffer sb = new StringBuffer();
		for (Object str : strings) {
			if (str != null) {
				sb.append(str);
			}
		}
		return sb.toString();
	}
}
