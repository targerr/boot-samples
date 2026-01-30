Vue3 + TypeScript 实战项目教程

### 项目概述
本教程从零搭建基于 Vite + Vue3 + TypeScript 的后台管理系统，集成 Element Plus、Vue Router、Pinia、Axios 核心技术栈，实现路由鉴权、状态持久化、网络请求封装、登录功能、商品 CRUD 等后台开发必备能力。


### 1. 创建基于 Vite 的 Vue3 + TS 项目
#### 1.1 项目初始化
```bash
# 创建项目（指定 vue-ts 模板）
npm create vite@latest springboot-ui -- --template vue-ts

# 进入项目目录
cd springboot-ui

# 安装依赖
npm install

# 启动开发服务器
npm run dev

```
#### 1.2 项目目录结构

```
springboot-ui/
├── node_modules/    # 项目依赖包
├── public/          # 静态资源（不会被打包）
├── src/             # 源代码目录
│   ├── api/         # 接口请求封装
│   ├── router/      # 路由配置
│   ├── store/       # Pinia 状态管理
│   ├── types/       # TypeScript 类型定义
│   ├── utils/       # 工具函数
│   ├── views/       # 页面级组件
│   ├── App.vue      # 根组件
│   └── main.ts      # 项目入口文件
├── index.html       # HTML入口
├── package.json     # 项目配置
└── vite.config.ts   # Vite 配置文件
```

#### 1.3 配置 vite.config.ts


- 配置路径别名
- 代理配置：配置后台接口请求路径

```typescript
import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import { resolve } from "path";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  // 路径别名配置
  resolve: {
    alias: {
      "@": resolve(__dirname, "src"), // @ 指向 src 目录
    },
  },
  // 开发服务器配置
  server: {
    port: 3000,         // 启动端口
    open: true,         // 自动打开浏览器
    cors: true,         // 允许跨域
    // 接口代理（解决跨域问题）
    proxy: {
      "/api": {
        target: "http://localhost:8083/blog", // 后端接口地址
        changeOrigin: true,                   // 开启跨域
        rewrite: (path) => path.replace(/^\/api/, ""), // 重写路径
      },
    },
  },
});

```


### 2.安装配置 Element Plus、Vue Router

#### 2.1 安装依赖包
```bash
# 安装 Element Plus 及图标库
npm install element-plus @element-plus/icons-vue

# 安装 Vue Router v4（适配 Vue3）
npm install vue-router@4
```

#### 2.2 配置入口文件 main.ts


```typescript
import { createApp } from "vue";
import App from "./App.vue";
import router from "@/router";
import pinia from "./store";
import ElementPlus from "element-plus";
import "element-plus/dist/index.css";
import locale from "element-plus/es/locale/lang/zh-cn"; // 中文语言包
import * as ElementPlusIconsVue from "@element-plus/icons-vue";

const app = createApp(App);

// 全局注册 Element Plus 所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component);
}

// 安装插件
app
  .use(pinia)
  .use(router)
  .use(ElementPlus, { locale })
  .mount("#app");

```

#### 2.3 配置路由（router/index.ts） index.ts，然后配置路由：
src/index.ts
```typescript

 import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import { useUserStore } from "@/store/user";

// 路由规则定义
const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    redirect: "/home", // 默认重定向到首页
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("@/views/Login.vue"), // 懒加载登录页
  },
  {
    path: "/",
    component: () => import("@/views/Layout.vue"), // 布局组件
    children: [
      {
        path: "home",
        name: "Home",
        component: () => import("@/views/Product.vue"), // 商品管理页
      },
      {
        path: "blog",
        name: "Blog",
        component: () => import("@/views/Blog.vue"), // 博客管理页
      },
    ],
  },
];

// 创建路由实例
const router = createRouter({
  history: createWebHistory(), // HTML5 历史模式
  routes,
});

// 路由守卫：登录鉴权
router.beforeEach(async (to, _from, next) => {
  const userStore = useUserStore();
  
  // 访问登录页的逻辑
  if (to.path === "/login") {
    userStore.isLoggedIn ? next("/home") : next();
  } 
  // 访问其他页面需要验证 token
  else {
    const token = localStorage.getItem("token");
    if (token) {
      try {
        // 可选：验证 token 有效性并获取用户信息
        // await userStore.getCurrentUser();
        next();
      } catch (error) {
        localStorage.removeItem("token");
        next("/login");
      }
    } else {
      next("/login");
    }
  }
});

export default router;

```

> 上面的例子中只配置了登录页面和主页的路由，路由守卫对登录和 token 进行了校验。



### 3. 安装配置 Pinia


#### 3.1 安装依赖包


```bash
# 安装 Pinia 及持久化插件
npm install pinia pinia-plugin-persistedstate
```

#### 3.2 配置 Pinia（store/index.ts）


