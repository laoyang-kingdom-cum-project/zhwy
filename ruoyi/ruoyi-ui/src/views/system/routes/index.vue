<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主键" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入主键"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="路线名称" prop="routeName">
        <el-input
          v-model="queryParams.routeName"
          placeholder="请输入路线名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="公里数" prop="distanceKm">
        <el-input
          v-model="queryParams.distanceKm"
          placeholder="请输入公里数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="跑过人数" prop="runners">
        <el-input
          v-model="queryParams.runners"
          placeholder="请输入跑过人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="起点距您公里数" prop="startDistKm">
        <el-input
          v-model="queryParams.startDistKm"
          placeholder="请输入起点距您公里数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="路线分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择路线分类" clearable>
          <el-option
            v-for="dict in dict.type.route_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="具体起点地址" prop="exactStartingAddress">
        <el-input
          v-model="queryParams.exactStartingAddress"
          placeholder="请输入具体起点地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:routes:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:routes:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:routes:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:routes:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="routesList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="路线名称" align="center" prop="routeName" />
      <el-table-column label="路线图片" align="center" prop="routeImg" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.routeImg" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="公里数" align="center" prop="distanceKm" />
      <el-table-column label="跑过人数" align="center" prop="runners" />
      <el-table-column label="起点距您公里数" align="center" prop="startDistKm" />
      <el-table-column label="路线分类" align="center" prop="category">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.route_category" :value="scope.row.category"/>
        </template>
      </el-table-column>
      <el-table-column label="具体起点地址" align="center" prop="exactStartingAddress" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:routes:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:routes:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改跑步路线对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="路线名称" prop="routeName">
          <el-input v-model="form.routeName" placeholder="请输入路线名称" />
        </el-form-item>
        <el-form-item label="路线图片" prop="routeImg">
          <image-upload v-model="form.routeImg"/>
        </el-form-item>
        <el-form-item label="公里数" prop="distanceKm">
          <el-input v-model="form.distanceKm" placeholder="请输入公里数" />
        </el-form-item>
        <el-form-item label="跑过人数" prop="runners">
          <el-input v-model="form.runners" placeholder="请输入跑过人数" />
        </el-form-item>
        <el-form-item label="起点距您公里数" prop="startDistKm">
          <el-input v-model="form.startDistKm" placeholder="请输入起点距您公里数" />
        </el-form-item>
        <el-form-item label="路线分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择路线分类">
            <el-option
              v-for="dict in dict.type.route_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="具体起点地址" prop="exactStartingAddress">
          <el-input v-model="form.exactStartingAddress" placeholder="请输入具体起点地址" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRoutes, getRoutes, delRoutes, addRoutes, updateRoutes } from "@/api/system/routes"

export default {
  name: "Routes",
  dicts: ['route_category'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 跑步路线表格数据
      routesList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        routeName: null,
        routeImg: null,
        distanceKm: null,
        runners: null,
        startDistKm: null,
        category: null,
        exactStartingAddress: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        routeName: [
          { required: true, message: "路线名称不能为空", trigger: "blur" }
        ],
        routeImg: [
          { required: true, message: "路线图片不能为空", trigger: "blur" }
        ],
        distanceKm: [
          { required: true, message: "公里数不能为空", trigger: "blur" }
        ],
        runners: [
          { required: true, message: "跑过人数不能为空", trigger: "blur" }
        ],
        startDistKm: [
          { required: true, message: "起点距您公里数不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "路线分类不能为空", trigger: "change" }
        ],
        exactStartingAddress: [
          { required: true, message: "具体起点地址不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询跑步路线列表 */
    getList() {
      this.loading = true
      listRoutes(this.queryParams).then(response => {
        this.routesList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        routeName: null,
        routeImg: null,
        distanceKm: null,
        runners: null,
        startDistKm: null,
        category: null,
        exactStartingAddress: null
      }
      this.resetForm("form")
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加跑步路线"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getRoutes(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改跑步路线"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateRoutes(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addRoutes(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除跑步路线编号为"' + ids + '"的数据项？').then(function() {
        return delRoutes(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/routes/export', {
        ...this.queryParams
      }, `routes_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
