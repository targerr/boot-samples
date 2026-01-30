<template>
  <div class="product-management">
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
