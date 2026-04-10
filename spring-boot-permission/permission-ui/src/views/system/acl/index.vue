<!--
  权限管理页面组件
  两栏布局：左侧权限模块树 + 右侧权限点列表

  功能：
  - 权限模块树管理（增删改）
  - 权限点列表管理（增删改）
  - 点击模块查看该模块下的权限点
-->
<template>
  <div class="page-container">
    <el-row :gutter="16">
      <!-- 权限模块树 -->
      <el-col :span="8">
        <el-card shadow="never">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>权限模块</span>
              <el-button type="primary" size="small" icon="Plus" @click="handleAddModule(null)">新增顶级模块</el-button>
            </div>
          </template>
          <el-tree
            ref="treeRef"
            :data="moduleTree"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            default-expand-all
            highlight-current
            class="compact-tree"
            @node-click="handleModuleClick"
          >
            <template #default="{ node, data }">
              <span style="flex: 1; display: flex; align-items: center; justify-content: space-between; padding-right: 8px;">
                <span>{{ node.label }}</span>
                <span>
                  <el-button type="primary" link size="small" icon="Plus" @click.stop="handleAddModule(data)" />
                  <el-button type="primary" link size="small" icon="Edit" @click.stop="handleEditModule(data)" />
                  <el-popconfirm title="确定删除?" @confirm="handleDeleteModule(data.id)">
                    <template #reference>
                      <el-button type="danger" link size="small" icon="Delete" @click.stop />
                    </template>
                  </el-popconfirm>
                </span>
              </span>
            </template>
          </el-tree>
        </el-card>
      </el-col>

      <!-- 权限点列表 -->
      <el-col :span="16">
        <el-card shadow="never">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>{{ currentModule ? `${currentModule.name} - 权限点` : '权限点' }}</span>
              <el-button type="primary" size="small" icon="Plus" @click="handleAddAcl" :disabled="!currentModule">新增权限点</el-button>
            </div>
          </template>

          <el-empty v-if="!currentModule" description="请选择左侧权限模块" />

          <div v-else class="acl-table-container">
            <el-table :data="aclList" v-loading="aclLoading" border stripe style="width: 100%">
              <el-table-column prop="id" label="ID" width="60" align="center" />
              <el-table-column prop="code" label="权限码" min-width="140" show-overflow-tooltip />
              <el-table-column prop="name" label="权限名称" min-width="120" show-overflow-tooltip />
              <el-table-column prop="url" label="URL" min-width="180" show-overflow-tooltip />
              <el-table-column prop="type" label="类型" width="80" align="center">
                <template #default="{ row }">
                  <el-tag :type="typeTagMap[row.type] || 'info'" size="small">{{ typeNameMap[row.type] || row.type }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="status" label="状态" width="80" align="center">
                <template #default="{ row }">
                  <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '正常' : '冻结' }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="seq" label="排序" width="60" align="center" />
              <el-table-column prop="remark" label="备注" min-width="120" show-overflow-tooltip />
              <el-table-column label="操作" width="140" align="center" fixed="right">
                <template #default="{ row }">
                  <el-button type="primary" link icon="Edit" @click="handleEditAcl(row)">编辑</el-button>
                  <el-popconfirm title="确定删除?" @confirm="handleDeleteAcl(row.id)">
                    <template #reference>
                      <el-button type="danger" link icon="Delete">删除</el-button>
                    </template>
                  </el-popconfirm>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 模块新增/编辑弹窗 -->
    <el-dialog v-model="moduleDialogVisible" :title="isModuleEdit ? '编辑模块' : '新增模块'" width="500px" destroy-on-close>
      <el-form ref="moduleFormRef" :model="moduleForm" :rules="moduleRules" label-width="80px">
        <el-form-item label="模块名称" prop="name">
          <el-input v-model="moduleForm.name" placeholder="请输入模块名称" />
        </el-form-item>
        <el-form-item label="上级模块">
          <el-input-number v-model="moduleForm.parentId" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="moduleForm.seq" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="moduleForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="moduleDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="moduleSubmitLoading" @click="handleModuleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 权限点新增/编辑弹窗 -->
    <el-dialog v-model="aclDialogVisible" :title="isAclEdit ? '编辑权限点' : '新增权限点'" width="500px" destroy-on-close>
      <el-form ref="aclFormRef" :model="aclForm" :rules="aclRules" label-width="80px">
        <el-form-item label="权限码" prop="code">
          <el-input v-model="aclForm.code" placeholder="请输入权限码" />
        </el-form-item>
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="aclForm.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="URL">
          <el-input v-model="aclForm.url" placeholder="请输入URL" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="aclForm.type" placeholder="请选择类型" style="width: 100%">
            <el-option label="菜单" :value="1" />
            <el-option label="按钮" :value="2" />
            <el-option label="其他" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="aclForm.seq" :min="0" style="width: 100%" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="aclForm.status">
            <el-radio :value="1">正常</el-radio>
            <el-radio :value="0">冻结</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="aclForm.remark" type="textarea" :rows="2" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="aclDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="aclSubmitLoading" @click="handleAclSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import {
  getAclModuleTree, saveAclModule, updateAclModule, deleteAclModule,
  getAclByModuleId, saveAcl, updateAcl, changeAclStatus
} from '@/api/system'
import { ElMessage } from 'element-plus'

