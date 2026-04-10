<!--
  代码生成器页面组件
  根据数据库表结构自动生成前后端代码

  功能：
  - 展示数据库表列表
  - 选择表并配置生成参数
  - 调用后端生成代码
-->
<template>
  <div class="page-container">
    <!-- 表格 -->
    <el-card shadow="never">
      <template #header>
        <span>数据库表列表</span>
      </template>

      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="tableName" label="表名" width="200" />
        <el-table-column prop="tableComment" label="表注释" />
        <el-table-column prop="engine" label="存储引擎" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link icon="Cpu" @click="handleGenerate(row)">生成代码</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 生成配置弹窗 -->
    <el-dialog v-model="dialogVisible" :title="`生成代码 - ${currentTable?.tableName || ''}`" width="500px" destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="表名">
          <!-- :model-value: 单向绑定（只显示，不允许编辑） -->
          <el-input :model-value="currentTable?.tableName" disabled />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者名称" />
        </el-form-item>
        <el-form-item label="模块名" prop="moduleName">
          <el-input v-model="form.moduleName" placeholder="请输入模块名" />
        </el-form-item>
        <el-form-item label="表前缀">
          <el-input v-model="form.tablePrefix" placeholder="请输入表前缀(如 sys_)" />
        </el-form-item>
        <el-form-item label="去除前缀">
          <el-switch v-model="form.removePrefix" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">生成</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getTables, generateCode } from '@/api/generator'
import { ElMessage } from 'element-plus'

const loading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const submitLoading = ref(false)
const formRef = ref(null)
const currentTable = ref(null)

const form = reactive({ author: '', moduleName: '', tablePrefix: '', removePrefix: true })
const rules = {
  author: [{ required: true, message: '请输入作者名称', trigger: 'blur' }],
  moduleName: [{ required: true, message: '请输入模块名', trigger: 'blur' }]
}

/**
 * 获取数据库表列表
 */
const fetchData = async () => {
  loading.value = true
  try {
    tableData.value = await getTables()
  } finally {
    loading.value = false
  }
}

/**
 * 打开生成配置弹窗
 * 自动检测表前缀（例如：sys_user → 前缀为 sys_）
 */
const handleGenerate = (row) => {
  currentTable.value = row
  // 自动检测表前缀
  const parts = row.tableName.split('_')
  form.tablePrefix = parts.length > 1 ? parts[0] + '_' : ''
  form.moduleName = ''
  form.author = ''
  form.removePrefix = true
  dialogVisible.value = true
}

/**
 * 提交生成请求
 * 调用后端 API：POST /generator/generate
 * 后端使用 MyBatis-Plus Generator + Velocity 模板生成代码
 */
const handleSubmit = async () => {
  await formRef.value.validate()
  submitLoading.value = true
  try {
    await generateCode({
      tableName: currentTable.value.tableName,
      author: form.author,
      moduleName: form.moduleName,
      tablePrefix: form.tablePrefix,
      removePrefix: form.removePrefix
    })
    ElMessage.success('代码生成成功')
    dialogVisible.value = false
  } finally {
    submitLoading.value = false
  }
}

onMounted(fetchData)
</script>

<style scoped>
.page-container { padding: 0; }
</style>
