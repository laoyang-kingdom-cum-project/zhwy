<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
      <el-form-item label="更新信息时间">
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
          v-hasPermi="['system:press:add']"
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
          v-hasPermi="['system:press:edit']"
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
          v-hasPermi="['system:press:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:press:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pressList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键" align="center" prop="id" />
      <el-table-column label="团队头像" align="center" prop="avatar" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.avatar" :width="50" :height="50"/>
        </template>
      </el-table-column>
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
      <el-table-column label="更新信息时间" align="center" prop="updatedAt" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updatedAt, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总运动人数" align="center" prop="lowerCamelCase" />
      <el-table-column label="总运动时长" align="center" prop="totalSportsTime" />
      <el-table-column label="总消耗" align="center" prop="totalConsumption" />
      <el-table-column label="任务时间" align="center" prop="taskTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.taskTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="要求打卡天数" align="center" prop="taskday" />
      <el-table-column label="任务排行榜" align="center" prop="teamMember" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:press:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:press:remove']"
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

    <!-- 添加或修改运动团详情对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="团队头像" prop="avatar">
          <image-upload v-model="form.avatar"/>
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
        <el-form-item label="总运动人数" prop="lowerCamelCase">
          <el-input v-model="form.lowerCamelCase" placeholder="请输入总运动人数" />
        </el-form-item>
        <el-form-item label="总运动时长" prop="totalSportsTime">
          <el-input v-model="form.totalSportsTime" placeholder="请输入总运动时长" />
        </el-form-item>
        <el-form-item label="总消耗" prop="totalConsumption">
          <el-input v-model="form.totalConsumption" placeholder="请输入总消耗" />
        </el-form-item>
        <el-form-item label="任务时间" prop="taskTime">
          <el-date-picker clearable
            v-model="form.taskTime"
            type="date"
            value-format="yyyy-MM-dd HH:mm:ss"
            placeholder="请选择任务时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="要求打卡天数" prop="taskday">
          <el-input v-model="form.taskday" placeholder="请输入要求打卡天数" />
        </el-form-item>
        <el-form-item label="任务排行榜" prop="teamMember">
          <el-input v-model="form.teamMember" placeholder="请输入任务排行榜" />
        </el-form-item>
        <el-divider content-position="center">运动团队列信息</el-divider>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddSportTeam">添加</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDeleteSportTeam">删除</el-button>
          </el-col>
        </el-row>
        <el-table :data="sportTeamList" :row-class-name="rowSportTeamIndex" @selection-change="handleSportTeamSelectionChange" ref="sportTeam">
          <el-table-column type="selection" width="50" align="center" />
          <el-table-column label="序号" align="center" prop="index" width="50"/>
          <el-table-column label="团队名称" prop="teamName" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.teamName" placeholder="请输入团队名称" />
            </template>
          </el-table-column>
          <el-table-column label="团队人数" prop="memberCount" width="150">
            <template slot-scope="scope">
              <el-input v-model="scope.row.memberCount" placeholder="请输入团队人数" />
            </template>
          </el-table-column>
          <el-table-column label="团队分类" prop="category" width="150">
            <template slot-scope="scope">
              <el-select v-model="scope.row.category" placeholder="请选择团队分类">
                <el-option
                  v-for="dict in dict.type.user_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                ></el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="" prop="createdAt" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.createdAt" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" />
            </template>
          </el-table-column>
          <el-table-column label="" prop="updatedAt" width="240">
            <template slot-scope="scope">
              <el-date-picker clearable v-model="scope.row.updatedAt" type="date" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择" />
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPress, getPress, delPress, addPress, updatePress } from "@/api/system/press"

export default {
  name: "Press",
  dicts: ['user_category'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 子表选中数据
      checkedSportTeam: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 运动团详情表格数据
      pressList: [],
      // 运动团队列表格数据
      sportTeamList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 时间范围
      daterangeCreatedAt: [],
      // 时间范围
      daterangeUpdatedAt: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamName: null,
        memberCount: null,
        category: null,
        createdAt: null,
        updatedAt: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        avatar: [
          { required: true, message: "团队头像不能为空", trigger: "blur" }
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
    /** 查询运动团详情列表 */
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
      listPress(this.queryParams).then(response => {
        this.pressList = response.rows
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
        teamName: null,
        memberCount: null,
        category: null,
        createdAt: null,
        updatedAt: null,
        lowerCamelCase: null,
        totalSportsTime: null,
        totalConsumption: null,
        taskTime: null,
        taskday: null,
        teamMember: null
      }
      this.sportTeamList = []
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
      this.title = "添加运动团详情"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getPress(id).then(response => {
        this.form = response.data
        this.sportTeamList = response.data.sportTeamList
        this.open = true
        this.title = "修改运动团详情"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.form.sportTeamList = this.sportTeamList
          if (this.form.id != null) {
            updatePress(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addPress(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除运动团详情编号为"' + ids + '"的数据项？').then(function() {
        return delPress(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
	/** 运动团队列序号 */
    rowSportTeamIndex({ row, rowIndex }) {
      row.index = rowIndex + 1
    },
    /** 运动团队列添加按钮操作 */
    handleAddSportTeam() {
      let obj = {}
      obj.avatar = ""
      obj.teamName = ""
      obj.memberCount = ""
      obj.category = ""
      obj.createdAt = ""
      obj.updatedAt = ""
      this.sportTeamList.push(obj)
    },
    /** 运动团队列删除按钮操作 */
    handleDeleteSportTeam() {
      if (this.checkedSportTeam.length == 0) {
        this.$modal.msgError("请先选择要删除的运动团队列数据")
      } else {
        const sportTeamList = this.sportTeamList
        const checkedSportTeam = this.checkedSportTeam
        this.sportTeamList = sportTeamList.filter(function(item) {
          return checkedSportTeam.indexOf(item.index) == -1
        })
      }
    },
    /** 复选框选中数据 */
    handleSportTeamSelectionChange(selection) {
      this.checkedSportTeam = selection.map(item => item.index)
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/press/export', {
        ...this.queryParams
      }, `press_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
