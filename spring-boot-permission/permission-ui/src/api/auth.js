/**
 * 认证相关 API
 * 对应后端 SysAuthController
 *
 * 前后端 API 对应关系：
 * 前端：login(data) → 后端：POST /api/auth/login → SysAuthController.login()
 *
 * RESTful API 设计：
 * - POST: 创建资源或执行操作（登录、登出）
 * - GET: 获取资源（用户信息、权限列表）
 */

import request from '@/utils/request'

/**
 * 用户登录
 * @param {object} data - 登录参数 { username, password }
 * @returns {Promise<string>} 返回 token 字符串
 *
 * 后端对应：@PostMapping("/login")
 * public R<String> login(@RequestBody LoginDTO dto)
 *
 * 使用示例：
 * const token = await login({ username: 'Admin', password: '123456' })
 * setToken(token)
 */
export const login = (data) => request.post('/auth/login', data)

/**
 * 用户登出
 * @returns {Promise<void>}
 *
 * 后端对应：@PostMapping("/logout")
 * public R<Void> logout()
 *
 * 使用示例：
 * await logout()
 * removeToken()
 * router.push('/login')
 */
export const logout = () => request.post('/auth/logout')

/**
 * 获取当前用户信息
 * @returns {Promise<object>} 用户信息对象 { id, username, realName, email, ... }
 *
 * 后端对应：@GetMapping("/userInfo")
 * public R<SysUser> getUserInfo()
 *
 * 使用示例：
 * const userInfo = await getUserInfo()
 * console.log(userInfo.username)
 */
export const getUserInfo = () => request.get('/auth/userInfo')

/**
 * 获取当前用户权限列表
 * @returns {Promise<object>} 权限对象 { menus: [], permissions: [] }
 *
 * 后端对应：@GetMapping("/userPermissions")
 * public R<UserPermissionVO> getUserPermissions()
 *
 * 返回数据结构：
 * {
 *   menus: ['/system/user', '/system/dept', ...],  // 菜单权限（用于路由守卫）
 *   permissions: [1, 2, 3, 5, 7, ...]             // 权限点 ID（用于 v-permission 指令）
 * }
 *
 * 使用示例：
 * const { menus, permissions } = await getUserPermissions()
 * localStorage.setItem('menus', JSON.stringify(menus))
 * localStorage.setItem('permissions', JSON.stringify(permissions))
 */
export const getUserPermissions = () => request.get('/auth/userPermissions')
