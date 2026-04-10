<!--
  部门管理页面组件
  树形结构管理：左侧部门树 + 右侧详情/编辑

  功能：
  - 部门树展示（支持增删改）
  - 部门详情查看
  - 新增/编辑部门
  - 删除部门
-->
<template>
  <div class="page-container">
    <el-row :gutter="16">
      <!-- 部门树 -->
      <el-col :span="10">
        <el-card shadow="never">
          <template #header>
            <div style="display: flex; justify-content: space-between; align-items: center;">
              <span>部门列表</span>
              <el-button type="primary" size="small" icon="Plus" @click="handleAdd(null)">新增顶级部门</el-button>
            </div>
          </template>
          <!--
            el-tree: 树形组件
            - :data: 树数据（嵌套结构）
            - :props: 配置项（label 显示字段，children 子节点字段）
            - node-key: 节点唯一标识
            - @node-click: 节点点击事件
          -->
          <el-tree
            ref="treeRef"
            :data="treeData"
            :props="{ label: 'name', children: 'children' }"
            node-key="id"
            default-expand-all
            highlight-current
            @node-click="handleNodeClick"
          >
            <!-- 自定义节点内容：节点名称 + 操作按钮 -->
            <template #default="{ node, data }">
              <span style="flex: 1; display: flex; align-items: center; justify-content: space-between; padding-right: 8px;">
                <span>{{ node.label }}</span>
                <span>
                  <!-- @click.stop: 阻止事件冒泡（不触发父节点的点击事件） -->
                  <el-button type="primary" link size="small" icon="Plus" @click.stop="handleAdd(data)" />
                  <el-button type="primary" link size="small" icon="Edit" @click.stop="handleEdit(data)" />
                  <el-popconfirm title="确定删除?" @confirm="handleDelete(data.id)">
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

      <!-- 部门详情/编辑 -->
      <el-col :span="14">
        <el-card shadow="never">
          <template #header>{{ currentDept ? (isEditing ? '编辑部门' : '部门详情') : '部门详情' }}</template>

          <!-- 详情展示：el-descriptions 描述列表组件 -->
          <div v-if="!isEditing && currentDept">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="部门ID">{{ currentDept.id }}</el-descriptions-item>
              <el-descriptions-item label="部门名称">{{ currentDept.name }}</el-descriptions-item>
              <el-descriptions-item label="上级部门ID">{{ currentDept.parentId }}</el-descriptions-item>
              <el-descriptions-item label="层级">{{ currentDept.level }}</el-descriptions-item>
              <el-descriptions-item label="排序">{{ currentDept.seq }}</el-descriptions-item>
              <el-descriptions-item label="备注">{{ currentDept.remark }}</el-descriptions-item>
            </el-descriptions>
          </div>

          <!-- 编辑表单 -->
          <el-form v-else-if="isEditing" ref="formRef" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入部门名称" />
            </el-form-item>
            <el-form-item label="上级部门">
              <el-select v-model="form.parentId" placeholder="请选择上级部门" clearable style="width: 100%">
                <el-option label="无（顶级部门）" :value="0" />
                <!-- :disabled: 禁用选择自己作为上级部门（避免循环引用） -->
                <el-option v-for="dept in deptOptions" :key="dept.id" :label="dept.name" :value="dept.id" :disabled="form.id && dept.id === form.id" />
              </el-select>
            </el-form-item>
            <el-form-item label="排序">
              <el-input-number v-model="form.seq" :min="0" style="width: 100%" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" :rows="2" />
            </el-form-item>
            <el-form-item>
              <el-button @click="isEditing = false">取消</el-button>
              <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
            </el-form-item>
          </el-form>

          <!-- 空状态提示 -->
          <el-empty v-else description="请选择左侧部门" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getDeptTree, saveDept, updateDept, deleteDept } from '@/api/system'
import { ElMessage } from 'element-plus'

const treeData = ref([])
const deptOptions = ref([])
const currentDept = ref(null)
const isEditing = ref(false)
const isEdit = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const treeRef = ref(null)

const form = reactive({ id: null, name: '', parentId: 0, seq: 0, remark: '' })
const rules = { name: [{ required: true, message: '请输入部门名称', trigger: 'blur' }] }

/**
 * 获取部门树
 */
const fetchTree = async () => {
  treeData.value = await getDeptTree()
  deptOptions.value = flattenDeptTree(treeData.value)
}

/**
 * 扁平化部门树为下拉选项
 * 递归函数：将嵌套的树形结构转换为一维数组
 * 例如：[{id:1,name:'总部',path:'总部'}, {id:2,name:'技术部',path:'总部/技术部'}]
 */
const flattenDeptTree = (tree, prefix = '') => {
  const result = []
  tree.forEach(dept => {
    result.push({ id: dept.id, name: prefix + dept.name })
    if (dept.children && dept.children.length > 0) {
      result.push(...flattenDeptTree(dept.children, prefix + dept.name + '/'))
    }
  })
  return result
}

const handleNodeClick = (data) => {
  currentDept.value = data
  isEditing.value = false
}

const handleAdd = (parent) => {
  isEdit.value = false
  isEditing.value = true
  Object.assign(form, { id: null, name: '', parentId: parent?.id || 0, seq: 0, remark: '' })
}

const handleEdit = (data) => {
  isEdit.value = true
  isEditing.value = true
  Object.assign(form, { id: data.id, name: data.name, parentId: data.parentId, seq: data.seq, remark: data.remark })
}

const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    isEdit.value ? await updateDept(form) : await saveDept(form)
    ElMessage.success('保存成功')
    isEditing.value = false
    fetchTree()
  } finally {
    submitLoading.value = false
  }
}

const handleDelete = async (id) => {
  try {
    await deleteDept(id)
    ElMessage.success('删除成功')
    if (currentDept.value?.id === id) { currentDept.value = null }
    fetchTree()
  } catch (e) {}
}

onMounted(fetchTree)
</script>
