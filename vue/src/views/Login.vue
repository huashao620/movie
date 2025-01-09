<template>
  <div class="login">
    <el-dialog append-to-body :close-on-click-modal="false" :visible.sync="registerDialogFlag" title="用户注册" width="600px">
      <el-form ref="registerForm" inline :model="registerForm" :rules="registerRules" size="small" label-width="120px">
        <el-form-item label="姓名" prop="name">
          <el-input size="small" v-model="registerForm.name" style="width: 400px" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="registerForm.sex" filterable clearable style="width: 400px">
            <el-option key="男" label="男" value="男" />
            <el-option key="女" label="女" value="女" />
          </el-select>
        </el-form-item>
        <el-form-item label="出生日期" prop="birthDate">
          <el-date-picker size="small" format="yyyy-MM-dd" value-format="yyyy-MM-dd" v-model="registerForm.birthDate" style="width: 400px" />
        </el-form-item>
        <el-form-item label="联系方式" prop="phone">
          <el-input size="small" v-model="registerForm.phone" style="width: 400px" />
        </el-form-item>
        <el-form-item label="登录账号" prop="account">
          <el-input size="small" v-model="registerForm.account" style="width: 400px" />
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input size="small" v-model="registerForm.password" style="width: 400px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="registerDialogFlag = false">取消</el-button>
        <el-button type="primary" @click="register">确认</el-button>
      </div>
    </el-dialog>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" label-width="50px">
      <h3 class="title">后台管理系统</h3>
      <el-form-item label="账号" prop="account">
        <el-input
            v-model="loginForm.account"
            type="text"
            auto-complete="off"
            placeholder="账号"
        >
        </el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
            @keyup.enter.native="handleLogin"
        >
        </el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:45%; float: left"
            @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <el-button
            size="medium"
            type="primary"
            style="width:45%; float: right"
            @click.native.prevent="registerDialogFlag = true"
        >
          <span>注 册</span>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>

export default {
  name: 'Login',
  data () {
    return {
      codeUrl: '',
      loginForm: {
        account: '',
        password: '',
      },
      loginRules: {
        account: [
          { required: true, trigger: 'blur', message: '请输入您的账号' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入您的密码' }
        ],
      },
      registerDialogFlag: false,
      registerForm: {
        account: '',
        password: '',
      },
      registerRules: {
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
      },
      loading: false,
    }
  },
  methods: {
    handleLogin () {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$axios({
            url: '/api/login',
            method: 'post',
            data: this.loginForm
          }).then(res => {
            this.loading = false
            if (res.data.code === 0) {
              localStorage.setItem('token', res.data.data.key)
              this.$router.push({ path: res.data.data.value === '管理员' ? '/user': '/movieList' })
            } else {
              this.$notify.error({
                title: '错误',
                message: res.data.msg
              })
            }
          }).catch(e => {
            this.$notify.error({
              title: '错误',
              message: e
            })
            this.loading = false
          })
        }
      })
    },
    register() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.registerForm.role = '用户'
          this.$axios({
            url: '/api/user/add',
            method: 'post',
            data: this.registerForm
          }).then(res => {
            this.loading = false
            if (res.data.code === 0) {
              this.$notify.success({
                title: '成功',
                message: '注册成功，请登录'
              })
              this.registerDialogFlag = false
            } else {
              this.$notify.error({
                title: '错误',
                message: res.data.msg
              })
            }
          }).catch(e => {
            this.$notify.error({
              title: '错误',
              message: e
            })
            this.loading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login.png");
  background-size: cover;
}
.title {
  margin: 0 auto 30px auto;
  text-align: center;
  color: #707070;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 500px;
  padding: 25px 25px 5px 25px;
  .el-input {
    height: 38px;
    input {
      height: 38px;
    }
  }
  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
</style>
