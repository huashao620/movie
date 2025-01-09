<template>
  <div style="width: 80%; margin-left: 10%">
    <div>
      <el-menu
          :default-active="this.$route.path"
          router
          mode="horizontal"
          class="el-menu-vertical-demo">
        <el-menu-item index="/movieList" route="/movieList">
          <i class="el-icon-s-shop"></i>
          <span slot="title">电影列表</span>
        </el-menu-item>
        <el-menu-item v-if="loginer.id" index="/UserInfo" route="/UserInfo">
          <i class="el-icon-user"></i>
          <span slot="title">个人中心</span>
        </el-menu-item>
        <el-menu-item>
          <i class="el-icon-s-platform"></i>
          <span slot="title" @click="logout">切换账号</span>
        </el-menu-item>
      </el-menu>
    </div>
    <my-header></my-header>
    <el-main>
      <router-view></router-view>
    </el-main>
  </div>
</template>

<script>
import MyHeader from './Header'
export default {
  data () {
    return {
      loginer: {}
    }
  },
  components: {
    MyHeader
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
      this.loginer = res.data.data || {}
    })
  }
}
</script>

<style scoped>

</style>
