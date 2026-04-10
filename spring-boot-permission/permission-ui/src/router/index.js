/**
 * Vue Router 配置文件
 * 类似于 Spring Boot 的 WebMvcConfigurer 或 Controller 路由映射
 *
 * 前端路由 vs 后端路由：
 * - 后端路由：@RequestMapping("/api/users") 对应 HTTP 请求路径
 * - 前端路由：path: '/system/user' 对应浏览器 URL 路径
 * - 前端路由支持懒加载：component: () => import(...) 按需加载，减少初始包体积
 */

import { createRouter, createWebHistory } from 'vue-router'

/**
 * 静态路由配置
 * 每个路由对象包含：
 * - path: URL 路径
 * - name: 路由名称（用于编程式导航：router.push({ name: 'Login' })）
 * - component: 对应的 Vue 组件
 * - meta: 元信息（自定义数据，如标题、图标）
 * - redirect: 重定向目标
 * - children: 嵌套路由（子路由）
 */
const routes = [
  {
    path: '/login',           // URL 路径
    name: 'Login',            // 路由名称
    component: () => import('@/views/login/index.vue'),  // 懒加载组件
    meta: { title: '登录' }   // 元信息：页面标题
  },
  {
    path: '/',                // 根路径
    component: () => import('@/layout/MainLayout.vue'),  // 布局组件
    redirect: '/dashboard',   // 默认重定向到首页
    children: [               // 子路由（会渲染在布局组件的 <router-view> 中）
      {
        path: 'dashboard',    // 完整路径为 /dashboard
        name: 'Dashboard',
        component: () => import('@/views/dashboard/index.vue'),
        meta: { title: '首页', icon: 'House' }  // icon 供侧边栏使用
      },
      {
        path: 'system/user',  // 完整路径为 /system/user
        name: 'SystemUser',
        component: () => import('@/views/system/user/index.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'system/dept',
        name: 'SystemDept',
        component: () => import('@/views/system/dept/index.vue'),
        meta: { title: '部门管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'system/acl',
        name: 'SystemAcl',
        component: () => import('@/views/system/acl/index.vue'),
        meta: { title: '权限管理', icon: 'Lock' }
      },
      {
        path: 'system/role',
        name: 'SystemRole',
        component: () => import('@/views/system/role/index.vue'),
        meta: { title: '角色管理', icon: 'UserFilled' }
      },
      {
        path: 'system/log',
        name: 'SystemLog',
        component: () => import('@/views/system/log/index.vue'),
        meta: { title: '操作日志', icon: 'Document' }
      },
      {
        path: 'generator/index',
        name: 'Generator',
        component: () => import('@/views/generator/index.vue'),
        meta: { title: '代码生成器', icon: 'Cpu' }
      }
    ]
  },
  {
    path: '/403',
    name: '403',
    component: () => import('@/views/error/403.vue'),
    meta: { title: '无权限' }
  },
  {
    path: '/:pathMatch(.*)*',  // 通配符路由，匹配所有未定义的路径
    name: '404',
    component: () => import('@/views/error/404.vue'),
    meta: { title: '页面不存在' }
  }
]

/**
 * 创建路由实例
 * - createWebHistory(): 使用 HTML5 History 模式（URL 不带 # 号）
 *   生产环境需要服务器配置支持，所有路由都指向 index.html
 * - createWebHashHistory(): 使用 Hash 模式（URL 带 # 号，如 /#/user）
 *   不需要服务器配置，但 URL 不美观
 */
const router = createRouter({
  history: createWebHistory(),  // History 模式
  routes
})

/**
 * 路由守卫（全局前置守卫）
 * 类似于 Spring 的拦截器或 Filter
 *
 * 执行时机：每次路由跳转前
 * 参数：
 * - to: 目标路由对象
 * - from: 来源路由对象
 * - next: 必须调用的函数，决定是否放行
 *
 * next() 的用法：
 * - next(): 放行，进入目标路由
 * - next('/path'): 重定向到指定路径
 * - next(false): 中止导航
 */
router.beforeEach(async (to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 权限管理系统` : '权限管理系统'

  // 从 localStorage 获取 token（类似于从 Session/Cookie 获取认证信息）
  const token = localStorage.getItem('permission_token')

  /**
   * 登录页特殊处理
   * - 已登录用户访问登录页 → 重定向到首页
   * - 未登录用户访问登录页 → 放行
   */
  if (to.path === '/login') {
    token ? next('/') : next()  // 三元表达式：condition ? true : false
    return
  }

  /**
   * 认证检查
   * 未登录用户访问受保护页面 → 重定向到登录页
   */
  if (!token) {
    next('/login')
    return
  }

  /**
   * 权限检查（RBAC：基于角色的访问控制）
   * 检查用户是否有权限访问目标路由
   *
   后端对应：@SaCheckPermission("system:user:list")
   */
  if (to.path !== '/dashboard') {  // 首页不做权限检查
    try {
      // 从 localStorage 获取用户菜单权限（由后端返回）
      const perms = localStorage.getItem('menus')
      const menus = perms ? JSON.parse(perms) : []

      // 检查是否有访问该路由的权限（精确匹配或前缀匹配）
      const hasPermission = menus.some(p => {
        return p === to.path || to.path.startsWith(p)
      })

      if (!hasPermission) {
        next('/')  // 无权限，重定向到首页
        return
      }
    } catch (e) {
      next('/')  // 权限检查失败，重定向到首页
      return
    }
  }

  next()  // 所有检查通过，放行
})

export default router
