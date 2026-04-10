/**
 * 状态管理 Store（组合式 API 风格）
 * 类似于 Spring 的 Singleton Service 或 Session 作用域的 Bean
 *
 * 什么是状态管理？
 * - 在 Vue 中，组件之间共享数据需要"状态管理"
 * - 相当于一个全局的数据中心，组件可以从这里读取和修改数据
 * - 常见方案：Vuex、Pinia、或者简单的 reactive 对象（本项目使用）
 *
 * reactive vs ref：
 * - reactive(): 用于对象，自动深度响应式（类似 Java 的响应式数据绑定）
 * - ref(): 用于基本类型，需要 .value 访问
 * - const count = ref(0); count.value++  // 需要 .value
 * - const state = reactive({ count: 0 }); state.count++  // 不需要 .value
 */

import { reactive } from 'vue'
import { getToken, removeToken } from '@/utils/auth'
import { getUserInfo, logout } from '@/api/auth'

/**
 * 组合式函数（Composable）
 * 类似于 Spring 的 @Service 类，提供一组相关的方法
 *
 * 使用方式：
 * const { state, getUserInfoAction } = useUserStore()
 *
 * 为什么导出函数而不是对象？
 * - 每次调用函数都创建一个新的响应式 state
 * - 避免单例模式可能导致的状态污染
 * - 符合 Vue 3 组合式 API 的设计理念
 */
export const useUserStore = () => {
  /**
   * 响应式状态对象
   * 类似于 Java 的私有字段，但具有响应式特性
   *
   * 当这些数据发生变化时，所有使用它们的组件会自动更新
   * 类似于 Observer 模式或 Spring Data JPA 的实体监听
   */
  const state = reactive({
    token: getToken(),           // 用户认证令牌
    userInfo: null,              // 用户信息对象
    menus: [],                   // 用户菜单权限列表
    permissions: []              // 用户权限点 ID 列表
  })

  /**
   * 设置 Token
   * @param {string} token - 后端返回的认证令牌
   */
  const setToken = (token) => {
    state.token = token
  }

  /**
   * 设置用户信息
   * @param {object} info - 用户信息对象
   */
  const setUserInfo = (info) => {
    state.userInfo = info
  }

  /**
   * 设置菜单列表
   * @param {array} menus - 菜单权限数组
   */
  const setMenus = (menus) => {
    state.menus = menus
  }

  /**
   * 设置权限列表
   * @param {array} perms - 权限点 ID 数组
   */
  const setPermissions = (perms) => {
    state.permissions = perms
  }

  /**
   * 获取用户信息（Action 方法）
   * 类似于 Service 层的业务方法
   *
   * async/await：异步函数调用
   * 类似于 Java 的 CompletableFuture 或 @Async 方法
   *
   * 使用示例：
   * const userInfo = await getUserInfoAction()
   */
  const getUserInfoAction = async () => {
    const res = await getUserInfo()  // 调用 API 层
    state.userInfo = res              // 更新状态
    return res
  }

  /**
   * 登出操作（Action 方法）
   * 清空所有状态并移除 token
   *
   * try-catch：异常处理
   * 类似于 Java 的 try-catch 块
   */
  const logoutAction = async () => {
    try {
      await logout()  // 调用后端登出接口
    } catch (e) {
      // 忽略登出接口错误，继续执行清理逻辑
      // ignore
    }
    // 清空状态
    state.token = ''
    state.userInfo = null
    state.menus = []
    state.permissions = []
    // 移除本地存储的 token
    removeToken()
  }

  /**
   * 返回导出的内容
   * 使用对象解构：const { state, setToken } = useUserStore()
   */
  return {
    state,              // 响应式状态
    setToken,           // Setter 方法
    setUserInfo,
    setMenus,
    setPermissions,
    getUserInfoAction,  // Action 方法
    logoutAction
  }
}
