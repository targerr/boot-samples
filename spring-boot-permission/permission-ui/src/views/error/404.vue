<!--
  404 页面不存在错误页面
  类似于 Spring Boot 的 ErrorPageCustomizer 或自定义错误页面

  场景：
  - 用户访问不存在的路由时显示
  - 由路由的通配符规则匹配（/:pathMatch(.*)*）
-->
<template>
  <div class="error-container">
    <el-icon class="error-icon" :size="120" color="#909399"><Warning /></el-icon>
    <h1 class="error-code">404</h1>
    <p class="error-desc">抱歉，您访问的页面不存在</p>
    <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
  </div>
</template>

<script setup>
// 无需额外逻辑，纯展示组件
</script>

<style scoped>
.error-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;  /* 视口高度 */
  background: #f0f2f5;
}
.error-icon { margin-bottom: 20px; }
.error-code { font-size: 72px; font-weight: bold; color: #909399; margin: 0 0 12px 0; }
.error-desc { font-size: 16px; color: #606266; margin-bottom: 24px; }
</style>
