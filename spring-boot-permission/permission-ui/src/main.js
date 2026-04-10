/**
 * 应用入口文件
 * 类似于 Spring Boot 的 Application.java 或 main 方法
 *
 * Vue 3 应用启动流程：
 * 1. createApp() 创建应用实例
 * 2. 注册全局插件（UI库、路由、自定义指令）
 * 3. mount('#app') 挂载到 DOM 元素
 */

import { createApp } from 'vue'
import ElementPlus from 'element-plus'          // Element Plus UI 组件库（类似 Ant Design）
import 'element-plus/dist/index.css'           // Element Plus 样式
import zhCn from 'element-plus/es/locale/lang/zh-cn'  // 中文语言包
import * as ElementPlusIconsVue from '@element-plus/icons-vue'  // 图标库
import App from './App.vue'                    // 根组件（类似 React 的 App.jsx）
import router from './router'                  // 路由配置
import { setupPermissionDirective } from './permission'  // 自定义权限指令

// 创建 Vue 应用实例
const app = createApp(App)

/**
 * 注册所有 Element Plus 图标为全局组件
 * 注册后可以在模板中直接使用：<el-icon><House /></el-icon>
 *
 * 相当于：app.component('House', HouseComponent)
 */
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

/**
 * 注册插件
 * - Element Plus：UI 组件库，配置中文语言环境
 * - router：Vue Router，用于前端路由（类似于 Spring MVC 的 @RequestMapping）
 */
app.use(ElementPlus, { locale: zhCn })  // 使用中文界面
app.use(router)                        // 启用路由

/**
 * 注册自定义指令 v-permission
 * 用于权限控制：<el-button v-permission="7">删除</el-button>
 * 如果用户没有权限 7，该按钮会从 DOM 中移除
 */
setupPermissionDirective(app)

/**
 * 挂载应用到 DOM
 * #app 是 index.html 中的根元素：<div id="app"></div>
 * 类似于 React 的 ReactDOM.render()
 */
app.mount('#app')