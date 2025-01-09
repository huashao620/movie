<template>
<div>
  <el-form :inline="true" :model="queryParam">
    <el-form-item label="评论内容">
      <el-input size="small" v-model="queryParam.content" width="370px" />
    </el-form-item>
    <el-form-item label="评论电影">
      <el-select size="small" v-model="queryParam.movieId" filterable clearable width="370px">
        <el-option v-for="item in movieList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button size="small" type="primary" icon="el-icon-search" @click="getList">搜索</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData" border style="width: 100%">
    <el-table-column prop="content" label="评论内容" align="center" />
    <el-table-column prop="user.name" label="评论用户" align="center" />
    <el-table-column prop="movie.name" label="评论电影" align="center" />
    <el-table-column prop="createTime" label="评论时间" align="center" />
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button size="mini" type="danger" @click="toDelete(scope.row)"><i class="el-icon-delete" />删除</el-button>
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
  <el-dialog append-to-body :close-on-click-modal="false" :visible.sync="editDialogFlag" title="编辑电影评论" width="600px">
    <el-form ref="saveForm" inline :model="saveForm" :rules="rules" size="small" label-width="120px">
      <el-form-item label="评论内容" prop="content">
        <el-input size="small" v-model="saveForm.content" style="width: 400px" />
      </el-form-item>
      <el-form-item label="评论用户" prop="userId">
        <el-select v-model="saveForm.userId" filterable clearable style="width: 400px">
          <el-option v-for="item in userList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="评论电影" prop="movieId">
        <el-select v-model="saveForm.movieId" filterable clearable style="width: 400px">
          <el-option v-for="item in movieList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" @click="editDialogFlag = false">取消</el-button>
      <el-button :loading="editLoading" type="primary" @click="save">确认</el-button>
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
      userList: [],
      movieList: [],
      editLoading: false,
      editDialogFlag: false,
      rules: {
        content: [
          { required: true, message: '评论内容不能为空', trigger: 'change' }
        ],
        userId: [
          { required: true, message: '评论用户不能为空', trigger: 'change' }
        ],
        movieId: [
          { required: true, message: '评论电影不能为空', trigger: 'change' }
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
    this.$axios({
      url: '/api/user/query',
      method: 'post',
      data: {}
    }).then(res => {
      this.userList = res.data.data
    })
    this.$axios({
      url: '/api/movie/query',
      method: 'post',
      data: {}
    }).then(res => {
      this.movieList = res.data.data
    })
  },

  methods: {
    handleCurrentChange(page) {
      this.queryParam.current = page
      this.getList()
    },
    getList() {
      this.$axios({
        url: '/api/movie-remark/page',
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
    save() {
      this.$refs.saveForm.validate(res => {
        if (res) {
          this.$axios({
            url: this.saveForm.id ? '/api/movie-remark/update': '/api/movie-remark/add',
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
    toDelete(row) {
      this.$confirm('确定删除吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios({
          url: '/api/movie-remark/delete?id=' + row.id,
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
