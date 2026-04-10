# CLAUDE.md

此文件为 Claude Code (claude.ai/code) 提供在此代码库中工作的指导。

## 项目概述

企业级 RBAC（基于角色的访问控制）权限管理系统的前端项目。使用 Vue 3、Element Plus 和 Vite 构建。通过 axios 与后端 REST API（Spring Boot）通信。

**技术栈：**
- Vue 3.4（组合式 API + `<script setup>`）
- Element Plus 2.5（UI 组件库，中文语言环境）
- Vue Router 4.2（前端路由）
- Axios 1.6（HTTP 客户端）
- Vite 5（构建工具，开发服务器端口 5173）

## 开发命令

```bash
# 安装依赖
npm install

# 启动开发服务器（端口：5173，代理 /api → http://localhost:8080）
npm run dev

# 构建生产版本
npm run build

# 预览生产构建
npm run preview
```

## 架构

### 目录结构

```
src/
├── main.js              # 应用启动入口（Element Plus、图标、路由、指令）
├── App.vue              # 根组件
├── permission.js        # v-permission 权限指令设置
├── router/index.js      # 路由配置 + 导航守卫（token 校验）
├── api/                 # 基于 axios 的 API 封装
│   ├── auth.js         # 登录、登出、获取用户信息
│   ├── system.js       # 所有系统管理 CRUD API
│   └── generator.js    # 代码生成器 API
├── utils/               # 工具函数
│   ├── auth.js         # Token 存储（localStorage key: permission_token）
│   └── request.js      # Axios 实例 + 拦截器（token 注入、错误处理）
├── store/               # 响应式状态管理（存在但未完全集成）
│   └── index.js        # useUserStore() 组合式函数
├── layout/              # 布局组件
│   ├── MainLayout.vue  # 侧边栏 + 顶部栏 + 主内容区
│   └── Sidebar.vue     # 带路由的导航菜单
└── views/               # 页面组件
    ├── login/          # 登录页
    ├── dashboard/      # 数据统计首页
    ├── system/         # 用户、部门、权限、角色、日志管理
    ├── generator/      # 代码生成器界面
    └── error/          # 403、404 错误页
```

### 核心模式

**API 层模式：**
- `src/api/` 中所有 API 使用 `src/utils/request.js` 的 axios 实例
- 请求拦截器自动注入 `Authorization` 请求头（携带 token）
- 响应拦截器自动解包 `R<T>` 结构（code=200 时返回 res.data）
- 错误处理：401 重定向到登录页，其他错误显示 ElMessage 提示

**标准 CRUD 页面组件结构：**
```vue
<template>
  <!-- 搜索卡片：el-form + queryParams -->
  <!-- 数据卡片：el-table + el-pagination -->
  <!-- 新增/编辑弹窗：el-form + validation rules -->
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { apiFunctions } from '@/api/system'

const queryParams = reactive({ pageNum: 1, pageSize: 10, ... })
const tableData = ref([])
const dialogVisible = ref(false)

const fetchData = async () => { /* 调用 API */ }
const handleAdd = () => { /* 重置表单 */ }
const handleEdit = (row) => { /* 填充表单 */ }
const handleSubmit = async () => { /* 校验 + 保存 */ }
</script>
```

**权限指令（`v-permission`）：**
- 定义在 `src/permission.js`，在 `main.js` 中注册
- 检查权限 ID 是否存在于 localStorage 的 `permissions` 数组中
- 用户无权限时从 DOM 中移除元素
- 用法：`<el-button v-permission="7">删除</el-button>`（其中 7 是权限点 ID）

**路由守卫：**
- Token 存储在 `localStorage.getItem('permission_token')`
- 未认证用户重定向到 `/login`
- 已登录用户访问登录页时重定向到首页

**状态管理：**
- Store 位于 `src/store/index.js`，使用 Vue 3 `reactive()`
- 注意：未完全集成 - MainLayout 直接调用 API 而非使用 store
- 权限存储为 ACL ID 数组，存于 `localStorage.getItem('permissions')`

### 后端集成

**API 代理：**
- 开发服务器代理 `/api` → `http://localhost:8080`（见 `vite.config.js`）
- 所有 API 调用通过 axios baseURL 使用 `/api/` 前缀

**响应格式：**
```json
{ "code": 200, "msg": "success", "data": {...} }
```
- 分页数据返回 `{ total, data, pageNum, pageSize }`

**认证流程：**
1. 登录 → 后端返回 token
2. Token 以 `permission_token` 为 key 存入 localStorage
3. 后续请求自动携带 `Authorization: <token>` 请求头
4. 后端通过 Sa-Token 验证 token

## 重要说明

**默认登录凭据：** `Admin` / `123456`

**树形结构处理：**
- 部门树和权限模块树以嵌套对象形式返回
- 使用递归扁平化函数转换为下拉选项：
```js
const flatten = (list) => {
  const result = []
  list.forEach(item => {
    result.push({ id: item.id, name: item.name })
    if (item.children) result.push(...flatten(item.children))
  })
  return result
}
```

**表单校验：**
- 密码字段仅在新增时必填（使用条件 prop：`:prop="isEdit ? '' : 'password'"`）
- 使用 el-switch 切换状态（1=正常，0=冻结）

**图标使用：**
- 所有 Element Plus 图标在 `main.js` 中全局注册
- 直接使用：`<el-icon><House /></el-icon>`
- 路由 meta 中配置图标名称供侧边栏使用

**CSS 作用域：**
- 组件样式使用 `<style scoped>`
- 深度选择器：`:deep(.selector)` 用于覆盖 Element Plus 组件样式
