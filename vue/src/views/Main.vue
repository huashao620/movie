<template>
  <el-container>
    <el-aside width="201px" style="overflow: hidden; background-color: #545c64">
      <el-menu
          :default-active="this.$route.path"
          router
          class="el-menu-vertical-demo"
          background-color="#545c64"
          text-color="#fff"
          active-text-color="#ffd04b">
        <el-menu-item index="/user" route="/user">
          <i class="el-icon-user"></i>
          <span slot="title">用户管理</span>
        </el-menu-item>
        <el-menu-item index="/movie" route="/movie">
          <i class="el-icon-video-camera-solid"></i>
          <span slot="title">电影管理</span>
        </el-menu-item>
        <el-menu-item index="/movieRemark" route="/movieRemark">
          <i class="el-icon-document"></i>
          <span slot="title">电影评论</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <div class="nav-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item>{{ this.$route.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="nav-right">
          <el-dropdown>
            <span class="el-dropdown-link">
              {{loginer.name}}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  data () {
    return {
      isCollapse: false,
      loginer: {}
    }
  },
  methods: {
    logout() {
      this.$axios({
        url: '/api/logout',
        method: 'delete'
      }).then(res => {
        this.$router.push({path: '/'})
      })
    }
  },
  created () {
    this.$axios({
      url: '/api/loginer',
      method: 'get',
    }).then(res => {
      if (!res.data.data) {
        this.$message.error('用户已退出，请先登录')
        this.$router.push({ path: '/' })
        return
      }
      this.loginer = res.data.data
    })
  }
}
</script>

<style scoped>

.el-header, .el-footer {
  background-color: #FFF;
  color: #333;
  line-height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-header .nav-left {
  display: flex;
}

.el-header .nav-left i {
  margin-right: 20px;
}

.el-aside {
  background-color: #FFF;
  color: #333;
}

.el-main {
  background-color: #f5f5f5;
  color: #333;
}

body > .el-container {
  margin-bottom: 40px;
}

.is-vertical {
  height: 100vh;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.el-submenu .el-menu-item {
  padding-left: 50px !important;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  font-size: 18px;
}

.logo img {
  width: 35px;
  margin: 0 15px 0 30px;
}

.nav-right {
  display: flex;
  align-items: center;
}

.nav-right img {
  width: 40px;
}

.nav-right .el-dropdown {
  margin: 0 10px;
}
</style>
