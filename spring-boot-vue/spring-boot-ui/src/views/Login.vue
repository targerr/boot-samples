<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <el-form
          :model="loginForm"
          :rules="loginRules"
          ref="loginFormRef"
          class="login-form"
      >
        <el-form-item prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="请输入用户名"
              prefix-icon="User"
          ></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="请输入密码"
              prefix-icon="Lock"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
              type="primary"
              class="login-button"
              @click="handleLogin"
              :loading="loading"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from "element-plus";
import type { FormInstance, FormRules } from "element-plus";
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "../store/user";
import { LoginParams } from "../types";
// 路由实例
const router = useRouter();
// 用户状态管理
const userStore = useUserStore();
// 表单引用
const loginFormRef = ref<FormInstance>();
// 加载状态
const loading = ref<boolean>(false);

// 登录表单数据
const loginForm = reactive<LoginParams>({
  username: "",
  password: "",
});

// 表单验证规则
const loginRules = reactive<FormRules>({
  username: [{ required: true, message: "请输入用户名", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度不能少于6位", trigger: "blur" },
  ],
});

// 处理登录
const handleLogin = async () => {
  if (!loginFormRef.value) return;
  try {
    // 表单验证
    await loginFormRef.value.validate();
    // 显示加载状态
    loading.value = true;
    // 调用登录方法
    const success = await userStore.login(loginForm);
    if (success) {
      ElMessage.success("登录成功");
      // 跳转到首页
      router.push("/home");
    }
  } catch (error: any) {
    if (error.message) {
      ElMessage.error(error.message);
    }
  } finally {
    // 隐藏加载状态
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.login-card {
  width: 400px;
  padding: 20px;
}

.login-title {
  text-align: center;
  font-size: 24px;
  margin-bottom: 30px;
  color: #333;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
}
</style>
