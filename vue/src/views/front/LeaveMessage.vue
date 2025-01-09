<template>
  <div class="msg-all-contain">
    <div class="msg-board">
      <div class="msg-board-contain">
        <div class="msg-head" v-if="user.id">

          <textarea
              type="textarea"
              :class="inputStatusClass"
              placeholder="请输入内容..."
              ref="input"
              v-model="newComment"
              cols="60"
              rows="5"
          >
          </textarea>
          <button @click="submit">发表</button>
        </div>
        <div class="msg-content">
          <comments-child
              :comments="comments"
              :count="layerCount"
              :movie-id="movieId"
          ></comments-child>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import CommentsChild from "./CommentsChild";
export default {
  name: "commentsMessage",
  props: {
    movieId: {
      type: String,
      required: true
    }
  },
  data() {
    return {
      // 评论列表
      comments: [],
      // 新评论
      newComment: "",
      // 非空判断
      hasNoConent: false,
      // 输入栏状态
      inputStatusClass: "",
      // 计数
      layerCount: 0,
      user: {},
    };
  },
  mounted() {
    this.$axios({
      url: '/api/loginer',
      method: 'get',
    }).then(res => {
      this.user = res.data.data || {}
      this.getComments();
    })
  },
  components: {
    "comments-child": CommentsChild,
  },
  methods: {
    // 提交
    submit() {
      if (!this.newComment) {
        this.$message.error('请输入内容')
        return
      }
      this.$axios({
        url: '/api/movie-remark/add',
        method: 'post',
        data: {userName: this.user.name, content: this.newComment, parentId: 0, movieId:this.movieId}
      }).then(res => {
        if (res.data.code === 0) {
          this.$message.success('留言成功');
          this.getComments()
          // 清空评论内容
          this.newComment = ""; } else {
          this.$message.error(res.data.msg);
        }
      })
    },
    // 获取数据
    getComments() {
      this.$axios({
        url: '/api/movie-remark/tree?movieId=' + this.movieId,
        method: 'get',
      }).then(res => {
        this.comments = res.data.data
      })
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* 评论头像 */
.msg-head img {
  width: 55px;
  height: 55px;
  border-radius: 50%;
  position: absolute;
  top: 22px;
  left: 13px;
}
.msg-all-contain {
  width: 100%;
  height: 100%;
  overflow-y: auto;
}

.msg-board-contain {
  letter-spacing: 1px;
  padding: 0 10px;
}

/* 信息栏标题 */
.msg-board-title {
  width: auto;
  text-align: center;
  font-size: 28px;
  font-weight: 300;
  margin: 0 0 1.8rem 0;
  min-height: 20px;
  color: #000 !important;
  font-family: "Lato", Verdana, sans-serif !important;
}
.msg-head {
  background-color: rgb(248, 248, 248);
  position: relative;
  height: 130px;
  border-radius: 5px;
}

/* 评论输入 */
.msg-head textarea {
  position: absolute;
  top: 13px;
  left: 85px;
  max-height: 60px;
  border-radius: 5px;
  outline: none;
  width: calc(100% - 300px);
  font-size: 16px;
  padding: 20px;
  border: 2px solid #f8f8f8;
  resize: none;
}
/* 发布评论按钮 */
.msg-head button {
  position: absolute;
  top: 13px;
  right: 35px;
  width: 100px;
  height: 100px;
  border: 0;
  border-radius: 5px;
  font-size: 18px;
  font-weight: 500;
  color: #fff !important;
  background-color: #00a1d6;
  transition: 0.1s;
  cursor: pointer;
  letter-spacing: 2px;
}
/* 鼠标经过字体加粗 */
.msg-head button:hover {
  /*font-weight: 600;*/
}

.msg-content {
  overflow-y: auto;
}

/* 评论内容区域 */
.msg-content .child-comment {
  display: flex;
  position: relative;
  padding: 18px 10px 18px 10px;
}

@media (max-width: 900px) {
  .msg-head img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    position: absolute;
    top: 22px;
    left: 13px;
  }
  .msg-head textarea {
    position: absolute;
    top: 13px;
    left: 70px;
    height: 55px;
    border-radius: 5px;
    outline: none;
    width: calc(100% - 200px);
    font-size: 15px;
    padding: 10px;
    border: 2px solid #f8f8f8;
    resize: none;
  }
  .msg-head button {
    position: absolute;
    top: 13px;
    right: 16px;
    width: 80px;
    height: 77px;
    border: 0;
    border-radius: 5px;
    font-size: 14px;
    font-weight: 500;
    color: #fff !important;
    background-color: #00a1d6;
    transition: 0.1s;
    cursor: pointer;
    letter-spacing: 2px;
  }
}
</style>


