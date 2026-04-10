<!--
  侧边栏导航菜单组件
  类似于 Spring Boot 的 Thymeleaf fragment 或 React 的 Sidebar 组件

  功能：
  - 显示导航菜单
  - 根据用户权限动态显示/隐藏菜单项
  - 支持菜单折叠
  - 支持多级菜单（子菜单）

  Element Plus el-menu 组件：
  - router: 启用路由模式，点击菜单项自动跳转
  - :default-active: 当前激活的菜单项
  - :collapse: 是否折叠菜单
  - :unique-opened: 只保持一个子菜单展开
-->

<template>
  <!--
    el-menu: 导航菜单组件
    - $route.path: 当前路由路径（如 '/system/user'）
    - router: 启用路由模式，点击菜单项会自动跳转
  -->
  <el-menu
    :default-active="$route.path"
    :collapse="isCollapse"
    background-color="#304156"
    text-color="#bfcbd9"
    active-text-color="#409EFF"
    :unique-opened="true"
    router
  >
    <!-- 首页菜单项（固定显示，不需要权限） -->
    <el-menu-item index="/dashboard">
      <el-icon><House /></el-icon>
      <template #title>首页</template>
    </el-menu-item>

    <!--
      v-for: 列表渲染指令
      类似于 Java 的 for 循环或 Thymeleaf 的 th:each
      :key: 必须指定唯一的 key 值，帮助 Vue 高效更新 DOM
    -->
    <template v-for="menu in filteredMenus" :key="menu.index">
      <!-- 有子菜单的情况：使用 el-sub-menu（可展开/折叠） -->
      <el-sub-menu v-if="menu.children && menu.children.length > 0" :index="menu.index">
        <template #title>
          <!-- component :is: 动态组件，根据 icon 名称渲染对应的图标组件 -->
          <el-icon><component :is="menu.icon" /></el-icon>
          <span>{{ menu.title }}</span>
        </template>
        <!-- 子菜单项 -->
        <el-menu-item v-for="child in menu.children" :key="child.index" :index="child.index">
          <el-icon><component :is="child.icon" /></el-icon>
          <template #title>{{ child.title }}</template>
        </el-menu-item>
      </el-sub-menu>

      <!-- 无子菜单的情况：直接使用 el-menu-item -->
      <el-menu-item v-else :index="menu.index">
        <el-icon><component :is="menu.icon" /></el-icon>
        <template #title>{{ menu.title }}</template>
      </el-menu-item>
    </template>
  </el-menu>
</template>

<script setup>
/**
 * Vue 3 组合式 API
 *
 * - computed: 计算属性，基于响应式数据自动计算并缓存结果
 * - onMounted: 生命周期钩子，组件挂载后执行
 * - ref: 创建响应式数据
 */
import { computed, onMounted, ref } from 'vue'

/**
 * defineProps: 定义组件接收的 props（父组件传递的数据）
 * 类似于 React 的 PropTypes 或 Java 的方法参数
 *
 * 使用示例（父组件）：
 * <Sidebar :is-collapse="true" />
 *
 * 简写方式：defineProps({ isCollapse: Boolean })
 * 不需要显式声明 props 变量，直接在模板中使用
 */
defineProps({
  isCollapse: Boolean
})

/**
 * 所有菜单配置（静态数据）
 * 结构说明：
 * - index: 菜单项的唯一标识
 * - title: 显示文本
 * - icon: Element Plus 图标组件名称
 * - path: 路由路径（用于权限检查）
 * - children: 子菜单数组
 */
const allMenus = [
  {
    index: '/system',
    title: '系统管理',
    icon: 'Setting',
    children: [
      { index: '/system/user', title: '用户管理', icon: 'User', path: '/system/user' },
      { index: '/system/dept', title: '部门管理', icon: 'OfficeBuilding', path: '/system/dept' },
      { index: '/system/acl', title: '权限管理', icon: 'Lock', path: '/system/acl' },
      { index: '/system/role', title: '角色管理', icon: 'UserFilled', path: '/system/role' },
      { index: '/system/log', title: '操作日志', icon: 'Document', path: '/system/log' }
    ]
  },
  {
    index: '/generator',
    title: '开发工具',
    icon: 'MagicStick',
    children: [
      { index: '/generator/index', title: '代码生成器', icon: 'Cpu', path: '/generator/index' }
    ]
  }
]

/**
 * 用户权限列表（从 localStorage 读取）
 * 存储 format: ["/system/user", "/system/dept", ...]
 */
const userPermissions = ref([])

/**
 * onMounted: 组件挂载后执行
 * 读取 localStorage 中的用户权限
 */
onMounted(() => {
  try {
    const perms = localStorage.getItem('menus')  // 读取菜单权限
    userPermissions.value = perms ? JSON.parse(perms) : []
  } catch (e) {
    userPermissions.value = []
  }
})

/**
 * computed: 计算属性
 * 类似于 Java 的 Stream API 或 React 的 useMemo
 *
 * 特点：
 * - 自动追踪依赖（userPermissions 变化时自动重新计算）
 * - 结果缓存（依赖不变时不重新计算）
 * - 只读属性（不能直接赋值）
 *
 * 功能：根据用户权限过滤菜单
 */
const filteredMenus = computed(() => {
  return allMenus.map(menu => {
    // 过滤子菜单：只保留用户有权限的菜单项
    const filteredChildren = menu.children
      ? menu.children.filter(child => hasPermission(child.path))
      : []

    return {
      ...menu,  // 展开运算符：复制 menu 的所有属性
      children: filteredChildren
    }
  }).filter(menu => {
    // 过滤父菜单：如果子菜单为空，则不显示父菜单
    if (menu.children && menu.children.length > 0) {
      return menu.children.length > 0
    }
    return true
  })
})

/**
 * 检查用户是否有权限访问指定路径
 * @param {string} path - 路由路径
 * @returns {boolean} 是否有权限
 */
const hasPermission = (path) => {
  if (!path) return true
  // some: 数组方法，只要有一个元素满足条件就返回 true
  // 检查逻辑：精确匹配或前缀匹配
  return userPermissions.value.some(p => p === path || path.startsWith(p))
}
</script>

<style scoped>
.el-menu { border-right: none; }  /* 移除菜单右侧边框 */
</style>
