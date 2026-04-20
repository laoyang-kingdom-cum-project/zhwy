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
      <el-form-item label="运动开始时间">
        <el-date-picker
          v-model="daterangeWorkoutTime"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="运动分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择运动分类" clearable>
          <el-option
            v-for="dict in dict.type.exercise_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="消耗卡路里" prop="calories">
        <el-input
          v-model="queryParams.calories"
          placeholder="请输入消耗卡路里"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="运动时长">
        <el-date-picker
          v-model="daterangeDuration"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="用户id" prop="userid">
        <el-input
          v-model="queryParams.userid"
          placeholder="请输入用户id"
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
          v-hasPermi="['system:log:add']"
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
          v-hasPermi="['system:log:edit']"
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
          v-hasPermi="['system:log:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:log:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="运动开始时间" align="center" prop="workoutTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.workoutTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="运动分类" align="center" prop="category">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.exercise_category" :value="scope.row.category"/>
        </template>
      </el-table-column>
      <el-table-column label="消耗卡路里" align="center" prop="calories" />
      <el-table-column label="运动时长" align="center" prop="duration" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.duration, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户id" align="center" prop="userid" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:log:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:log:remove']"
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

    <!-- 添加或修改运动记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="运动开始时间" prop="workoutTime">
          <el-date-picker clearable
            v-model="form.workoutTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择运动开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="运动分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择运动分类">
            <el-option
              v-for="dict in dict.type.exercise_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="消耗卡路里" prop="calories">
          <el-input v-model="form.calories" placeholder="请输入消耗卡路里" />
        </el-form-item>
        <el-form-item label="运动时长" prop="duration">
          <el-date-picker clearable
            v-model="form.duration"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择运动时长">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="用户id" prop="userid">
          <el-input v-model="form.userid" placeholder="请输入用户id" />
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
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/system/log"

export default {
  name: "Log",
  dicts: ['exercise_category'],
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
      // 运动记录表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 用户id时间范围
      daterangeWorkoutTime: [],
      // 用户id时间范围
      daterangeDuration: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        workoutTime: null,
        category: null,
        calories: null,
        duration: null,
        userid: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        workoutTime: [
          { required: true, message: "运动开始时间不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "运动分类不能为空", trigger: "change" }
        ],
        calories: [
          { required: true, message: "消耗卡路里不能为空", trigger: "blur" }
        ],
        duration: [
          { required: true, message: "运动时长不能为空", trigger: "blur" }
        ],
        userid: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询运动记录列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeWorkoutTime && '' != this.daterangeWorkoutTime) {
        this.queryParams.params["beginWorkoutTime"] = this.daterangeWorkoutTime[0]
        this.queryParams.params["endWorkoutTime"] = this.daterangeWorkoutTime[1]
      }
      if (null != this.daterangeDuration && '' != this.daterangeDuration) {
        this.queryParams.params["beginDuration"] = this.daterangeDuration[0]
        this.queryParams.params["endDuration"] = this.daterangeDuration[1]
      }
      listLog(this.queryParams).then(response => {
        this.logList = response.rows
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
        workoutTime: null,
        category: null,
        calories: null,
        duration: null,
        userid: null
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
      this.daterangeWorkoutTime = []
      this.daterangeDuration = []
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
      this.title = "添加运动记录"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getLog(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改运动记录"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addLog(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除运动记录编号为"' + ids + '"的数据项？').then(function() {
        return delLog(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/log/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
