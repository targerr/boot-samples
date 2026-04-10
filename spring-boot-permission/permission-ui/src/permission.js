/**
 * 自定义权限指令：v-permission
 * 类似于 Spring Security 的 @PreAuthorize("hasAuthority('user:delete')")
 *
 * Vue 指令是什么？
 * - 指令是带有 v- 前缀的特殊属性
 * - 常见内置指令：v-if、v-for、v-model、v-show
 * - 自定义指令：扩展 Vue 的功能
 *
 * 使用示例：
 * <el-button v-permission="7">删除</el-button>
 * 如果用户没有权限 ID 7，该按钮会被移除
 *
 * 指令生命周期钩子：
 * - created: 指令绑定到元素前
 * - beforeMount: 指令绑定到元素后
 * - mounted: 元素插入 DOM 后（本项目使用）
 * - beforeUpdate: 元素更新前
 * - updated: 元素更新后
 * - beforeUnmount: 元素卸载前
 * - unmounted: 元素卸载后
 */

/**
 * 设置权限指令
 * @param {object} app - Vue 应用实例
 *
 * 类似于 Spring 的 @Component 注册
 */
export const setupPermissionDirective = (app) => {
  /**
   * 注册自定义指令
   * 指令名：permission（使用时 v-permission）
   */
  app.directive('permission', {
    /**
     * mounted 钩子
     * 当指令绑定的元素被插入到 DOM 时调用
     *
     * @param {HTMLElement} el - 指令绑定的 DOM 元素
     * @param {object} binding - 指令绑定对象
     *   - value: 指令的值（v-permission="7" 中的 7）
     *   - oldValue: 旧值（更新时）
     *   - arg: 指令参数（v-permission:arg 中的 arg）
     *   - modifiers: 修饰符对象（v-permission.mod 中的 { mod: true }）
     */
    mounted(el, binding) {
      // 获取指令的值（权限 ID）
      const { value } = binding

      /**
       * 从 localStorage 获取用户权限列表
       * 权限列表格式：[1, 2, 3, 5, 7, ...]（权限点 ID 数组）
       * 由后端返回，登录成功后存储
       */
      const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')

      if (value) {
        /**
         * 检查用户是否拥有该权限
         * Array.includes(): 检查数组是否包含某元素
         * 类似于 Java 的 List.contains()
         */
        const hasPermission = permissions.includes(value)

        /**
         * 如果没有权限，从 DOM 中移除该元素
         * parentNode.removeChild(): 原生 DOM API
         *
         * 为什么不使用 v-if？
         * - v-if 是 Vue 的编译时指令，需要响应式数据
         * - v-permission 是运行时指令，更灵活
         * - 可以直接操作 DOM，性能更好
         */
        if (!hasPermission) {
          el.parentNode && el.parentNode.removeChild(el)  // 安全检查：parentNode 存在才移除
        }
      }
    }
  })
}

/**
 * 使用说明：
 *
 * 1. 在 main.js 中注册：
 *    import { setupPermissionDirective } from './permission'
 *    setupPermissionDirective(app)
 *
 * 2. 在模板中使用：
 *    <el-button v-permission="7">删除</el-button>
 *    <el-button v-permission="8">编辑</el-button>
 *
 * 3. 权限 ID 来源：
 *    - 后端 sys_acl 表的 id 字段
 *    - 用户登录后，后端根据用户角色返回权限 ID 列表
 *    - 前端存储到 localStorage.setItem('permissions', JSON.stringify([1,2,3,7]))
 *
 * 4. 与后端的对应关系：
 *    前端 v-permission="7" → 后端 @SaCheckPermission("system:user:delete")
 *    前端通过 ID 控制 UI 显示，后端通过 URL 控制接口访问
 */
