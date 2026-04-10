<!--
  主布局组件
  类似于 Spring Boot 的 Thymeleaf 模板或 React 的 Layout 组件

  组件结构：
  - el-container: 容器组件（Element Plus 提供的布局组件）
  - el-aside: 侧边栏（左侧导航菜单）
  - el-header: 顶部栏（面包屑、用户信息、退出登录）
  - el-main: 主内容区（<router-view> 渲染当前路由的页面组件）

  <router-view> 是什么？
  - Vue Router 的内置组件
  - 类似于 React Router 的 <Outlet /> 或 Thymeleaf 的 th:replace
  - 根据当前路由，动态渲染对应的页面组件
  - 例如：访问 /system/user → 渲染 views/system/user/index.vue

  Vue 3 组合式 API：<script setup>
  - setup 是一个特殊的选项，是组合式 API 的入口
  - <script setup> 是 setup 的语法糖，更简洁
  - 在 <script setup> 中定义的变量和函数，自动暴露给模板使用
-->

<template>
  <!-- 布局容器：固定高度占满视口 -->
  <el-container class="layout-container">
    <!-- 侧边栏：根据 isCollapse 状态动态调整宽度 -->
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <!-- Logo 区域 -->
      <div class="logo">
        <el-icon size="24"><Setting /></el-icon>
        <!-- v-show: 条件显示（display: none），元素仍在 DOM 中 -->
        <span v-show="!isCollapse" class="logo-text">权限管理系统</span>
      </div>
      <!-- 侧边栏菜单组件 -->
      <Sidebar :is-collapse="isCollapse" />
    </el-aside>

    <!-- 右侧内容区域 -->
    <el-container>
      <!-- 顶栏：面包屑导航 + 用户信息 -->
      <el-header class="header">
        <!-- 左侧：折叠按钮 + 面包屑 -->
        <div class="header-left">
          <!-- 折叠/展开侧边栏按钮 -->
          <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
            <Expand v-if="isCollapse" />
            <Fold v-else />
          </el-icon>
          <!-- 面包屑导航：显示当前页面路径 -->
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <!-- 只在非首页时显示当前页面标题 -->
            <el-breadcrumb-item v-if="$route.meta.title && $route.name !== 'Dashboard'">
              <!-- $route: Vue Router 注入的全局对象，表示当前路由 -->
              {{ $route.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <!-- 右侧：用户信息下拉菜单 -->
        <div class="header-right">
          <!-- el-dropdown: 下拉菜单组件 -->
          <el-dropdown @command="handleCommand">
            <span class="user-info">
              <!-- 头像组件 -->
              <el-avatar :size="32" icon="UserFilled" />
              <!-- 可选链操作符：userInfo?.username 等同于 userInfo && userInfo.username -->
              <span class="username">{{ userInfo?.username || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <!-- 下拉菜单内容 -->
            <template #dropdown>
              <el-dropdown-menu>
                <!-- command: 点击时触发 handleCommand('logout') -->
                <el-dropdown-item command="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 主内容区：渲染当前路由对应的页面组件 -->
      <el-main class="main">
        <!-- router-view: 路由出口，动态渲染组件 -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
/**
 * Vue 3 组合式 API
 *
 * import 语句：导入 Vue 核心功能和第三方模块
 * - ref: 创建响应式基本类型（如 isCollapse = false）
 * - onMounted: 组件挂载后执行的钩子（类似 React 的 useEffect(() => {}, [])）
 * - useRouter: Vue Router 提供的钩子，获取路由实例
 */
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getUserInfo, logout } from '@/api/auth'
import Sidebar from './Sidebar.vue'

/**
 * useRouter: 获取路由实例
 * 类似于 React Router 的 useNavigate()
 * 使用场景：编程式导航（router.push('/login')）
 */
const router = useRouter()

/**
 * ref: 创建响应式数据
 * isCollapse 控制侧边栏折叠状态
 * 使用时需要 .value 访问：isCollapse.value = true
 * 但在模板中自动解包，不需要 .value
 */
const isCollapse = ref(false)
const userInfo = ref(null)

/**
 * 获取用户信息
 * async/await: 异步函数调用
 * 类似于 Java 的 CompletableFuture 或 @Async
 *
 * 使用示例：
 * const data = await fetchData()  // 等待异步操作完成
 */
const fetchUserInfo = async () => {
  try {
    userInfo.value = await getUserInfo()
  } catch (e) {
    // ignore: 静默处理错误
  }
}

/**
 * 处理下拉菜单命令
 * @param {string} command - 命令名称（如 'logout'）
 */
const handleCommand = async (command) => {
  if (command === 'logout') {
    try { await logout() } catch (e) {}  // 调用后端登出接口
    // 清除本地存储的数据
    localStorage.removeItem('permission_token')
    localStorage.removeItem('menus')
    localStorage.removeItem('permissions')
    // 跳转到登录页
    router.push('/login')
  }
}

/**
 * onMounted: 生命周期钩子
 * 组件挂载到 DOM 后执行（类似 React 的 componentDidMount）
 * 使用场景：初始化数据、订阅事件
 */
onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
/*
  scoped: 作用域样式
  - 样式只作用于当前组件
  - Vue 会自动添加 data-v-xxx 属性来隔离样式
  - 类似于 React 的 CSS Modules
*/

.layout-container { height: 100vh; }  /* vh: 视口高度单位（100vh = 屏幕高度） */
.aside {
  background-color: #304156;
  transition: width 0.3s;  /* CSS 过渡动画：宽度变化时平滑过渡 */
  overflow: hidden;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
}

.logo-text { white-space: nowrap; }  /* 防止文字换行 */

.header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);  /* 阴影效果 */
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}

.header-left { display: flex; align-items: center; gap: 16px; }
.collapse-btn { font-size: 20px; cursor: pointer; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.username { font-size: 14px; }
.main { background: #f0f2f5; }
</style>
