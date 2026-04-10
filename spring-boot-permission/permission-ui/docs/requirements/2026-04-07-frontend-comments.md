# 前端代码注释需求文档

**日期**: 2026-04-07
**用户角色**: 后端开发，前端了解较少
**项目**: permission-ui（Vue 3 + Element Plus）

## 目标

为所有业务前端文件添加中文注释，重点关注 Vue 特定概念和前后端集成，帮助后端开发者理解前端代码。

## 交付物

- 所有 `.vue` 和 `.js` 文件添加结构化中文注释
- 注释覆盖：Vue 3 组合式 API、响应式原理、组件通信、前后端交互模式

## 约束条件

1. **注释语言**: 中文
2. **不改变代码逻辑**: 仅添加注释，不修改任何功能代码
3. **注释风格**: 简洁实用，针对后端开发者视角
4. **Vue 3 语法**: 使用 `<script setup>` 和组合式 API 的解释

## 验收标准

1. 每个文件顶部有文件用途说明
2. 每个 Vue 组件有：template 结构说明、script 核心逻辑说明、关键 API 解释
3. 每个 JS 文件有：模块用途、导出内容、关键函数说明
4. Vue 特定概念：ref、reactive、computed、watch、生命周期、组件通信
5. 前后端集成：axios 配置、拦截器、认证流程、API 调用模式

## 完成语言策略

- 不声明"完成"直到所有文件注释添加完成
- 提供注释摘要报告

## 非目标

1. 不修改代码逻辑
2. 不重构代码结构
3. 不添加新功能
4. 不修复 bug（除非发现严重问题）

## 自主模式

interactive_governed - 按文件逐步添加，每批完成后可验证

## 推断假设

1. 用户熟悉 Java/Spring Boot，但前端概念需详细解释
2. 用户希望理解前后端如何协同工作
3. 注释应简洁，避免过度注释

## 文件清单（22个文件）

**核心架构（5个）**:
- src/main.js
- src/router/index.js
- src/store/index.js
- src/utils/request.js
- src/utils/auth.js
- src/permission.js

**API层（3个）**:
- src/api/auth.js
- src/api/system.js
- src/api/generator.js

**布局组件（2个）**:
- src/layout/MainLayout.vue
- src/layout/Sidebar.vue

**页面组件（11个）**:
- src/App.vue
- src/views/login/index.vue
- src/views/dashboard/index.vue
- src/views/system/user/index.vue
- src/views/system/dept/index.vue
- src/views/system/role/index.vue
- src/views/system/acl/index.vue
- src/views/system/log/index.vue
- src/views/generator/index.vue
- src/views/error/403.vue
- src/views/error/404.vue