// 类型映射：用于显示不同颜色的标签
const typeTagMap = { 1: '', 2: 'warning', 3: 'info' }
const typeNameMap = { 1: '菜单', 2: '按钮', 3: '其他' }

// 模块树相关
const moduleTree = ref([])
const currentModule = ref(null)
const treeRef = ref(null)

// 模块表单相关
const moduleDialogVisible = ref(false)
const isModuleEdit = ref(false)
const moduleSubmitLoading = ref(false)
const moduleFormRef = ref(null)
const moduleForm = reactive({ id: null, name: '', parentId: 0, seq: 0, remark: '' })
const moduleRules = { name: [{ required: true, message: '请输入模块名称', trigger: 'blur' }] }

// 权限点相关
const aclList = ref([])
const aclLoading = ref(false)
const aclDialogVisible = ref(false)
const isAclEdit = ref(false)
const aclSubmitLoading = ref(false)
const aclFormRef = ref(null)
const aclForm = reactive({ id: null, aclModuleId: null, code: '', name: '', url: '', type: 1, seq: 0, status: 1, remark: '' })
const aclRules = {
  code: [{ required: true, message: '请输入权限码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择类型', trigger: 'change' }]
}

/**
 * 获取模块树
 */
const fetchModuleTree = async () => {
  moduleTree.value = await getAclModuleTree()
}

/**
 * 获取权限点列表
 * @param {number} moduleId - 模块 ID
 */
const fetchAclList = async (moduleId) => {
  if (!moduleId) { aclList.value = []; return }
  aclLoading.value = true
  try {
    const list = await getAclByModuleId(moduleId)
    aclList.value = list || []
  } finally {
    aclLoading.value = false
  }
}

// 模块操作
const handleModuleClick = (data) => {
  currentModule.value = data
  fetchAclList(data.id)
}

const handleAddModule = (parent) => {
  isModuleEdit.value = false
  Object.assign(moduleForm, { id: null, name: '', parentId: parent?.id || 0, seq: 0, remark: '' })
  moduleDialogVisible.value = true
}

const handleEditModule = (data) => {
  isModuleEdit.value = true
  Object.assign(moduleForm, { id: data.id, name: data.name, parentId: data.parentId, seq: data.seq, remark: data.remark })
  moduleDialogVisible.value = true
}

const handleModuleSubmit = async () => {
  await moduleFormRef.value.validate()
  moduleSubmitLoading.value = true
  try {
    isModuleEdit.value ? await updateAclModule(moduleForm) : await saveAclModule(moduleForm)
    ElMessage.success('保存成功')
    moduleDialogVisible.value = false
    fetchModuleTree()
  } finally {
    moduleSubmitLoading.value = false
  }
}

const handleDeleteModule = async (id) => {
  try {
    await deleteAclModule(id)
    ElMessage.success('删除成功')
    if (currentModule.value?.id === id) { currentModule.value = null; aclList.value = [] }
    fetchModuleTree()
  } catch (e) {}
}

// 权限点操作
const handleAddAcl = () => {
  isAclEdit.value = false
  Object.assign(aclForm, { id: null, aclModuleId: currentModule.value.id, code: '', name: '', url: '', type: 1, seq: 0, status: 1, remark: '' })
  aclDialogVisible.value = true
}

const handleEditAcl = (row) => {
  isAclEdit.value = true
  Object.assign(aclForm, { id: row.id, aclModuleId: row.aclModuleId, code: row.code, name: row.name, url: row.url, type: row.type, seq: row.seq, status: row.status, remark: row.remark })
  aclDialogVisible.value = true
}

const handleAclSubmit = async () => {
  await aclFormRef.value.validate()
  aclSubmitLoading.value = true
  try {
    isAclEdit.value ? await updateAcl(aclForm) : await saveAcl(aclForm)
    ElMessage.success('保存成功')
    aclDialogVisible.value = false
    fetchAclList(currentModule.value.id)
  } finally {
    aclSubmitLoading.value = false
  }
}

const handleDeleteAcl = async (id) => {
  try {
    await changeAclStatus(id, 0)
    ElMessage.success('删除成功')
    fetchAclList(currentModule.value.id)
  } catch (e) {}
}

onMounted(fetchModuleTree)
</script>

<style scoped>
/* 自定义树节点高度 */
.compact-tree {
  --el-tree-node-content-height: 28px;
}
.acl-table-container {
  overflow-x: auto;
}
</style>
