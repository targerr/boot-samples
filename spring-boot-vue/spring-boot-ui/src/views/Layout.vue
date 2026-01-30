<!--
  Layout.vue - 主布局组件
  功能：提供带侧边栏的页面布局结构，支持侧边栏展开/收起
  包含：左侧导航菜单、顶部用户信息栏、右侧内容区域
-->
<template>
  <div class="layout">
    <!-- Element Plus 容器组件，用于页面整体布局 -->
    <el-container>
      <!-- 左侧边栏：宽度根据折叠状态动态变化 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <!-- Logo区域：折叠时只显示首字 -->
        <div class="logo">{{ isCollapse ? '管' : '管理系统' }}</div>
        <!--
          导航菜单
          :default-active - 当前激活的菜单项，绑定到当前路由路径
          :collapse - 控制菜单是否折叠
          router - 启用路由模式，点击菜单项会自动跳转到对应路由
        -->
        <el-menu :default-active="route.path" :collapse="isCollapse" router>
          <!-- 商品管理菜单项 -->
          <el-menu-item index="/home">
            <span>商品管理</span>
          </el-menu-item>
          <!-- 博客管理菜单项 -->
          <el-menu-item index="/blog">
            <span>博客管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <!-- 右侧主体区域 -->
      <el-container>
        <!-- 顶部栏：折叠按钮、用户信息和退出按钮 -->
        <el-header>
          <!-- 折叠/展开按钮 -->
          <el-button @click="isCollapse = !isCollapse" class="collapse-btn">
            {{ isCollapse ? '☰' : '✕' }}
          </el-button>
          <div class="header-right">
            <span>用户：{{ userStore.getUsername }}</span>
            <el-button @click="userStore.logout()">退出</el-button>
          </div>
        </el-header>
        <!-- 主内容区域 -->
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// 导入 Vue 的 ref 函数用于创建响应式变量
import { ref } from "vue";
// 导入 Vue Router 的 useRoute 钩子
import { useRoute } from "vue-router";
// 导入用户状态管理 store
import { useUserStore } from "../store/user";

// 获取当前路由对象
const route = useRoute();
// 获取用户 store 实例
const userStore = useUserStore();
// 侧边栏折叠状态：false=展开，true=折叠
const isCollapse = ref(false);
</script>

<style scoped>
/* 布局容器占满整个视口高度 */
.layout {
  height: 100vh;
}
.el-container {
  height: 100%;
}
/* 侧边栏样式：添加过渡动画 */
.el-aside {
  background: #304156;
  transition: width 0.3s;
}
/* Logo样式 */
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  overflow: hidden;
}
/* 菜单样式 */
.el-menu {
  border-right: none;
  background: #304156;
}
.el-menu-item {
  color: #bfcbd9;
}
.el-menu-item.is-active {
  background: #263445;
  color: #409eff;
}
/* 顶部栏样式 */
.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: #fff;
  border-bottom: 1px solid #eee;
}
/* 折叠按钮样式 */
.collapse-btn {
  border: none;
  font-size: 18px;
}
/* 顶部右侧区域 */
.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}
</style>
