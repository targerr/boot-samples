<!--
  首页仪表盘组件
  类似于 Spring Boot Actuator 的健康检查页面或管理后台的 Dashboard

  功能：
  - 显示系统统计数据（用户数、角色数、部门数、权限数）
  - 欢迎信息
-->

<template>
  <div class="dashboard">
    <!--
      el-row + el-col: 栅格布局系统
      类似于 Bootstrap 的 row + col
      - :gutter: 列之间的间距（像素）
      - :span: 列宽度（总共24格，:span="6" 占 1/4）
    -->
    <el-row :gutter="20">
      <!-- 用户总数卡片 -->
      <el-col :span="6">
        <!--
          el-card: 卡片组件
          - shadow: 阴影效果（hover/always/never）
          - #header: 卡片标题插槽
        -->
        <el-card shadow="hover">
          <template #header>用户总数</template>
          <!-- 统计数字显示 -->
          <div class="stat-number">{{ stats.userCount }}</div>
        </el-card>
      </el-col>

      <!-- 角色总数卡片 -->
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>角色总数</template>
          <div class="stat-number">{{ stats.roleCount }}</div>
        </el-card>
      </el-col>

      <!-- 部门总数卡片 -->
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>部门总数</template>
          <div class="stat-number">{{ stats.deptCount }}</div>
        </el-card>
      </el-col>

      <!-- 权限总数卡片 -->
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>权限总数</template>
          <div class="stat-number">{{ stats.aclCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 欢迎信息卡片 -->
    <el-card style="margin-top: 20px;">
      <template #header>欢迎使用权限管理系统</template>
      <p>默认账号: Admin / 12345678</p>
    </el-card>
  </div>
</template>

<script setup>
/**
 * Vue 3 组合式 API
 *
 * - ref: 创建响应式数据
 * - onMounted: 生命周期钩子，组件挂载后执行
 */
import { ref, onMounted } from 'vue'
import { getUserPage } from '@/api/system'
import { getRoleList } from '@/api/system'
import { getDeptTree } from '@/api/system'
import { getAclModuleTree } from '@/api/system'

/**
 * 统计数据
 * 存储：用户数、角色数、部门数、权限数
 */
const stats = ref({
  userCount: 0,
  roleCount: 0,
  deptCount: 0,
  aclCount: 0
})

/**
 * onMounted: 组件挂载后执行
 * 类似于 React 的 useEffect(() => {}, []) 或 jQuery 的 $(document).ready()
 *
 * 使用场景：初始化数据、订阅事件、调用 API
 */
onMounted(async () => {
  try {
    /**
     * Promise.all: 并发执行多个异步请求
     * 类似于 Java 的 CompletableFuture.allOf()
     *
     * 优势：多个请求同时发送，减少等待时间
     * 相比顺序请求：await A(); await B(); await C();
     * 并发请求只需等待最慢的那个
     */
    const [users, roles, depts, modules] = await Promise.all([
      // 获取用户总数（分页接口，只请求1条数据获取 total）
      getUserPage({ pageNum: 1, pageSize: 1 }).catch(() => ({ total: 0 })),
      // 获取角色列表
      getRoleList().catch(() => []),
      // 获取部门树
      getDeptTree().catch(() => []),
      // 获取权限模块树
      getAclModuleTree().catch(() => [])
    ])

    // 更新统计数据
    stats.value.userCount = users.total || 0
    stats.value.roleCount = roles.length || 0
    stats.value.deptCount = depts.length || 0

    /**
     * 计算权限总数
     * reduce: 数组归约方法，计算累加值
     * 类似于 Java Stream 的 reduce()
     *
     * 逻辑：遍历所有模块，累加每个模块下的权限点数量
     * m.aclList?.length: 可选链操作符，如果 aclList 不存在返回 undefined
     */
    stats.value.aclCount = modules.reduce((sum, m) => sum + (m.aclList?.length || 0), 0)
  } catch (e) {
    // 静默处理错误
  }
})
</script>

<style scoped>
.stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;  /* Element Plus 主题色 */
  text-align: center;
}
</style>
