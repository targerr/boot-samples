<!--
  角色管理页面组件
  类似于用户管理页面，但增加了"分配权限"和"分配用户"功能

  功能：
  - 角色列表查询（分页、搜索）
  - 新增/编辑/删除角色
  - 为角色分配权限点（树形选择）
  - 为角色分配用户（多选表格）
-->
<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="角色名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作栏 + 表格 -->
    <el-card shadow="never" style="margin-top: 16px">
      <template #header>
        <el-button type="primary" icon="Plus" @click="handleAdd">新增角色</el-button>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="name" label="角色名称" width="150" />
        <!-- 类型列：使用 el-tag 显示不同颜色的标签 -->
        <el-table-column prop="type" label="类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.type === 1 ? 'success' : 'warning'" size="small">{{ row.type === 1 ? '系统角色' : '自定义角色' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '正常' : '冻结' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <!-- 分配权限按钮 -->
            <el-button type="warning" link icon="Key" @click="handleAssignAcl(row)">分配权限</el-button>
            <!-- 分配用户按钮 -->
            <el-button type="success" link icon="User" @click="handleAssignUser(row)">分配用户</el-button>
            <el-popconfirm title="确定删除?" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button type="danger" link icon="Delete">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

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

    <!-- 新增/编辑角色弹窗 -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑角色' : '新增角色'" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="系统角色" :value="1" />
            <el-option label="自定义角色" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="0">冻结</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配权限弹窗：使用树形组件展示权限模块和权限点 -->
    <el-dialog v-model="aclDialogVisible" :title="`分配权限 - ${currentRole?.name || ''}`" width="500px" destroy-on-close>
      <div style="max-height: 400px; overflow-y: auto;">
        <!--
          el-tree: 树形组件
          - show-checkbox: 显示复选框（支持多选）
          - check-strictly: false 表示父子节点关联选中
        -->
        <el-tree
          ref="aclTreeRef"
          :data="aclModuleTree"
          :props="{ label: 'name', children: 'children' }"
          node-key="id"
          show-checkbox
          default-expand-all
          :check-strictly="false"
        >
          <template #default="{ data }">
            <span>{{ data.name }}</span>
          </template>
        </el-tree>
      </div>
      <template #footer>
        <el-button @click="aclDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="aclSubmitLoading" @click="handleAclSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 分配用户弹窗：使用表格多选 -->
    <el-dialog v-model="userDialogVisible" :title="`分配用户 - ${currentRole?.name || ''}`" width="600px" destroy-on-close>
      <!--
        el-table: 表格组件
        - type="selection": 复选框列
        - @selection-change: 选中项变化时触发
      -->
      <el-table ref="userTableRef" :data="allUsers" v-loading="userLoading" border stripe max-height="400" @selection-change="handleUserSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="ID" width="60" align="center" />
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="telephone" label="手机号" width="130" />
        <el-table-column prop="mail" label="邮箱" show-overflow-tooltip />
      </el-table>
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="userSubmitLoading" @click="handleUserSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, nextTick, onMounted } from 'vue'
import {
  getRolePage, saveRole, updateRole, changeRoleStatus,
  getRoleAclIds, assignRoleAcls,
  getRoleUserIds, assignRoleUsers, getUserPage,
  getAclModuleTree
} from '@/api/system'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)

const queryParams = reactive({ pageNum: 1, pageSize: 10, keyword: '' })
const form = reactive({ id: null, name: '', type: 2, status: 1, remark: '' })
const rules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
}

// 分配权限相关
const aclDialogVisible = ref(false)
const aclSubmitLoading = ref(false)
const aclTreeRef = ref(null)
const aclModuleTree = ref([])
const currentRole = ref(null)

// 分配用户相关
const userDialogVisible = ref(false)
const userLoading = ref(false)
const userSubmitLoading = ref(false)
const userTableRef = ref(null)
const allUsers = ref([])
const currentUserIds = ref([])
const selectedUserIds = ref([])

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getRolePage(queryParams)
    tableData.value = res.data || []
    total.value = res.total || 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { queryParams.pageNum = 1; fetchData() }