```typescript
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

// 创建 Pinia 实例
const pinia = createPinia()

// 安装持久化插件（解决页面刷新状态丢失）
pinia.use(piniaPluginPersistedstate)

export default pinia
```



### 4. 安装配置 Axios

#### 4.1 安装 Axios

```
npm install axios

```

#### 4.2 封装 Axios（utils/axios.ts）

```typescript

import axios, {
  AxiosInstance,
  InternalAxiosRequestConfig,
  AxiosResponse,
} from "axios";
import { ElMessage } from "element-plus";

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: "/api",                // 基础请求路径
  timeout: 10000,                 // 请求超时时间
  headers: {
    "Content-Type": "application/json;charset=utf-8",
  },
});

// 请求拦截器：添加 token
service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["token"] = token;
    }
    return config;
  },
  (error: any) => {
    ElMessage.error("请求发送失败");
    return Promise.reject(error);
  }
);

// 响应拦截器：统一处理结果
service.interceptors.response.use(
  (response: AxiosResponse) => {
    const { code, message } = response.data;
    if (code === 200) {
      return response.data;
    } else {
      ElMessage.error(message || "请求失败");
      return Promise.reject(new Error(message || "Error"));
    }
  },
  (error: any) => {
    // 401 未授权：清除 token 并跳转登录页
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      ElMessage.error("登录已过期，请重新登录");
      window.location.href = "/login";
    } else {
      ElMessage.error(error.message || "服务器错误");
    }
    return Promise.reject(error);
  }
);

export default service;

```

### 5. 封装 HTTP 请求（api/index.ts）


在 /src/api 文件夹下面新建 index.ts 文件：

```typescript
import axios from "@/utils/axios";
import { ApiResponse, PageParams, PageResponse } from "@/types";

/**
 * 通用GET请求
 * @param url 请求地址
 * @param params 请求参数
 * @returns 响应数据
 */
export const get = async <T>(url: string, params?: any): Promise<T> => {
    console.log("url: ",url,"params: ", params)
    const res = await axios.get<ApiResponse<T>>(url, { params });
    return res.data.data;
};

/**
 * 通用POST请求
 * @param url 请求地址
 * @param data 请求体数据
 * @returns 响应数据
 */
export const post = async <T>(url: string, data?: any): Promise<T> => {
    const res = await axios.post<ApiResponse<T>>(url, data);
    return res.data.data;
};

/**
 * 通用PUT请求
 * @param url 请求地址
 * @param data 请求体数据
 * @returns 响应数据
 */
export const put = async <T>(url: string, data?: any): Promise<T> => {
    const res = await axios.put<ApiResponse<T>>(url, data);
    return res.data.data;
};

/**
 * 通用DELETE请求
 * @param url 请求地址
 * @param params 请求参数
 * @returns 响应数据
 */
export const del = async <T>(url: string, params?: any): Promise<T> => {
    const res = await axios.delete<ApiResponse<T>>(url, { params });
    return res.data.data;
};

/**
 * 分页请求
 * @param url 请求地址
 * @param params 分页参数
 * @returns 分页响应数据
 */
export const getPage = async <T>(
    url: string,
    params: PageParams
): Promise<PageResponse<T>> => {
    return get<PageResponse<T>>(url, params);
};

```

这里我们拿 get 请求举例，前面 axios 响应拦截器中返回的是 response，这里封装之后的 get 请求返回的是 res.data.data。如果后台返回的数据是:

```json
{code:200,data：{username:'hhh'},messeg:'请求成功'}
```
那么经过封装之后的 get 请求取到的就是：

```
{username:'hhh'}
```
### 6. TypeScript 类型定义（types/index.ts）


```typescript
// 用户类型
export interface User {
    id: number;
    username: string;
    name: string;
    age: number;
    email?: string;
    avatar?: string;
    role?: string[];
}

// API响应类型
export interface ApiResponse<T = any> {
    code: number;
    msg: string;
    data: T;
}
// 分页参数
export interface PageParams {
    current: number;
    size: number;
    [key: string]: any; // 允许其他查询参数
}

// 分页响应
export interface PageResponse<T = any> {
    records: T[];
    total: number;
    current: number;
    size: number;
    pages: number;
}

// 登录请求参数
export interface LoginParams {
    username: string;
    password: string;
}

// 登录响应
export interface LoginResponse {
    token: string;
    userInfo: User;
}

export interface UserState {
    userInfo: User | null;
    token: string | null;
    isLoggedIn: boolean;
}

// 示例：商品类型
export interface Product {
    id?: string;
    name: string;
    price: number;
    stock: number;
    description?: string;
}

// 博客类型
export interface Blog {
    id?: string;
    type: string;
    title: string;
    content: string;
}

```

### 7. 用户登录功能实现
#### 7.1 用户 API 封装（api/user.ts）

