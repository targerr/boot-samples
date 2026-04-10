/**
 * Axios 请求封装
 * 类似于 Spring Boot 的 RestTemplate 或 Feign Client
 *
 * 功能：
 * 1. 统一配置 baseURL（后端 API 地址）
 * 2. 请求拦截器：自动注入 token
 * 3. 响应拦截器：统一处理响应数据和错误
 *
 * 前后端交互流程：
 * 前端 → 发送请求 → [请求拦截器：添加 token] → 后端
 * 后端 → 返回响应 → [响应拦截器：解包数据/处理错误] → 前端组件
 */

import axios from 'axios'                  // HTTP 客户端库
import { ElMessage } from 'element-plus'   // Element Plus 消息提示组件
import { getToken, removeToken } from './auth'

/**
 * 创建 Axios 实例
 * 类似于 Spring 中配置 RestTemplate bean
 *
 * baseURL 配置说明：
 * - 开发环境：'/api' → Vite 代理转发到 http://localhost:8080
 * - 生产环境：Nginx 反向代理到后端服务
 *
 * 代理配置位置：vite.config.js
 * server.proxy: { '/api': { target: 'http://localhost:8080' } }
 */
const request = axios.create({
  baseURL: '/api',     // 所有请求的基础路径
  timeout: 15000       // 请求超时时间（毫秒）
})

/**
 * 请求拦截器
 * 类似于 Spring 的 HandlerInterceptor 或 Feign RequestInterceptor
 *
 * 执行时机：发送请求前
 * 用途：
 * 1. 自动注入 token 到请求头
 * 2. 添加通用请求参数
 * 3. 请求签名、加密等
 *
 * config 对象：
 * - url: 请求地址
 * - method: 请求方法
 * - headers: 请求头（可以在这里添加 Authorization）
 * - data: 请求体数据
 */
request.interceptors.request.use(
  (config) => {
    // 从 localStorage 获取 token
    const token = getToken()

    /**
     * 如果 token 存在，添加到请求头
     * 后端 Sa-Token 通过请求头获取 token 进行认证
     * 类似于：@RequestHeader("Authorization") String token
     *
     * 注意：后端 Sa-Token 默认使用 "Authorization" 请求头
     */
    if (token) {
      config.headers.Authorization = token
    }

    return config  // 必须返回 config，否则请求不会发送
  },
  (error) => {
    // 请求配置错误处理
    return Promise.reject(error)
  }
)

/**
 * 响应拦截器
 * 类似于 Spring 的 @ControllerAdvice 或 ResponseBodyAdvice
 *
 * 执行时机：收到响应后
 * 用途：
 * 1. 统一解包后端返回的 R<T> 结构
 * 2. 处理业务错误码（非 200）
 * 3. 处理 HTTP 错误（401、500 等）
 * 4. 统一错误提示
 *
 * 后端响应格式：
 * {
 *   "code": 200,        // 业务状态码
 *   "msg": "success",   // 消息
 *   "data": {...}       // 实际数据
 * }
 */
request.interceptors.response.use(
  (response) => {
    const res = response.data  // 获取响应体

    /**
     * 成功响应：code === 200
     * 直接返回 data 字段，简化组件中的使用
     *
     * 使用示例：
     * const users = await getUserList()  // 直接得到用户数组，不需要 .data
     */
    if (res.code === 200) {
      return res.data
    }

    /**
     * 未授权：code === 401
     * 处理场景：
     * - Token 过期
     * - Token 无效
     * - 用户被禁用
     *
     * 处理方式：
     * 1. 清除本地 token
     * 2. 重定向到登录页
     * 3. 拒绝当前的 Promise
     */
    if (res.code === 401) {
      removeToken()              // 清除 token
      window.location.href = '/login'  // 强制跳转登录页
      return Promise.reject(new Error(res.msg || '未授权'))
    }

    /**
     * 其他业务错误
     * 显示错误消息提示
     * ElMessage：Element Plus 的消息提示组件
     * 类似于：Toast.makeText() 或 Snackbar.show()
     */
    ElMessage.error(res.msg || '请求失败')
    return Promise.reject(new Error(res.msg || '请求失败'))
  },
  /**
   * HTTP 错误处理
   * 场景：
   * - 网络断开
   * - 服务器无响应
   * - CORS 错误
   * - 500 服务器内部错误
   */
  (error) => {
    ElMessage.error('网络异常')
    return Promise.reject(error)
  }
)

export default request
