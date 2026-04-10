# 前端代码注释执行计划

**日期**: 2026-04-07
**需求文档**: docs/requirements/2026-04-07-frontend-comments.md
**内部执行等级**: L（串行原生执行）

## 批次结构

按文件功能分组，分4批次执行：

### Wave 1: 核心架构层（6个文件）
- src/main.js - 应用入口
- src/router/index.js - 路由配置
- src/store/index.js - 状态管理
- src/utils/auth.js - Token 存储
- src/utils/request.js - Axios 配置
- src/permission.js - 权限指令

### Wave 2: API 集成层（3个文件）
- src/api/auth.js - 认证 API
- src/api/system.js - 系统 CRUD API
- src/api/generator.js - 代码生成器 API

### Wave 3: 布局组件（2个文件）
- src/layout/MainLayout.vue - 主布局
- src/layout/Sidebar.vue - 侧边栏导航

### Wave 4: 页面组件（11个文件）
- src/App.vue - 根组件
- src/views/login/index.vue - 登录页
- src/views/dashboard/index.vue - 首页仪表盘
- src/views/system/user/index.vue - 用户管理
- src/views/system/dept/index.vue - 部门管理
- src/views/system/role/index.vue - 角色管理
- src/views/system/acl/index.vue - 权限管理
- src/views/system/log/index.vue - 日志管理
- src/views/generator/index.vue - 代码生成器
- src/views/error/403.vue - 403错误页
- src/views/error/404.vue - 404错误页

## 所有权边界

- 所有文件编辑在单一 governed lane 执行
- 不创建子代理
- 不使用并行执行

## 验证命令

每个 Wave 完成后：
```bash
# 检查文件是否修改
git diff --name-only 2>/dev/null || ls -lt src/**/*.vue src/**/*.js | head -20
```

## 交付验收计划

1. 每个文件有文件头注释
2. Vue 组件有：template 结构说明、script setup 核心逻辑
3. JS 文件有：模块用途、导出说明
4. 重点注释：ref/reactive/computed、生命周期、API 调用、前后端数据流

## 完成语言规则

- 不使用"全部完成"表述
- 最终交付：注释摘要报告，列出已注释文件

## 回滚规则

- 仅添加注释，不修改代码
- 如需回滚：删除所有新增的注释行（或恢复原文件）

## 阶段清理期望

- 保留需求文档和执行计划
- 不创建临时文件
- 最终输出：注释添加报告