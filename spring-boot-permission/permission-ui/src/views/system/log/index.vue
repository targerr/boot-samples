<!--
  操作日志页面组件
  展示用户的操作记录，支持复原功能

  功能：
  - 日志列表查询（分页、搜索、筛选）
  - 按操作类型筛选
  - 按关键词搜索（操作人/目标ID）
  - 复原操作（将数据恢复到操作前的状态）
-->
<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="queryParams">
        <el-form-item label="操作类型">
          <el-select v-model="queryParams.type" placeholder="全部" clearable style="width: 140px">
            <el-option label="登录" :value="1" />
            <el-option label="登出" :value="2" />
            <el-option label="新增" :value="3" />
            <el-option label="更新" :value="4" />
            <el-option label="删除" :value="5" />
            <el-option label="冻结" :value="6" />
            <el-option label="其他" :value="7" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="操作人/目标ID" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleSearch">搜索</el-button>
          <el-button icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="never" style="margin-top: 16px">
      <el-table :data="tableData" v-loading="loading" border stripe>
        <el-table-column prop="id" label="ID" width="60" align="center" />

        <!-- 操作类型列：使用映射显示不同颜色的标签 -->
        <el-table-column prop="type" label="操作类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="logTypeTagMap[row.type] || 'info'" size="small">{{ row.typeDesc || logTypeNameMap[row.type] || row.type }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="targetId" label="目标ID" width="80" align="center" />
        <el-table-column prop="operator" label="操作人" width="120" />
        <el-table-column prop="operateTime" label="操作时间" width="180" />
        <el-table-column prop="operateIp" label="操作IP" width="140" />

        <!-- 状态列：已复原/未复原 -->
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">{{ row.status === 1 ? '已复原' : '未复原' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="detail" label="详情" show-overflow-tooltip />

        <!-- 操作列：复原按钮（仅未复原时显示） -->
        <el-table-column label="操作" width="100" align="center" fixed="right">
          <template #default="{ row }">
            <el-popconfirm
              v-if="row.status !== 1"
              title="确定复原该操作?"
              @confirm="handleRestore(row.id)"
            >
              <template #reference>
                <el-button type="warning" link icon="RefreshLeft">复原</el-button>
              </template>
            </el-popconfirm>
            <el-tag v-else type="success" size="small">已复原</el-tag>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getLogPage, restoreLog } from '@/api/system'
import { ElMessage } from 'element-plus'

/**
 * 类型映射：用于显示不同颜色的标签
 * - logTypeTagMap: 类型 → 标签颜色映射
 * - logTypeNameMap: 类型 → 类型名称映射
 */
const logTypeTagMap = { 1: '', 2: 'info', 3: 'success', 4: 'warning', 5: 'danger', 6: 'danger', 7: 'info' }
const logTypeNameMap = { 1: '登录', 2: '登出', 3: '新增', 4: '更新', 5: '删除', 6: '冻结', 7: '其他' }

const loading = ref(false)
const tableData = ref([])
const total = ref(0)

const queryParams = reactive({ pageNum: 1, pageSize: 10, type: null, keyword: '' })

/**
 * 获取日志列表
 */
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getLogPage(queryParams)
    tableData.value = res.data || []
    total.value = res.total || 0
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { queryParams.pageNum = 1; fetchData() }
const handleReset = () => { queryParams.type = null; queryParams.keyword = ''; queryParams.pageNum = 1; fetchData() }

/**
 * 复原操作
 * 调用后端 API：PUT /system/log/restore/{id}
 * 后端会根据日志记录的 old_value 将数据恢复到操作前的状态
 */
const handleRestore = async (id) => {
  try {
    await restoreLog(id)
    ElMessage.success('复原成功')
    fetchData()  // 刷新列表
  } catch (e) {}
}

onMounted(fetchData)
</script>

<style scoped>
.page-container { padding: 0; }
.search-card :deep(.el-card__body) { padding-bottom: 0; }
</style>
