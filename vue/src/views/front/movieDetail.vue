<template>
  <div>
    <div>
      <el-row :gutter="20">
        <el-col :span="6">
          <img :src="'http://localhost:8080/api/files/' + movie.imgUrl" style="width: 300px; height: 300px">
        </el-col>
        <el-col :span="18" style="line-height: 30px">
          <span style="font-size: 20px; font-weight: bold">{{movie.name}}</span>
          <br>
          类别：{{movie.type}}
          <br>
          导演：{{movie.director}}
          <br>
          演员：{{movie.actor}}
          <br>
          上映日期：{{movie.upDate}}
          <br>
          评分：{{movie.score}}
          <br>
          简介：{{movie.introduction}}
          <br>
          <el-button size="small" type="primary" @click="$router.go(-1)">返回</el-button>
        </el-col>
      </el-row>
    </div>
    <el-divider></el-divider>
    <el-tabs v-model="activeName" type="card">
      <el-tab-pane label="电影评论" name="first">
        <leave-message :movie-id="$route.query.id" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import LeaveMessage from "@/views/front/LeaveMessage.vue";
export default {
  data() {
    return {
      activeName: 'first',
      score: 4,
      movie: {
        type: {}
      },
      loginer: {}
    }
  },
  components: {
    LeaveMessage
  },
  created() {
    this.$axios({
      url: '/api/movie/page',
      method: 'post',
      data: {id: this.$route.query.id}
    }).then(res => {
      this.movie = res.data.data.records[0]
    })
    this.$axios({
      url: '/api/loginer',
      method: 'get',
    }).then(res => {
      this.loginer = res.data.data || {}
    })
  },

  methods: {

  }
}
</script>
<style>

</style>
