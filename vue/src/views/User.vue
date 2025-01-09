<template>
<div>
  <el-form :inline="true" :model="queryParam">
    <el-form-item label="姓名">
      <el-input size="small" v-model="queryParam.name" width="370px" />
    </el-form-item>
    <el-form-item label="角色">
      <el-select size="small" v-model="queryParam.role" filterable clearable width="370px">
        <el-option key="管理员" label="管理员" value="管理员" />
        <el-option key="用户" label="用户" value="用户" />
        </el-select>
    </el-form-item>
    <el-form-item label="状态">
      <el-select size="small" v-model="queryParam.status" filterable clearable width="370px">
        <el-option key="正常" label="正常" value="正常" />
        <el-option key="禁用" label="禁用" value="禁用" />
        </el-select>
    </el-form-item>
    <el-form-item>
      <el-button size="small" type="primary" icon="el-icon-search" @click="getList">搜索</el-button>
      <el-button size="small" type="success" icon="el-icon-plus" @click="toSave({})">新增</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData" border style="width: 100%">
    <el-table-column prop="name" label="姓名" align="center" />
    <el-table-column prop="sex" label="性别" align="center" />
    <el-table-column prop="birthDate" label="出生日期" align="center" />
    <el-table-column prop="phone" label="联系方式" align="center" />
    <el-table-column prop="role" label="角色" align="center" />
    <el-table-column prop="account" label="登录账号" align="center" />
    <el-table-column prop="password" label="登录密码" align="center" />
    <el-table-column prop="status" label="状态" align="center" />
    <el-table-column prop="createTime" label="创建时间" align="center"/>
    <el-table-column label="操作" width="250px">
      <template slot-scope="scope">
        <el-button size="mini" type="primary" @click="toSave(scope.row)"><i class="el-icon-edit" />编辑</el-button>
        <el-button v-if="scope.row.status === '正常'" size="mini" type="warning" @click="toFreeze(scope.row)"><i class="el-icon-document-delete" />禁用</el-button>
        <el-button v-if="scope.row.status === '禁用'" size="mini" type="success" @click="unFreeze(scope.row)"><i class="el-icon-refresh-right" />恢复</el-button>
      </template>
    </el-table-column>
  </el-table>
  <el-pagination
      style="margin-top: 20px"
      @current-change="handleCurrentChange"
      :total="totalItems"
      layout="total, prev, pager, next"
      :pageSize="10"
      :currentPage="queryParam.current"
  ></el-pagination>
  <el-dialog append-to-body :close-on-click-modal="false" :visible.sync="editDialogFlag" title="编辑用户" width="600px">
    <el-form ref="saveForm" inline :model="saveForm" :rules="rules" size="small" label-width="120px">
      <el-form-item label="姓名" prop="name">
        <el-input size="small" v-model="saveForm.name" style="width: 400px" />
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="saveForm.sex" filterable clearable style="width: 400px">
          <el-option key="男" label="男" value="男" />
          <el-option key="女" label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="出生日期" prop="birthDate">
        <el-date-picker size="small" format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="saveForm.birthDate" style="width: 400px" />
      </el-form-item>
      <el-form-item label="联系方式" prop="phone">
        <el-input size="small" v-model="saveForm.phone" style="width: 400px" />
      </el-form-item>
      <el-form-item label="角色" prop="role">
        <el-select v-model="saveForm.role" filterable clearable style="width: 400px">
          <el-option key="管理员" label="管理员" value="管理员" />
          <el-option key="用户" label="用户" value="用户" />
        </el-select>
      </el-form-item>
      <el-form-item label="登录账号" prop="account">
        <el-input size="small" v-model="saveForm.account" style="width: 400px" />
      </el-form-item>
      <el-form-item label="登录密码" prop="password">
        <el-input size="small" v-model="saveForm.password" style="width: 400px" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="editDialogFlag = false">取消</el-button>
      <el-button :loading="editLoading" type="primary" @click="save">确认</el-button>
    </div>
  </el-dialog>
  <el-dialog append-to-body :close-on-click-modal="false" :visible.sync="editDialogFlag2" title="禁用用户" width="600px">
    <el-form ref="saveForm2" inline :model="saveForm" :rules="rules" size="small" label-width="120px">
      <el-form-item label="禁用原因" prop="freezeReason">
        <el-input size="small" v-model="saveForm.freezeReason" style="width: 400px" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="editDialogFlag2 = false">取消</el-button>
      <el-button :loading="editLoading" type="primary" @click="freeze">确认</el-button>
    </div>
  </el-dialog>