在 /src/api 文件夹下新建 user.ts 文件

```ts

import { get, post } from "./index";
import { LoginParams, LoginResponse, User } from "@/types";

/**
 * 用户登录
 * @param params 登录参数
 * @returns 登录结果
 */
export const userLogin = async (params: LoginParams) => {
    return post<LoginResponse>("/user/login", params);
};

/**
 * 获取当前用户信息
 * @returns 用户信息
 */
export const getCurrentUserInfo = async () => {
    return get<User>("/user/currentUserInfo");
};

```
#### 7.2 用户状态管理（store/user.ts）


src/story/user.ts
```typescript

import { defineStore } from "pinia";
import { LoginParams, UserState } from "@/types";
import { userLogin, getCurrentUserInfo } from "@/api/user";
import router from "@/router";
import { ElMessage } from "element-plus";

export const useUserStore = defineStore("user", {
  // 状态定义
  state: (): UserState => ({
    userInfo: null,
    token: null,
    isLoggedIn: false,
  }),

  // 计算属性
  getters: {
    getUsername: (state) => state.userInfo?.username || "",
  },

  // 方法
  actions: {
    // 登录
    async login(params: LoginParams) {
      try {
        const res = await userLogin(params);
        this.userInfo = res.userInfo;
        this.token = res.token;
        this.isLoggedIn = true;
        localStorage.setItem("token", res.token);
        ElMessage.success("登录成功");
        return true;
      } catch (error: any) {
        ElMessage.error(error.message || "登录失败");
        throw new Error(error.message);
      }
    },

    // 退出登录
    logout() {
      this.userInfo = null;
      this.token = null;
      this.isLoggedIn = false;
      localStorage.removeItem("token");
      localStorage.removeItem("user-store");
      router.push("/login");
      ElMessage.info("已退出登录");
    },

    // 获取当前用户信息
    async getCurrentUser() {
      try {
        const res = await getCurrentUserInfo();
        this.userInfo = res;
        this.isLoggedIn = true;
        return res;
      } catch (error) {
        console.error("获取用户信息失败:", error);
        this.logout();
        return null;
      }
    },
  },
  // 持久化配置
  persist: {
    key: "user-store",
    storage: localStorage,
    pick: ["userInfo", "token", "isLoggedIn"],
  },
});

```

#### 7.3 登录页面（views/Login.vue）

登录页面很简单，这里我们使用 el-card、el-form、el-button 完成登录页面的设计：

views/ Login.vue
```vue
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

```


###### 用户登录成功之后，userStore里面的数据被存储到了浏览器里面：

