<!--
  用户管理页面组件
  类似于 Spring Boot 的 CRUD 页面

  功能：
  - 用户列表查询（分页、搜索、筛选）
  - 新增用户
  - 编辑用户
  - 删除用户
  - 修改用户状态
-->

<template>
  <div class="page-container">
    <!--
      搜索栏：el-card + el-form（inline 模式）
      - :inline="true": 表单项横向排列
      - v-model: 双向数据绑定（输入变化时 queryParams.keyword 自动更新）
      - clearable: 显示清除按钮
    -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="用户名/手机号/邮箱" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="正常" :value="1" />
            <el-option label="冻结" :value="0" />
            <el-option label="已删除" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!--
      操作栏 + 表格
      - el-table: 数据表格组件
      - v-loading: 加载状态（显示 loading 遮罩）
      - border/stripe: 表格样式
      - el-table-column: 表格列（prop 对应数据字段名）
    -->
    <el-card shadow="never" style="margin-top: 16px">
      <template #header>
        <!--
          v-permission: 自定义权限指令
          如果用户没有 'USER_ADD' 权限，该按钮会被移除
          类似于 Spring Security 的 @PreAuthorize("hasAuthority('USER_ADD')")
        -->
        <el-button v-permission="'USER_ADD'" type="primary" icon="Plus" @click="handleAdd">新增用户</el-button>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="telephone" label="手机号" width="130" />
        <el-table-column prop="mail" label="邮箱" width="160" />
        <el-table-column prop="deptName" label="部门" width="120" />

        <!--
          状态列：使用 el-switch 开关组件
          - :model-value: 绑定值（row.status === 1 时开启）
          - @change: 状态变化时触发 handleStatusChange
        -->
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-switch :model-value="row.status === 1" @change="(val) => handleStatusChange(row, val ? 1 : 0)" />
          </template>
        </el-table-column>

        <el-table-column prop="remark" label="备注" show-overflow-tooltip />

        <!-- 操作列：固定在右侧 -->
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'USER_EDIT'" type="primary" link icon="Edit" @click="handleEdit(row)">编辑</el-button>

            <!--
              el-popconfirm: 气泡确认框
              - title: 确认提示文本
              - @confirm: 确认后触发 handleDelete
              - #reference: 触发确认框的元素（删除按钮）
            -->
            <el-popconfirm title="确定删除?" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button v-permission="'USER_DELETE'" type="danger" link icon="Delete">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <!--
        分页组件
        - v-model:current-page: 当前页码（双向绑定）
        - v-model:page-size: 每页条数
        - :total: 总条数
        - @size-change/@current-change: 页码或每页条数变化时触发 fetchData
      -->
      <el-pagination style="margin-top: 16px; justify-content: flex-end"
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchData"
        @current-change="fetchData"
      />
    </el-card>

    <!--
      新增/编辑弹窗
      - v-model: 控制弹窗显示/隐藏
      - :title: 弹窗标题
      - destroy-on-close: 关闭时销毁弹窗内的组件（重新打开时重置状态）
    -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑用户' : '新增用户'" width="500px" destroy-on-close>
      <!--
        el-form: 表单组件
        - ref: 获取表单实例（用于调用 validate 方法）
        - :model: 绑定表单数据
        - :rules: 绑定验证规则
      -->
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="手机号" prop="telephone">
          <el-input v-model="form.telephone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="mail">
          <el-input v-model="form.mail" placeholder="请输入邮箱" />
        </el-form-item>

        <!--
          密码字段：编辑时非必填
          - :prop="isEdit ? '' : 'password'": 编辑时不绑定验证规则
          - placeholder: 动态提示文本
        -->
        <el-form-item label="密码" :prop="isEdit ? '' : 'password'">
          <el-input v-model="form.password" :placeholder="isEdit ? '不填则不修改' : '请输入密码'" type="password" show-password />
        </el-form-item>

        <el-form-item label="部门" prop="deptId">
          <el-select v-model="form.deptId" placeholder="请选择部门" clearable style="width: 100%">
            <!--
              v-for: 列表渲染
              - :key: 必须指定唯一的 key 值
              - :label: 选项显示文本
              - :value: 选项实际值
            -->
            <el-option
              v-for="dept in deptOptions"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>

      <!-- 弹窗底部按钮 -->
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
/**
 * Vue 3 组合式 API：<script setup>
 *
 * - ref: 创建响应式数据（基本类型、DOM 引用）
 * - reactive: 创建响应式对象（表单数据、查询参数）
 * - onMounted: 生命周期钩子（组件挂载后执行）
 */
import { ref, reactive, onMounted } from 'vue'
import { getUserPage, saveUser, updateUser, changeUserStatus, deleteUsers, getDeptTree } from '@/api/system'
import { ElMessage } from 'element-plus'

