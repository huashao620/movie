<template>
<div>
  <el-form :inline="true" :model="queryParam">
    <el-form-item label="电影名称">
      <el-input size="small" v-model="queryParam.name" width="370px" />
    </el-form-item>
    <el-form-item label="导演">
      <el-input size="small" v-model="queryParam.director" width="370px" />
    </el-form-item>
    <el-form-item label="演员">
      <el-input size="small" v-model="queryParam.actor" width="370px" />
    </el-form-item>
    <el-form-item>
      <el-button size="small" type="primary" icon="el-icon-search" @click="getList">搜索</el-button>
      <el-button size="small" type="success" icon="el-icon-plus" @click="toSave({})">新增</el-button>
    </el-form-item>
  </el-form>
  <el-table :data="tableData" border style="width: 100%">
    <el-table-column prop="name" label="电影名称" align="center" />
    <el-table-column prop="type" label="电影类型" align="center" />
    <el-table-column show-overflow-tooltip prop="introduction" label="简介" align="center" />
    <el-table-column label="海报" align="center">
      <template slot-scope="scope">
        <img :src="'http://localhost:8080/api/files/' + scope.row.imgUrl" style="width: 50px; height: 50px;"/>
      </template>
    </el-table-column>
    <el-table-column prop="director" label="导演" align="center" />
    <el-table-column prop="actor" label="演员" align="center" />
    <el-table-column prop="upDate" label="上映日期" align="center" />
    <el-table-column prop="score" label="评分" align="center" />
    <el-table-column label="操作">
      <template slot-scope="scope">
        <el-button size="mini" type="primary" @click="toSave(scope.row)"><i class="el-icon-edit" />编辑</el-button>
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
  <el-dialog append-to-body :close-on-click-modal="false" :visible.sync="editDialogFlag" title="编辑电影信息" width="600px" top="10px">
    <el-form ref="saveForm" inline :model="saveForm" :rules="rules" size="small" label-width="120px">
      <el-form-item label="电影名称" prop="name">
        <el-input size="small" v-model="saveForm.name" style="width: 400px" />
      </el-form-item>
      <el-form-item label="电影类型" prop="type">
        <el-input size="small" v-model="saveForm.type" style="width: 400px" />
      </el-form-item>
      <el-form-item label="简介" prop="introduction">
        <el-input type="textarea" rows="5" size="small" v-model="saveForm.introduction" style="width: 400px" />
      </el-form-item>
      <el-form-item label="海报" prop="imgUrl">
        <el-upload
            style="width: 400px"
            class="avatar-uploader"
            action="http://localhost:8080/api/file/upload"
            :show-file-list="false"
            :on-success="imgSuccess"
        >
          <img v-if="saveForm.imgUrl" :src="'http://localhost:8080/api/files/' + saveForm.imgUrl" class="avatar"/>
          <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form-item>
      <el-form-item label="导演" prop="director">
        <el-input size="small" v-model="saveForm.director" style="width: 400px" />
      </el-form-item>
      <el-form-item label="演员" prop="actor">
        <el-input size="small" v-model="saveForm.actor" style="width: 400px" />
      </el-form-item>
      <el-form-item label="上映日期" prop="upDate">
        <el-date-picker size="small" format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="saveForm.upDate" style="width: 400px" />
      </el-form-item>
      <el-form-item label="评分" prop="score">
        <el-input-number size="small" v-model="saveForm.score" style="width: 400px" :precision="1" :min="0" :max="9.9"/>
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
      editLoading: false,
      editDialogFlag: false,
      rules: {
        name: [
          { required: true, message: '电影名称不能为空', trigger: 'change' }
        ],
        type: [
          { required: true, message: '电影类型不能为空', trigger: 'change' }
        ],
        introduction: [
          { required: true, message: '简介不能为空', trigger: 'change' }
        ],
        imgUrl: [
          { required: true, message: '海报不能为空', trigger: 'change' }
        ],
        director: [
          { required: true, message: '导演不能为空', trigger: 'change' }
        ],
        actor: [
          { required: true, message: '演员不能为空', trigger: 'change' }
        ],
        upDate: [
          { required: true, message: '上映日期不能为空', trigger: 'change' }
        ],
        score: [
          { required: true, message: '评分不能为空', trigger: 'change' }
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
        url: '/api/movie/page',
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
            url: this.saveForm.id ? '/api/movie/update': '/api/movie/add',
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
          url: '/api/movie/delete?id=' + row.id,
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
    imgSuccess(res, file) {
      this.$set(this.saveForm, 'imgUrl', res.data)
      this.saveForm.imgName = file.name
    },
    imgRemove(res, file) {
      this.saveForm.imgUrl = null
      this.saveForm.imgName = null
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
