<template>
  <div>
    <el-form ref="editForm" :model="editForm" size="small" :rules="rules" label-width="120px">
      <el-form-item label="登录账号" prop="account">
        <el-input size="small" v-model="editForm.account" style="width: 400px" readonly></el-input>
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input size="small" v-model="editForm.name" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item label="性别" prop="sex">
        <el-select v-model="editForm.sex" filterable clearable style="width: 400px">
          <el-option key="男" label="男" value="男" />
          <el-option key="女" label="女" value="女" />
        </el-select>
      </el-form-item>
      <el-form-item label="出生日期" prop="birthDate">
        <el-date-picker size="small" format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="editForm.birthDate" style="width: 400px" />
      </el-form-item>
      <el-form-item label="手机号" prop="phone">
        <el-input size="small" v-model="editForm.phone" style="width: 400px"></el-input>
      </el-form-item>
      <el-form-item label="登录密码" prop="password">
        <el-input size="small" v-model="editForm.password" style="width: 400px"></el-input>
      </el-form-item>
    </el-form>
    <el-button size="small" :loading="loading" type="primary" @click="save">修改</el-button>
  </div>
</template>
<script>
export default {
  data() {
    return {
      loading: false,
      editForm: {
        name: null,
        sex: null,
        phone: null,
        password: null,
      },
      rules: {
        name: [
          { required: true, message: '姓名不能为空', trigger: 'change' }
        ],
        sex: [
          { required: true, message: '性别不能为空', trigger: 'change' }
        ],
        phone: [
          { required: true, message: '手机号不能为空', trigger: 'change' }
        ],
        password: [
          { required: true, message: '登录密码不能为空', trigger: 'change' }
        ],
        birthDate: [
          { required: true, message: '出生日期不能为空', trigger: 'change' }
        ],
      }
    }
  },

  created() {
    this.$axios({
      url: '/api/loginer',
      method: 'get',
    }).then(res => {
      this.editForm = res.data.data || {}
    })
  },

  methods: {
    save() {
      this.$refs.editForm.validate(res => {
        if (res) {
          this.$axios({
            url: '/api/user/update',
            method: 'post',
            data: this.editForm
          }).then(res => {
            this.$message({
              message: '保存成功',
              type: 'success'
            });
          })
        }
      })
    }
  }
}
</script>