const handleReset = () => { queryParams.keyword = ''; queryParams.pageNum = 1; fetchData() }

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, { id: null, name: '', type: 2, status: 1, remark: '' })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, { id: row.id, name: row.name, type: row.type, status: row.status, remark: row.remark })
  dialogVisible.value = true
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    isEdit.value ? await updateRole(form) : await saveRole(form)
    ElMessage.success(isEdit.value ? '更新成功' : '新增成功')
    dialogVisible.value = false
    fetchData()
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (id) => {
  try {
    await changeRoleStatus(id, 0)
    ElMessage.success('删除成功')
    fetchData()
  } catch (e) {}
}

/**
 * 将权限点合并到模块树中
 * 后端分别返回模块树和权限点，需要合并成一个树供 el-tree 使用
 */
const buildAclTree = (modules) => {
  return modules.map(module => {
    const node = {
      id: module.id,
      name: module.name,
      type: 'module',
      children: []
    }

    // 递归处理子模块
    if (module.children && module.children.length > 0) {
      node.children.push(...buildAclTree(module.children))
    }

    // 将权限点作为子节点添加
    if (module.aclList && module.aclList.length > 0) {
      module.aclList.forEach(acl => {
        node.children.push({
          id: acl.id,
          name: acl.name,
          type: 'acl'
        })
      })
    }

    return node
  })
}

/**
 * 分配权限：打开弹窗，加载权限树，设置已选中的权限
 */
const handleAssignAcl = async (row) => {
  currentRole.value = row
  const rawTree = await getAclModuleTree()
  aclModuleTree.value = buildAclTree(rawTree)
  aclDialogVisible.value = true

  // nextTick: 等待 DOM 更新后再执行（确保 el-tree 已渲染）
  await nextTick()
  try {
    const aclIds = await getRoleAclIds(row.id)
    // setCheckedKeys: 设置树形组件的选中项
    aclTreeRef.value?.setCheckedKeys(aclIds || [])
  } catch (e) {
    aclTreeRef.value?.setCheckedKeys([])
  }
}

const handleAclSubmit = async () => {
  // getCheckedKeys: 获取树形组件的选中项
  const checkedKeys = aclTreeRef.value?.getCheckedKeys(false) || []
  aclSubmitLoading.value = true
  try {
    await assignRoleAcls({ roleId: currentRole.value.id, aclIds: checkedKeys })
    ElMessage.success('分配权限成功')
    aclDialogVisible.value = false
  } finally {
    aclSubmitLoading.value = false
  }
}

/**
 * 分配用户：打开弹窗，加载用户列表，设置已选中的用户
 */
const handleAssignUser = async (row) => {
  currentRole.value = row
  userDialogVisible.value = true
  userLoading.value = true
  selectedUserIds.value = []
  currentUserIds.value = []
  allUsers.value = []

  try {
    // 先获取用户列表
    const usersRes = await getUserPage({ pageNum: 1, pageSize: 1000 })
    allUsers.value = usersRes?.data || []

    // 再获取已绑定的用户ID
    const userIds = await getRoleUserIds(row.id)
    currentUserIds.value = userIds || []

    // 等待 DOM 更新后设置选中状态
    await nextTick()
    if (userTableRef.value && allUsers.value.length > 0) {
      userTableRef.value.clearSelection()
      allUsers.value.forEach(user => {
        if (currentUserIds.value.includes(user.id)) {
          // toggleRowSelection: 设置表格行的选中状态
          userTableRef.value.toggleRowSelection(user, true)
        }
      })
    }
  } catch (error) {
    ElMessage.error('加载用户数据失败: ' + (error.message || '未知错误'))
  } finally {
    userLoading.value = false
  }
}

/**
 * 表格选中项变化时更新选中的用户ID列表
 */
const handleUserSelectionChange = (selection) => {
  selectedUserIds.value = selection.map(u => u.id)
}

const handleUserSubmit = async () => {
  userSubmitLoading.value = true
  try {
    await assignRoleUsers({ roleId: currentRole.value.id, userIds: selectedUserIds.value })
    ElMessage.success('分配用户成功')
    userDialogVisible.value = false
  } finally {
    userSubmitLoading.value = false
  }
}

onMounted(fetchData)
</script>

<style scoped>
.page-container { padding: 0; }
.search-card :deep(.el-card__body) { padding-bottom: 0; }
</style>
