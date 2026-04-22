<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="id" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input
          v-model="queryParams.age"
          placeholder="请输入年龄"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="住址" prop="room">
        <el-input
          v-model="queryParams.room"
          placeholder="请输入住址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="healthStatus">
        <el-select v-model="queryParams.healthStatus" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.family_health_zt"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="最后活动" prop="lastActive">
        <el-date-picker clearable
          v-model="queryParams.lastActive"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择最后活动">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="紧急联系人+手机号" prop="emergencyContact">
        <el-input
          v-model="queryParams.emergencyContact"
          placeholder="请输入紧急联系人+手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="步数" prop="steps">
        <el-input
          v-model="queryParams.steps"
          placeholder="请输入步数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="睡眠时间" prop="sleep">
        <el-input
          v-model="queryParams.sleep"
          placeholder="请输入睡眠时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="心率" prop="heartRate">
        <el-input
          v-model="queryParams.heartRate"
          placeholder="请输入心率"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="血压" prop="pressure">
        <el-input
          v-model="queryParams.pressure"
          placeholder="请输入血压"
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
          v-hasPermi="['system:health:add']"
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
          v-hasPermi="['system:health:edit']"
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
          v-hasPermi="['system:health:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:health:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="healthList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="id" align="center" prop="id" />
      <el-table-column label="名称" align="center" prop="name" />
      <el-table-column label="年龄" align="center" prop="age" />
      <el-table-column label="住址" align="center" prop="room" />
      <el-table-column label="状态" align="center" prop="healthStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.family_health_zt" :value="scope.row.healthStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="最后活动" align="center" prop="lastActive" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.lastActive, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="紧急联系人+手机号" align="center" prop="emergencyContact" />
      <el-table-column label="步数" align="center" prop="steps" />
      <el-table-column label="睡眠时间" align="center" prop="sleep" />
      <el-table-column label="心率" align="center" prop="heartRate" />
      <el-table-column label="血压" align="center" prop="pressure" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:health:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:health:remove']"
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

    <!-- 添加或修改家人健康对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input v-model="form.age" placeholder="请输入年龄" />
        </el-form-item>
        <el-form-item label="住址" prop="room">
          <el-input v-model="form.room" placeholder="请输入住址" />
        </el-form-item>
        <el-form-item label="状态" prop="healthStatus">
          <el-radio-group v-model="form.healthStatus">
            <el-radio
              v-for="dict in dict.type.family_health_zt"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="最后活动" prop="lastActive">
          <el-date-picker clearable
            v-model="form.lastActive"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择最后活动">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="紧急联系人+手机号" prop="emergencyContact">
          <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人+手机号" />
        </el-form-item>
        <el-form-item label="步数" prop="steps">
          <el-input v-model="form.steps" placeholder="请输入步数" />
        </el-form-item>
        <el-form-item label="睡眠时间" prop="sleep">
          <el-input v-model="form.sleep" placeholder="请输入睡眠时间" />
        </el-form-item>
        <el-form-item label="心率" prop="heartRate">
          <el-input v-model="form.heartRate" placeholder="请输入心率" />
        </el-form-item>
        <el-form-item label="血压" prop="pressure">
          <el-input v-model="form.pressure" placeholder="请输入血压" />
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
import { listHealth, getHealth, delHealth, addHealth, updateHealth } from "@/api/system/health"

export default {
  name: "Health",
  dicts: ['family_health_zt'],
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
      // 家人健康表格数据
      healthList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        name: null,
        age: null,
        room: null,
        healthStatus: null,
        lastActive: null,
        emergencyContact: null,
        steps: null,
        sleep: null,
        heartRate: null,
        pressure: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询家人健康列表 */
    getList() {
      this.loading = true
      listHealth(this.queryParams).then(response => {
        this.healthList = response.rows
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
        name: null,
        age: null,
        room: null,
        healthStatus: null,
        lastActive: null,
        emergencyContact: null,
        steps: null,
        sleep: null,
        heartRate: null,
        pressure: null
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
      this.title = "添加家人健康"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getHealth(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改家人健康"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateHealth(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addHealth(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除家人健康编号为"' + ids + '"的数据项？').then(function() {
        return delHealth(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/health/export', {
        ...this.queryParams
      }, `health_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
