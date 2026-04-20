<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="课程名称" prop="courseName">
        <el-input
          v-model="queryParams.courseName"
          placeholder="请输入课程名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程时长" prop="courseDuration">
        <el-input
          v-model="queryParams.courseDuration"
          placeholder="请输入课程时长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="预计消耗卡路里" prop="courseCalories">
        <el-input
          v-model="queryParams.courseCalories"
          placeholder="请输入预计消耗卡路里"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="课程难度" prop="courseLevel">
        <el-input
          v-model="queryParams.courseLevel"
          placeholder="请输入课程难度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreatedAt"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="更新时间">
        <el-date-picker
          v-model="daterangeUpdatedAt"
          style="width: 240px"
          value-format="yyyy-MM-dd HH:mm:ss"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="视频" prop="video">
        <el-input
          v-model="queryParams.video"
          placeholder="请输入视频"
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
          v-hasPermi="['system:course:add']"
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
          v-hasPermi="['system:course:edit']"
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
          v-hasPermi="['system:course:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:course:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="courseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="课程封面图片" align="center" prop="courseCoverUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.courseCoverUrl" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="课程名称" align="center" prop="courseName" />
      <el-table-column label="课程时长" align="center" prop="courseDuration" />
      <el-table-column label="预计消耗卡路里" align="center" prop="courseCalories" />
      <el-table-column label="课程难度" align="center" prop="courseLevel" />
      <el-table-column label="创建时间" align="center" prop="createdAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="视频" align="center" prop="video" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:course:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:course:remove']"
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

    <!-- 添加或修改课程信息主对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="课程封面图片" prop="courseCoverUrl">
          <image-upload v-model="form.courseCoverUrl"/>
        </el-form-item>
        <el-form-item label="课程名称" prop="courseName">
          <el-input v-model="form.courseName" placeholder="请输入课程名称" />
        </el-form-item>
        <el-form-item label="课程时长" prop="courseDuration">
          <el-input v-model="form.courseDuration" placeholder="请输入课程时长" />
        </el-form-item>
        <el-form-item label="预计消耗卡路里" prop="courseCalories">
          <el-input v-model="form.courseCalories" placeholder="请输入预计消耗卡路里" />
        </el-form-item>
        <el-form-item label="课程难度" prop="courseLevel">
          <el-input v-model="form.courseLevel" placeholder="请输入课程难度" />
        </el-form-item>
        <el-form-item label="视频" prop="video">
          <el-input v-model="form.video" placeholder="请输入视频" />
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
import { listCourse, getCourse, delCourse, addCourse, updateCourse } from "@/api/system/course"

export default {
  name: "Course",
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
      // 课程信息主表格数据
      courseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 视频时间范围
      daterangeCreatedAt: [],
      // 视频时间范围
      daterangeUpdatedAt: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        courseCoverUrl: null,
        courseName: null,
        courseDuration: null,
        courseCalories: null,
        courseLevel: null,
        createdAt: null,
        updatedAt: null,
        video: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        courseCoverUrl: [
          { required: true, message: "课程封面图片不能为空", trigger: "blur" }
        ],
        courseName: [
          { required: true, message: "课程名称不能为空", trigger: "blur" }
        ],
        courseDuration: [
          { required: true, message: "课程时长不能为空", trigger: "blur" }
        ],
        courseCalories: [
          { required: true, message: "预计消耗卡路里不能为空", trigger: "blur" }
        ],
        courseLevel: [
          { required: true, message: "课程难度不能为空", trigger: "blur" }
        ],
        createdAt: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updatedAt: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询课程信息主列表 */
    getList() {
      this.loading = true
      this.queryParams.params = {}
      if (null != this.daterangeCreatedAt && '' != this.daterangeCreatedAt) {
        this.queryParams.params["beginCreatedAt"] = this.daterangeCreatedAt[0]
        this.queryParams.params["endCreatedAt"] = this.daterangeCreatedAt[1]
      }
      if (null != this.daterangeUpdatedAt && '' != this.daterangeUpdatedAt) {
        this.queryParams.params["beginUpdatedAt"] = this.daterangeUpdatedAt[0]
        this.queryParams.params["endUpdatedAt"] = this.daterangeUpdatedAt[1]
      }
      listCourse(this.queryParams).then(response => {
        this.courseList = response.rows
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
        courseCoverUrl: null,
        courseName: null,
        courseDuration: null,
        courseCalories: null,
        courseLevel: null,
        createdAt: null,
        updatedAt: null,
        video: null
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
      this.daterangeCreatedAt = []
      this.daterangeUpdatedAt = []
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
      this.title = "添加课程信息主"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getCourse(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改课程信息主"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCourse(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addCourse(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除课程信息主编号为"' + ids + '"的数据项？').then(function() {
        return delCourse(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/course/export', {
        ...this.queryParams
      }, `course_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
