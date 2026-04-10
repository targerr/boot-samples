<!--
  App.vue - 根组件
  类似于 React 的 App.jsx 或 Spring Boot 的 Thymeleaf 主模板

  作用：
  - 整个应用的入口组件
  - 包含全局的 <router-view>，根据路由渲染不同的页面
  - main.js 中通过 app.mount('#app') 挂载到 index.html 的 <div id="app">

  <router-view>:
  - Vue Router 的内置组件
  - 类似于 React Router 的 <Outlet />
  - 根据当前 URL 路径，动态渲染对应的页面组件
  - 例如：访问 /login → 渲染 Login 组件；访问 /system/user → 渲染 User 组件
-->

<template>
  <!-- 路由出口：所有页面组件都在这里渲染 -->
  <router-view />
</template>
