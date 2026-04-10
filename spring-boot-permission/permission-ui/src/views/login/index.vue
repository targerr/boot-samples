<!--
  登录页面组件
  类似于 Spring Boot 的 Thymeleaf 登录模板或 React 的 Login 组件

  功能：
  - 用户名/密码输入
  - 表单验证
  - 登录请求
  - 存储 token 和权限信息
  - 跳转到首页
-->

<template>
  <div class="login-container">
    <!-- 登录卡片 -->
    <div class="login-card">
      <h2 class="login-title">权限管理系统</h2>

      <!--
        el-form: 表单组件
        - ref: 获取表单实例（用于调用 validate 方法）
        - :model: 绑定表单数据对象
        - :rules: 绑定验证规则
        - label-width="0": 不显示标签（输入框有 placeholder）
      -->
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <!--
          el-form-item: 表单项
          - prop: 对应 form 中的字段名，用于验证和错误提示
        -->
        <el-form-item prop="username">
          <!--
            el-input: 输入框组件
            - v-model: 双向数据绑定（输入变化时 form.username 自动更新）
            - prefix-icon: 前置图标
            - size: 尺寸（large/medium/small）
          -->
          <el-input v-model="form.username" prefix-icon="User" placeholder="请输入用户名" size="large" />
        </el-form-item>

        <el-form-item prop="password">
          <!--
            type="password": 密码输入框
            show-password: 显示/隐藏密码按钮
            @keyup.enter: 回车键事件监听（触发登录）
          -->
          <el-input
            v-model="form.password"
            prefix-icon="Lock"
            placeholder="请输入密码"
            type="password"
            show-password
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <!--
            el-button: 按钮组件
            - type: 按钮类型（primary/success/warning/danger）
            - :loading: 加载状态（true 时显示 loading 图标，禁用按钮）
            - @click: 点击事件监听
          -->
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登 录
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
/**
 * Vue 3 组合式 API
 *
 * - ref: 创建响应式数据（用于基本类型和 DOM 引用）
 * - reactive: 创建响应式对象（用于表单数据）
 * - useRouter: Vue Router 提供的钩子
 */
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { login, getUserPermissions } from '@/api/auth'
import { ElMessage } from 'element-plus'

/**
 * useRouter: 获取路由实例
 * 使用场景：编程式导航（router.push() 跳转页面）
 */
const router = useRouter()

/**
 * ref: 创建响应式数据
 * formRef: 存储 el-form 的 DOM 引用，用于调用表单方法
 * loading: 登录按钮的加载状态
 *
 * 使用 ref 获取 DOM 元素：
 * - 在模板中 ref="formRef"
 * - 在 script 中 formRef.value 自动绑定到对应的 DOM 元素
 */
const formRef = ref(null)
const loading = ref(false)

/**
 * reactive: 创建响应式对象
 * 类似于 ref({ username: '', password: '' })
 * 区别：reactive 不需要 .value，直接访问属性
 *
 * 使用场景：表单数据、配置对象等复杂数据结构
 */
const form = reactive({
  username: '',
  password: ''
})

/**
 * 表单验证规则
 * 类似于 Spring 的 @Valid 注解 + Hibernate Validator
 *
 * rules 对象的 key 对应 form 的字段名
 * 每个 rule 是一个数组，可以包含多个验证规则
 */
const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6位', trigger: 'blur' }
  ]
}

/**
 * 处理登录
 * async/await: 异步函数，使异步代码看起来像同步代码
 *
 * 登录流程：
 * 1. 表单验证（validate）
 * 2. 调用登录接口
 * 3. 存储 token
 * 4. 获取用户权限
 * 5. 存储权限信息
 * 6. 跳转到首页
 */
const handleLogin = async () => {
  // 表单验证：如果验证失败，抛出异常，中断执行
  await formRef.value.validate()

  // 显示 loading 状态
  loading.value = true

  try {
    // 调用登录接口
    const res = await login(form)

    // 存储 token 到 localStorage
    localStorage.setItem('permission_token', res.token)

    // 获取用户权限列表（包含菜单和按钮权限）
    const permData = await getUserPermissions()

    // 存储权限信息到 localStorage
    localStorage.setItem('menus', JSON.stringify(permData.menus || []))
    localStorage.setItem('permissions', JSON.stringify(permData.permissions || []))

    // 显示成功提示
    ElMessage.success('登录成功')

    // 跳转到首页（/）
    router.push('/')
  } catch (e) {
    // 错误处理已在 request 拦截器中处理（显示错误提示）
    // 这里可以添加额外的错误处理逻辑
  } finally {
    // 无论成功或失败，都关闭 loading 状态
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;  /* 视口高度 */
  display: flex;
  align-items: center;
  justify-content: center;
  /* 渐变背景：从左上到右下，紫色渐变 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,.1);  /* 阴影效果 */
}

.login-title {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
  font-size: 24px;
}
</style>
