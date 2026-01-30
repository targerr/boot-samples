<!--
  Blog.vue - 博客管理页面
  功能：博客的增删改查操作
  包含：搜索区域、博客列表表格、分页、新增/编辑弹窗
-->
<template>
  <div class="blog-management">
    <!-- 搜索区域卡片 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <!-- 标题搜索输入框 -->
        <el-form-item label="标题">
          <el-input v-model="searchForm.title" clearable placeholder="请输入标题" @clear="handleSearch"></el-input>
        </el-form-item>
        <!-- 操作按钮组 -->
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="success" @click="handleAdd">添加博客</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 博客列表表格卡片 -->
    <el-card class="table-card">
      <!--
        el-table 表格组件
        :data - 绑定博客列表数据
        v-loading - 加载状态
      -->
      <el-table :data="blogList" border v-loading="loading">
        <el-table-column type="index" label="序号" width="55" />
        <el-table-column prop="type" label="类型" width="100" />
        <el-table-column prop="title" label="标题" />
        <!-- show-overflow-tooltip: 内容过长时显示省略号，鼠标悬停显示完整内容 -->
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <!-- 操作列：编辑和删除按钮 -->
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页组件 -->
      <div class="pagination">
        <el-pagination
            :current-page="pageParams.current"
            :page-size="pageParams.size"
            :total="total"
            layout="total, prev, pager, next"
            @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 新增/编辑博客弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <!--
        表单组件
        :model - 绑定表单数据
        :rules - 表单验证规则
        ref - 表单引用，用于调用验证方法
      -->
      <el-form :model="formData" :rules="formRules" ref="formRef" label-width="80px">
        <el-form-item label="类型" prop="type">
          <el-input v-model="formData.type" placeholder="请输入类型"></el-input>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="formData.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input v-model="formData.content" type="textarea" rows="5" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <!-- 弹窗底部按钮 -->
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
// ==================== 导入依赖 ====================
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox, FormInstance, FormRules } from "element-plus";
import { Blog, PageParams } from "@/types";
import { getBlogList, saveBlog, deleteBlog } from "@/api/blog";

// ==================== 响应式数据定义 ====================
// 加载状态
const loading = ref(false);
// 博客列表数据
const blogList = ref<Blog[]>([]);
// 数据总条数
const total = ref(0);
// 搜索表单数据
const searchForm = reactive({ title: "" });
// 分页参数：当前页、每页条数、搜索条件
const pageParams = reactive<PageParams>({ current: 1, size: 10, title: "" });

// ==================== 弹窗相关数据 ====================
// 弹窗显示状态
const dialogVisible = ref(false);
// 弹窗标题（添加/编辑）
const dialogTitle = ref("添加博客");
// 表单引用，用于调用验证方法
const formRef = ref<FormInstance>();
// 表单数据
const formData = reactive<Blog>({ type: "", title: "", content: "" });
// 表单验证规则
const formRules = reactive<FormRules>({
  type: [{ required: true, message: "请输入类型", trigger: "blur" }],
  title: [{ required: true, message: "请输入标题", trigger: "blur" }],
  content: [{ required: true, message: "请输入内容", trigger: "blur" }],
});

// ==================== 生命周期钩子 ====================
// 组件挂载时获取博客列表
onMounted(() => fetchBlogList());

// ==================== 方法定义 ====================
/**
 * 获取博客列表数据
 */
const fetchBlogList = async () => {
  loading.value = true;
  try {
    const res = await getBlogList(pageParams);
    blogList.value = res.records;
    total.value = res.total;
  } catch {
    ElMessage.error("获取博客列表失败");
  } finally {
    loading.value = false;
  }
};

/**
 * 搜索按钮点击事件
 */
const handleSearch = () => {
  pageParams.current = 1; // 重置到第一页
  pageParams.title = searchForm.title;
  fetchBlogList();
};

/**
 * 重置搜索条件
 */
const resetSearch = () => {
  searchForm.title = "";
  pageParams.title = "";
  pageParams.current = 1;
  fetchBlogList();
};

/**
 * 分页页码变化事件
 * @param page - 新的页码
 */
const handleCurrentChange = (page: number) => {
  pageParams.current = page;
  fetchBlogList();
};

/**
 * 打开添加博客弹窗
 */
const handleAdd = () => {
  dialogTitle.value = "添加博客";
  // 重置表单数据
  Object.assign(formData, { id: undefined, type: "", title: "", content: "" });
  dialogVisible.value = true;
};

/**
 * 打开编辑博客弹窗
 * @param row - 当前行的博客数据
 */
const handleEdit = (row: Blog) => {
  dialogTitle.value = "编辑博客";
  // 填充表单数据
  Object.assign(formData, row);
  dialogVisible.value = true;
};

/**
 * 提交表单（新增或编辑）
 */
const handleSubmit = async () => {
  if (!formRef.value) return;
  // 验证表单
  await formRef.value.validate();
  // 调用保存接口
  await saveBlog(formData);
  ElMessage.success(formData.id ? "修改成功" : "添加成功");
  dialogVisible.value = false;
  // 刷新列表
  fetchBlogList();
};

/**
 * 删除博客
 * @param id - 博客ID
 */
const handleDelete = async (id: string) => {
  // 弹出确认框
  await ElMessageBox.confirm("确定删除该博客吗？", "提示", { type: "warning" });
  // 调用删除接口
  await deleteBlog(id);
  ElMessage.success("删除成功");
  // 刷新列表
  fetchBlogList();
};
</script>

<style scoped>
/* 搜索卡片底部间距 */
.search-card { margin-bottom: 20px; }
/* 分页组件样式 */
.pagination { margin-top: 15px; text-align: right; }
</style>
