<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="3">
        <el-menu
            default-active="0"
            class="el-menu-vertical-demo"
            @select="typeChange">
          <el-menu-item index="0">
            <span slot="title">全部</span>
          </el-menu-item>
          <el-menu-item v-for="type in typeList" :index="type">
            <span slot="title">{{type}}</span>
          </el-menu-item>
          <el-menu-item index="99999">
            <span slot="title">我的收藏</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="21">
        <el-row :gutter="20">
          <el-col :span="4">
            <el-input v-model="queryForm.name" size="small" placeholder="请输入电影名称"></el-input>
          </el-col>
          <el-col :span="4">
            <el-input v-model="queryForm.director" size="small" placeholder="请输入导演"></el-input>
          </el-col>
          <el-col :span="4">
            <el-input v-model="queryForm.actor" size="small" placeholder="请输入演员"></el-input>
          </el-col>
          <el-col :span="4">
            <el-date-picker
                size="small"
                v-model="queryForm.year"
                type="year"
                value-format="yyyy"
                placeholder="请选择年份">
            </el-date-picker>
          </el-col>
          <el-col :span="4">
            <el-select size="small" style="margin-left: 20px" placeholder="请选择排序方式" v-model="queryForm.sortType">
              <el-option label="好评优先" :value="1"></el-option>
              <el-option label="最新上映" :value="2"></el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            <el-button style="margin-left: 20px" size="small" type="primary" @click="loadData">搜索</el-button>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="6" v-for="movie in movieList" :key="movie.id">
            <el-card style="margin-top: 20px">
              <img :src="'http://localhost:8080/api/files/' + movie.imgUrl" class="image" @click="toDetail(movie.id)" style="width: 300px; height: 250px">
              <div style="padding: 14px;">
                <span>{{movie.name}}</span>
                <el-button v-if="movie.ifCollect" type="text" class="button" @click="cancelCollect(movie)">取消收藏</el-button>
                <el-button v-else type="text" class="button" @click="toCollect(movie)">收藏</el-button>
                <div class="bottom clearfix">
                  <time class="time">{{ movie.upDate }}</time>
                  <el-rate style="display: inline; float: right" :value="movie.score/2" show-score disabled :score-template="`${movie.score}分`" />
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      movieList: [],
      typeList: [],
      queryForm: {
        name: null,
        type: null,
        sortType: 1
      }
    }
  },

  created() {
    this.$axios({
      url: '/api/movie/type',
      method: 'get',
      data: {}
    }).then(res => {
      this.typeList = res.data.data
    })
    this.loadData()
  },

  methods: {
    typeChange(val) {
      if (val === '0') {
        this.queryForm.type = null
        this.queryForm.onlyCollect = false
      } else if (val === '99999') {
        this.queryForm.onlyCollect = true
        this.queryForm.type = null
      } else {
        this.queryForm.onlyCollect = false
        this.queryForm.type = val
      }
      this.loadData()
    },

    loadData() {
      this.$axios({
        url: '/api/movie/query',
        method: 'post',
        data: this.queryForm
      }).then(res => {
        this.movieList = res.data.data
      })
    },
    toDetail(id) {
      this.$router.push({
        path: 'movieDetail',
        query: {id: id}
      })
    },
    toCollect(movie) {
      this.$axios({
        url: '/api/movie-collect/add',
        method: 'post',
        data: {movieId: movie.id}
      }).then(res => {
        if (res.data.code === 0) {
          this.$message.success('收藏成功')
          movie.ifCollect = true
        } else {
          this.$message.error(res.data.msg)
          this.$router.push('/')
        }
      })
    },
    cancelCollect(movie) {
      this.$axios({
        url: '/api/movie-collect/delete?movieId=' + movie.id,
        method: 'delete'
      }).then(res => {
        this.$message.success('取消收藏成功')
        this.loadData()
      })
    },
  }
}
</script>
<style>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
  cursor: pointer;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}
</style>
