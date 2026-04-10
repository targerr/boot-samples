/**
 * Token 存储工具
 * 类似于 Spring Security 的 SecurityContextHolder 或 Session 管理
 *
 * localStorage vs sessionStorage vs Cookie：
 * - localStorage：永久存储（除非手动删除），跨标签页共享
 * - sessionStorage：会话级别，关闭标签页后清除
 * - Cookie：每次请求自动携带，有大小限制（4KB）
 *
 * 本项目使用 localStorage 存储 token
 * 注意：生产环境应考虑 XSS 攻击风险，可结合 httpOnly Cookie
 */

/**
 * Token 存储的 Key 值
 * 类似于 Spring 的配置常量：private static final String TOKEN_KEY = "permission_token";
 */
const TOKEN_KEY = 'permission_token'

/**
 * 获取 Token
 * @returns {string|null} Token 字符串，不存在时返回 null
 *
 * 使用场景：
 * - 发送 API 请求前，检查用户是否已登录
 * - 路由守卫中验证用户身份
 */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

/**
 * 设置 Token
 * @param {string} token - 后端返回的认证令牌
 *
 * 调用时机：
 * - 用户登录成功后，保存后端返回的 token
 * - token 刷新成功后
 *
 * 类似于：SecurityContextHolder.getContext().setAuthentication(auth)
 */
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

/**
 * 移除 Token
 * 调用时机：
 * - 用户主动登出
 * - Token 过期或无效
 * - 安全退出
 *
 * 类似于：session.invalidate() 或 SecurityContextHolder.clearContext()
 */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
}