/**
 * ref: 创建响应式数据
 * - loading: 表格加载状态
 * - tableData: 表格数据数组
 * - total: 总条数
 * - dialogVisible: 弹窗显示状态
 * - isEdit: 是否编辑模式
 * - submitLoading: 提交按钮加载状态
 * - formRef: 表单 DOM 引用
 * - deptOptions: 部门下拉选项
 */
const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const deptOptions = ref([])

/**
 * reactive: 创建响应式对象
 * - queryParams: 查询参数
 * - form: 表单数据
 *
 * 使用场景：表单数据、配置对象等复杂数据结构
 * 区别：reactive 不需要 .value，直接访问属性
 */
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: '',
  status: null
})

const form = reactive({
  id: null,
  username: '',
  telephone: '',
  mail: '',
  password: '',
  deptId: null,
  remark: ''
})

/**
 * 表单验证规则
 * 类似于 Spring 的 @Valid + Hibernate Validator
 *
 * - required: 必填
 * - message: 错误提示
 * - trigger: 触发时机（blur/change）
 */
const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

/**
 * 加载部门列表
 * 后端返回树形结构，需要扁平化为下拉选项
 *
 * 递归函数：flattenDepts
 * 遍历树形结构，提取所有节点为一维数组
 */
const fetchDeptOptions = async () => {
  try {
    const depts = await getDeptTree()
    const flattenDepts = (list) => {
      const result = []
      list.forEach(dept => {
        result.push({ id: dept.id, name: dept.name })
        if (dept.children && dept.children.length > 0) {
          // 递归：展开运算符 ... 将子数组元素合并到 result
          result.push(...flattenDepts(dept.children))
        }
      })
      return result
    }
    deptOptions.value = flattenDepts(depts || [])
  } catch (e) {
    deptOptions.value = []
  }
}

/**
 * 获取用户列表（分页查询）
 * 调用后端 API：GET /system/user/page
 */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getUserPage(queryParams)
    tableData.value = res.data || []
    total.value = res.total || 0
  } finally {
    loading.value = false
  }
}

/**
 * 搜索：重置页码为第一页，然后获取数据
 */
const handleSearch = () => {
  queryParams.pageNum = 1
  fetchData()
}

/**
 * 重置：清空查询条件，重置页码
 */
const handleReset = () => {
  queryParams.keyword = ''
  queryParams.status = null
  queryParams.pageNum = 1
  fetchData()
}

/**
 * 新增用户：打开弹窗，重置表单
 * - Object.assign: 复制对象属性到 form
 */
const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    id: null,
    username: '',
    telephone: '',
    mail: '',
    password: '',
    deptId: null,
    remark: ''
  })
  dialogVisible.value = true
}

/**
 * 编辑用户：打开弹窗，填充表单数据
 */
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, {
    id: row.id,
    username: row.username,
    telephone: row.telephone,
    mail: row.mail,
    password: '',  // 密码不回显，用户可以不填表示不修改
    deptId: row.deptId,
    remark: row.remark
  })
  dialogVisible.value = true
}

/**
 * 提交表单（新增或编辑）
 * 1. 表单验证
 * 2. 调用 API（saveUser 或 updateUser）
 * 3. 关闭弹窗
 * 4. 刷新列表
 */
const handleSubmit = async () => {
  // 表单验证：如果验证失败，抛出异常
  await formRef.value.validate()

  submitLoading.value = true
  try {
    // 三元表达式：根据 isEdit 选择调用不同的 API
    isEdit.value ? await updateUser(form) : await saveUser(form)

    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    fetchData()  // 刷新列表
  } finally {
    submitLoading.value = false
  }
}

/**
 * 修改用户状态
 * 调用后端 API：PUT /system/user/changeStatus
 */
const handleStatusChange = async (row, status) => {
  try {
    await changeUserStatus(row.id, status)
    ElMessage.success('状态更新成功')
    fetchData()  // 刷新列表
  } catch (e) {}
}

/**
 * 删除用户
 * 调用后端 API：DELETE /system/user（请求体为 ID 数组）
 */
const handleDelete = async (id) => {
  try {
    await deleteUsers([id])  // 传递 ID 数组
    ElMessage.success('删除成功')
    fetchData()  // 刷新列表
  } catch (e) {}
}

/**
 * onMounted: 生命周期钩子
 * 组件挂载到 DOM 后执行（类似于 jQuery 的 $(document).ready()）
 */
onMounted(() => {
  fetchData()  // 获取用户列表
  fetchDeptOptions()  // 获取部门选项
})
</script>

<style scoped>
.page-container { padding: 0; }

/**
  :deep() - 深度选择器
  用于修改子组件或第三方组件的样式
  类似于 React 的 :global() 或 CSS 的 ::v-deep

  这里减少 el-card__body 的底部内边距
 */
.search-card :deep(.el-card__body) { padding-bottom: 0; }
</style>
