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
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单编号" prop="billNo">
        <el-input
          v-model="queryParams.billNo"
          placeholder="请输入账单编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单名称" prop="billName">
        <el-input
          v-model="queryParams.billName"
          placeholder="请输入账单名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费" prop="billType">
        <el-input
          v-model="queryParams.billType"
          placeholder="请输入账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="金额" prop="amount">
        <el-input
          v-model="queryParams.amount"
          placeholder="请输入金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态：0未缴费，1已缴费" prop="status">
        <el-input
          v-model="queryParams.status"
          placeholder="请输入状态：0未缴费，1已缴费"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="缴费时间" prop="payTime">
        <el-date-picker clearable
          v-model="queryParams.payTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择缴费时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="支付方式：wechat微信支付，alipay支付宝" prop="payMethod">
        <el-input
          v-model="queryParams.payMethod"
          placeholder="请输入支付方式：wechat微信支付，alipay支付宝"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账单周期" prop="billPeriod">
        <el-input
          v-model="queryParams.billPeriod"
          placeholder="请输入账单周期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="截止日期" prop="dueDate">
        <el-date-picker clearable
          v-model="queryParams.dueDate"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择截止日期">
        </el-date-picker>
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
          v-hasPermi="['system:bill:add']"
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
          v-hasPermi="['system:bill:edit']"
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
          v-hasPermi="['system:bill:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:bill:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="billList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="账单编号" align="center" prop="billNo" />
      <el-table-column label="账单名称" align="center" prop="billName" />
      <el-table-column label="账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费" align="center" prop="billType" />
      <el-table-column label="金额" align="center" prop="amount" />
      <el-table-column label="状态：0未缴费，1已缴费" align="center" prop="status" />
      <el-table-column label="缴费时间" align="center" prop="payTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.payTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付方式：wechat微信支付，alipay支付宝" align="center" prop="payMethod" />
      <el-table-column label="账单周期" align="center" prop="billPeriod" />
      <el-table-column label="截止日期" align="center" prop="dueDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.dueDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:bill:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:bill:remove']"
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

    <!-- 添加或修改缴费账单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="账单编号" prop="billNo">
          <el-input v-model="form.billNo" placeholder="请输入账单编号" />
        </el-form-item>
        <el-form-item label="账单名称" prop="billName">
          <el-input v-model="form.billName" placeholder="请输入账单名称" />
        </el-form-item>
        <el-form-item label="账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费" prop="billType">
          <el-input v-model="form.billType" placeholder="请输入账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入金额" />
        </el-form-item>
        <el-form-item label="状态：0未缴费，1已缴费" prop="status">
          <el-input v-model="form.status" placeholder="请输入状态：0未缴费，1已缴费" />
        </el-form-item>
        <el-form-item label="缴费时间" prop="payTime">
          <el-date-picker clearable
            v-model="form.payTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择缴费时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="支付方式：wechat微信支付，alipay支付宝" prop="payMethod">
          <el-input v-model="form.payMethod" placeholder="请输入支付方式：wechat微信支付，alipay支付宝" />
        </el-form-item>
        <el-form-item label="账单周期" prop="billPeriod">
          <el-input v-model="form.billPeriod" placeholder="请输入账单周期" />
        </el-form-item>
        <el-form-item label="截止日期" prop="dueDate">
          <el-date-picker clearable
            v-model="form.dueDate"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择截止日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="删除标志" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
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
import { listBill, getBill, delBill, addBill, updateBill } from "@/api/system/bill"

export default {
  name: "Bill",
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
      // 缴费账单表格数据
      billList: [],
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
        billNo: null,
        billName: null,
        billType: null,
        amount: null,
        status: null,
        payTime: null,
        payMethod: null,
        billPeriod: null,
        dueDate: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        billNo: [
          { required: true, message: "账单编号不能为空", trigger: "blur" }
        ],
        billName: [
          { required: true, message: "账单名称不能为空", trigger: "blur" }
        ],
        billType: [
          { required: true, message: "账单类型：property物业费，water水费，electric电费，gas燃气费，parking停车费不能为空", trigger: "blur" }
        ],
        amount: [
          { required: true, message: "金额不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询缴费账单列表 */
    getList() {
      this.loading = true
      listBill(this.queryParams).then(response => {
        this.billList = response.rows
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
        billNo: null,
        billName: null,
        billType: null,
        amount: null,
        status: null,
        payTime: null,
        payMethod: null,
        billPeriod: null,
        dueDate: null,
        remark: null,
        createTime: null,
        updateTime: null,
        delFlag: null
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
      this.title = "添加缴费账单"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getBill(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改缴费账单"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateBill(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addBill(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除缴费账单编号为"' + ids + '"的数据项？').then(function() {
        return delBill(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/bill/export', {
        ...this.queryParams
      }, `bill_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
