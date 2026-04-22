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
      <el-form-item label="团队名称" prop="teamName">
        <el-input
          v-model="queryParams.teamName"
          placeholder="请输入团队名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="团队人数" prop="memberCount">
        <el-input
          v-model="queryParams.memberCount"
          placeholder="请输入团队人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="团队分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择团队分类" clearable>
          <el-option
            v-for="dict in dict.type.user_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
      <el-form-item label="总运动人数" prop="lowerCamelase">
        <el-input
          v-model="queryParams.lowerCamelase"
          placeholder="请输入总运动人数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总运动时长" prop="toralSportsTime">
        <el-input
          v-model="queryParams.toralSportsTime"
          placeholder="请输入总运动时长"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="总消耗" prop="totalConsumption">
        <el-input
          v-model="queryParams.totalConsumption"
          placeholder="请输入总消耗"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="运动天数" prop="tasKTime">
        <el-date-picker clearable
          v-model="queryParams.tasKTime"
          type="date"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择运动天数">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="打卡天数" prop="taskday">
        <el-input
          v-model="queryParams.taskday"
          placeholder="请输入打卡天数"
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
          v-hasPermi="['system:team:add']"
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
          v-hasPermi="['system:team:edit']"
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
          v-hasPermi="['system:team:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:team:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="teamList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="团队头像URL" align="center" prop="avatar" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.avatar" :width="50" :height="50"/>
        </template>
      </el-table-column>
      <el-table-column label="简介" align="center" prop="content" />
      <el-table-column label="团队名称" align="center" prop="teamName" />
      <el-table-column label="团队人数" align="center" prop="memberCount" />
      <el-table-column label="团队分类" align="center" prop="category">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.user_category" :value="scope.row.category"/>
        </template>
      </el-table-column>
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
      <el-table-column label="总运动人数" align="center" prop="lowerCamelase" />
      <el-table-column label="总运动时长" align="center" prop="toralSportsTime" />
      <el-table-column label="总消耗" align="center" prop="totalConsumption" />
      <el-table-column label="运动天数" align="center" prop="tasKTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.tasKTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="打卡天数" align="center" prop="taskday" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:team:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:team:remove']"
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

    <!-- 添加或修改运动团队列对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="团队头像URL" prop="avatar">
          <image-upload v-model="form.avatar"/>
        </el-form-item>
        <el-form-item label="简介">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="团队名称" prop="teamName">
          <el-input v-model="form.teamName" placeholder="请输入团队名称" />
        </el-form-item>
        <el-form-item label="团队人数" prop="memberCount">
          <el-input v-model="form.memberCount" placeholder="请输入团队人数" />
        </el-form-item>
        <el-form-item label="团队分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择团队分类">
            <el-option
              v-for="dict in dict.type.user_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="总运动人数" prop="lowerCamelase">
          <el-input v-model="form.lowerCamelase" placeholder="请输入总运动人数" />
        </el-form-item>
        <el-form-item label="总运动时长" prop="toralSportsTime">
          <el-input v-model="form.toralSportsTime" placeholder="请输入总运动时长" />
        </el-form-item>
        <el-form-item label="总消耗" prop="totalConsumption">
          <el-input v-model="form.totalConsumption" placeholder="请输入总消耗" />
        </el-form-item>
        <el-form-item label="运动天数" prop="tasKTime">
          <el-date-picker clearable
            v-model="form.tasKTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择运动天数">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="打卡天数" prop="taskday">
          <el-input v-model="form.taskday" placeholder="请输入打卡天数" />
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
import { listTeam, getTeam, delTeam, addTeam, updateTeam } from "@/api/system/team"

export default {
  name: "Team",
  dicts: ['user_category'],
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
      // 运动团队列表格数据
      teamList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 打卡天数时间范围
      daterangeCreatedAt: [],
      // 打卡天数时间范围
      daterangeUpdatedAt: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        id: null,
        avatar: null,
        content: null,
        teamName: null,
        memberCount: null,
        category: null,
        createdAt: null,
        updatedAt: null,
        lowerCamelase: null,
        toralSportsTime: null,
        totalConsumption: null,
        tasKTime: null,
        taskday: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        avatar: [
          { required: true, message: "团队头像URL不能为空", trigger: "blur" }
        ],
        teamName: [
          { required: true, message: "团队名称不能为空", trigger: "blur" }
        ],
        memberCount: [
          { required: true, message: "团队人数不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "团队分类不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询运动团队列列表 */
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
      listTeam(this.queryParams).then(response => {
        this.teamList = response.rows
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
        avatar: null,
        content: null,
        teamName: null,
        memberCount: null,
        category: null,
        createdAt: null,
        updatedAt: null,
        lowerCamelase: null,
        toralSportsTime: null,
        totalConsumption: null,
        tasKTime: null,
        taskday: null
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
      this.title = "添加运动团队列"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getTeam(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改运动团队列"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTeam(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addTeam(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除运动团队列编号为"' + ids + '"的数据项？').then(function() {
        return delTeam(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/team/export', {
        ...this.queryParams
      }, `team_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