</div>
</template>

<script>
import {downloadFile} from "@/utils/download";
export default {
  data() {
    return {
      tableData: [],
      totalItems: null,
      queryParam: {
        current: 1,
      },
      saveForm: {},
      editLoading: false,
      editDialogFlag: false,
      editDialogFlag2: false,
      rules: {
        name: [
          { required: true, message: '姓名不能为空', trigger: 'change' }
        ],
        sex: [
          { required: true, message: '性别不能为空', trigger: 'change' }
        ],
        birthDate: [
          { required: true, message: '出生日期不能为空', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '联系方式不能为空', trigger: 'change' }
        ],
        role: [
          { required: true, message: '角色不能为空', trigger: 'change' }
        ],
        account: [
          { required: true, message: '登录账号不能为空', trigger: 'change' }
        ],
        password: [
          { required: true, message: '登录密码不能为空', trigger: 'change' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ],
        freezeReason: [
          { required: true, message: '禁用原因不能为空', trigger: 'change' }
        ],
      },
      loginer: {}
    }
  },

  created() {
    this.getList()
    this.$axios({
      url: '/api/loginer',
      method: 'get',
    }).then(res => {
      this.loginer = res.data.data || {}
    })
  },

  methods: {
    handleCurrentChange(page) {
      this.queryParam.current = page
      this.getList()
    },
    getList() {
      this.$axios({
        url: '/api/user/page',
        method: 'post',
        data: this.queryParam
      }).then(res => {
        this.tableData = res.data.data.records
        this.totalItems = res.data.data.total
      })
    },
    toSave(data) {
      this.saveForm = data
      this.editDialogFlag = true
      this.$nextTick(() => {
        this.$refs.saveForm.clearValidate()
      })
    },
    toFreeze(data) {
      this.saveForm = data
      this.editDialogFlag2 = true
      this.$nextTick(() => {
        this.$refs.saveForm2.clearValidate()
      })
    },
    save() {
      this.$refs.saveForm.validate(res => {
        if (res) {
          this.$axios({
            url: this.saveForm.id ? '/api/user/update': '/api/user/add',
            method: 'post',
            data: this.saveForm
          }).then(res => {
            if (res.data.code === 0) {
              this.$message.success('保存成功');
              this.editDialogFlag = false
              this.getList()
            } else {
              this.$message.error(res.data.msg);
            }
          })
        }
      })
    },
    freeze() {
      this.$refs.saveForm2.validate(res => {
        if (res) {
          this.saveForm.status = '禁用'
          this.$axios({
            url: '/api/user/update',
            method: 'post',
            data: this.saveForm
          }).then(res => {
            if (res.data.code === 0) {
              this.$message.success('禁用成功');
              this.editDialogFlag2 = false
              this.getList()
            } else {
              this.$message.error(res.data.msg);
            }
          })
        }
      })
    },
    unFreeze(row) {
      this.saveForm = row
      this.saveForm.status = '正常'
      this.saveForm.freezeReason = ''
      this.$axios({
        url: '/api/user/update',
        method: 'post',
        data: this.saveForm
      }).then(res => {
        if (res.data.code === 0) {
          this.$message.success('恢复成功');
          this.editDialogFlag2 = false
          this.getList()
        } else {
          this.$message.error(res.data.msg);
        }
      })
    },
    toDelete(row) {
      this.$confirm('确定删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios({
          url: '/api/user/delete?id=' + row.id,
          method: 'delete'
        }).then(res => {
          if (res.data.code === 0) {
            this.$message.success('删除成功');
            this.getList()
          } else {
            this.$message.error(res.data.msg);
          }
        })
      }).catch(e => {})
    },
    downloadAttach(url, name) {
      downloadFile(url, name)
    },
    handleExceed(files, fileList) {
      this.$message.error('最多只能上传一个文件');
    },
  }
}
</script>
<style scoped>
.avatar {
  display: inline-block;
  width: 200px;
  float: right;
}
</style>