![image.png](https://upload-images.jianshu.io/upload_images/4994935-82b7d6b7d0060cee.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



### 8. 布局组件（views/Layout.vue）


```vue
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

```


9. 商品 CRUD 功能实现
   9.1 商品 API 封装（api/product.ts）


### 7 商品的增删改查

#### 7.1 创建商品相关的 API
在 /src/api 文件夹下面新建 product.ts 文件
```
import { get, post, del, getPage } from "./index";
import { Product, PageParams } from "@/types";

/**
 * 获取商品列表（分页）
 * @param params 分页查询参数
 * @returns 分页商品列表
 * 实际请求会被代理到后端 http://localhost:8083/zhifou-blog/product/page
 */
export const getProductList = (params: PageParams) => {
    return getPage<Product>("/product/page", params);
};

/**
 * 获取商品详情
 * @param id 商品ID
 * @returns 商品详情
 */
export const getProductDetail = (id: number) => {
    return get<Product>(`/product/info/${id}`);
};

/**
 * 新增/修改商品
 * @param data 商品数据
 * @returns
 */
export const createUpdateProduct = (data: Product) => {
    return post<Product>("/product/saveUpdate", data);
};

/**
 * 删除商品
 * @param id 商品ID
 * @returns 删除结果
 */
export const deleteProduct = (id: number) => {
    return del<{ success: boolean }>(`/product/delete/${id}`);
};

```

#### 7.2 新建views/product.ts

```vue
<template>
  <div class="product-management">
    <h1>商品管理</h1>

    <!-- 搜索区域 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="商品名称">
          <el-input
              v-model="searchForm.name"
              clearable
              @clear="handleSearch"
              placeholder="请输入商品名称"
              style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="handleAddProduct"> 添加商品</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 商品列表 -->
    <el-card class="table-card">
      <el-table :data="productList" border style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="55"/>
        <el-table-column prop="name" label="商品名称"></el-table-column>
        <el-table-column prop="price" label="价格">
          <template #default="scope"> ¥{{ scope.row.price.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存"></el-table-column>
        <el-table-column prop="createTime" label="创建时间"></el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
            :current-page="pageParams.current"
            :page-size="pageParams.size"
            :page-sizes="[10, 20, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </el-card>

    <!-- 商品表单弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="100px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="formData.name" placeholder="请输入商品名称"></el-input>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input
              v-model.number="formData.price"
              type="number"
              placeholder="请输入数字"
          ></el-input>
        </el-form-item>
        <el-form-item label="商品库存" prop="stock">
          <el-input
              v-model.number="formData.stock"
              type="number"
              min="0"
              placeholder="请输入数字"
          ></el-input>
        </el-form-item>
        <el-form-item label="商品描述" prop="description">
          <el-input v-model="formData.description" type="textarea" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="resetSubmit(formRef)">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive, onMounted} from "vue";
import {ElForm, FormInstance, ElMessage, ElMessageBox, FormRules} from "element-plus";
import {Product, PageParams} from "@/types";
import {getProductList, createUpdateProduct, deleteProduct} from "../api/product";


// 初始化时加载数据
onMounted(() => {
  fetchProductList();
});

// 加载状态
const loading = ref<boolean>(false);
// 商品列表数据
const productList = ref<Product[]>([]);
const total = ref<number>(0);
// 搜索表单
const searchForm = reactive({
  name: "",
});
// 分页参数
const pageParams = reactive<PageParams>({
  current: 1,
  size: 10,
  name: "",
});

// 表单弹窗状态
const dialogVisible = ref<boolean>(false);
const dialogTitle = ref<string>("添加商品");
const formRef = ref<FormInstance>();

// 表单数据
let formData = reactive<Product>({
  id: "",
  name: "",
  price: 0,
  stock: 0,
  description: "",
});

// 表单验证规则
const formRules = reactive<FormRules>({
  name: [
    {required: true, message: "请输入商品名称", trigger: "blur"},
    {max: 50, message: "商品名称不能超过50个字符", trigger: "blur"},
  ],
  price: [
    {required: true, message: "请输入商品价格", trigger: "blur"},
    {type: "number", min: 0, message: "商品价格必须大于等于0", trigger: "blur"},
  ],
  stock: [
    {required: true, message: "请输入商品库存", trigger: "blur"},
    {type: "number", min: 0, message: "商品库存必须大于等于0", trigger: "blur"},
  ],
});

// 获取商品列表
const fetchProductList = async () => {
  try {
    loading.value = true;
    const res = await getProductList(pageParams);
    productList.value = res.records;
    total.value = res.total;
  } catch (error) {
    ElMessage.error("获取商品列表失败");
  } finally {
    loading.value = false;
  }
};

// 搜索
const handleSearch = () => {
  pageParams.current = 1;
  pageParams.name = searchForm.name;
  fetchProductList();
};

// 重置搜索
const resetSearch = () => {
  pageParams.current = 1;
  pageParams.name = "";
  fetchProductList();
};

// 分页大小变化
const handleSizeChange = (size: number) => {
  pageParams.size = size;
  fetchProductList();
};

// 当前页变化
const handleCurrentChange = (page: number) => {
  pageParams.current = page;
  fetchProductList();
};

// 打开添加商品弹窗
const handleAddProduct = () => {
  dialogTitle.value = "添加商品";
  dialogVisible.value = true;
};
// 取消表单提交
const resetSubmit = (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  dialogVisible.value = false;
  formEl.resetFields();
  fetchProductList();
};

// 打开编辑商品弹窗
const handleEdit = (product: Product) => {
  dialogTitle.value = "修改商品";
  // 填充表单数据
  formData = product;
  dialogVisible.value = true;
};

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
    // 创建商品
    await createUpdateProduct(formData);
    ElMessage.success(`商品${formData.id ? "修改成功" : "添加成功"}`);
    // 重置提交
    resetSubmit(formRef.value);
  } catch (error: any) {
    if (error.message) {
      ElMessage.error(error.message);
    }
  }
};

// 删除商品
const handleDelete = async (id: number) => {
  try {
    const confirmResult = await ElMessageBox.confirm(
        "确定要删除这个商品吗？",
        "删除确认",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
    );
    if (confirmResult === "confirm") {
      await deleteProduct(id);
      ElMessage.success("商品删除成功");
      fetchProductList();
    }
  } catch (error: any) {
    // 如果是取消操作，不显示错误信息
    if (error != "cancel") {
      ElMessage.error("商品删除失败");
    }
  }
};
</script>

<style scoped>
.product-management {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-card {
  margin-bottom: 20px;
  padding: 15px;
}

.table-card {
  padding: 15px;
}

.pagination {
  margin-top: 15px;
  text-align: right;
}
</style>

```

### 10. 根组件（App.vue）

```vue
<template>
  <router-view />
</template>

<script setup lang="ts">
// 根组件仅作为路由出口
</script>

<style scoped>
/* 全局样式建议写在 src/assets/style.css 并在 main.ts 引入 */
</style>
```
