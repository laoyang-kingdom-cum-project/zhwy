<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="主键ID" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入主键ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预约人用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入预约人用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="访客姓名" prop="visitorName">
        <el-input
          v-model="queryParams.visitorName"
          placeholder="请输入访客姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="访客手机号" prop="visitorPhone">
        <el-input
          v-model="queryParams.visitorPhone"
          placeholder="请输入访客手机号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="访客人数" prop="visitorCount">
        <el-input
          v-model="queryParams.visitorCount"
          placeholder="请输入访客人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="访问日期" prop="visitDate">
        <el-date-picker clearable
          v-model="queryParams.visitDate"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择访问日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="访问开始时间" prop="visitTimeStart">
        <el-date-picker clearable
          v-model="queryParams.visitTimeStart"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择访问开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="访问结束时间" prop="visitTimeEnd">
        <el-date-picker clearable
          v-model="queryParams.visitTimeEnd"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择访问结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="访问事由" prop="visitReason">
        <el-input
          v-model="queryParams.visitReason"
          placeholder="请输入访问事由"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-input
          v-model="queryParams.status"
          placeholder="请输入状态"
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
          v-hasPermi="['system:appointment:add']"
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
          v-hasPermi="['system:appointment:edit']"
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
          v-hasPermi="['system:appointment:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:appointment:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="appointmentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="预约人用户ID" align="center" prop="userId" />
      <el-table-column label="访客姓名" align="center" prop="visitorName" />
      <el-table-column label="访客手机号" align="center" prop="visitorPhone" />
      <el-table-column label="访客人数" align="center" prop="visitorCount" />
      <el-table-column label="访问日期" align="center" prop="visitDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="访问开始时间" align="center" prop="visitTimeStart" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitTimeStart, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="访问结束时间" align="center" prop="visitTimeEnd" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.visitTimeEnd, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="访问事由" align="center" prop="visitReason" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:appointment:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:appointment:remove']"
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

    <!-- 添加或修改访客预约对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="预约人用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入预约人用户ID" />
        </el-form-item>
        <el-form-item label="访客姓名" prop="visitorName">
          <el-input v-model="form.visitorName" placeholder="请输入访客姓名" />
        </el-form-item>
        <el-form-item label="访客手机号" prop="visitorPhone">
          <el-input v-model="form.visitorPhone" placeholder="请输入访客手机号" />
        </el-form-item>
        <el-form-item label="访客人数" prop="visitorCount">
          <el-input v-model="form.visitorCount" placeholder="请输入访客人数" />
        </el-form-item>
        <el-form-item label="访问日期" prop="visitDate">
          <el-date-picker clearable
            v-model="form.visitDate"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择访问日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="访问开始时间" prop="visitTimeStart">
          <el-date-picker clearable
            v-model="form.visitTimeStart"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择访问开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="访问结束时间" prop="visitTimeEnd">
          <el-date-picker clearable
            v-model="form.visitTimeEnd"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择访问结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="访问事由" prop="visitReason">
          <el-input v-model="form.visitReason" placeholder="请输入访问事由" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-input v-model="form.status" placeholder="请输入状态" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listAppointment, getAppointment, delAppointment, addAppointment, updateAppointment } from "@/api/system/appointment"

export default {
  name: "Appointment",
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
      // 访客预约表格数据
      appointmentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        userId: null,
        visitorName: null,
        visitorPhone: null,
        visitorCount: null,
        visitDate: null,
        visitTimeStart: null,
        visitTimeEnd: null,
        visitReason: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "预约人用户ID不能为空", trigger: "blur" }
        ],
        visitorName: [
          { required: true, message: "访客姓名不能为空", trigger: "blur" }
        ],
        visitorPhone: [
          { required: true, message: "访客手机号不能为空", trigger: "blur" }
        ],
        visitDate: [
          { required: true, message: "访问日期不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询访客预约列表 */
    getList() {
      this.loading = true
      listAppointment(this.queryParams).then(response => {
        this.appointmentList = response.rows
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
        userId: null,
        visitorName: null,
        visitorPhone: null,
        visitorCount: null,
        visitDate: null,
        visitTimeStart: null,
        visitTimeEnd: null,
        visitReason: null,
        status: null,
        remark: null,
        createTime: null,
        updateTime: null
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
      this.title = "添加访客预约"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getAppointment(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改访客预约"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateAppointment(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addAppointment(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除访客预约编号为"' + ids + '"的数据项？').then(function() {
        return delAppointment(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/appointment/export', {
        ...this.queryParams
      }, `appointment_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
