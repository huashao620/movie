<template>
  <div>
    <div :class="count > 0 ? '' : 'comments-child-contain'" v-for="(item, index) in comments" :key="index">
      <!--style 根据层级缩进-->
      <div class="comments-child" :style="{ paddingLeft: 30 * count + 'px' }">
        <div :class="count > 0 ? 'comments-child-img-sm' : 'comments-child-img'"></div>
        <div class="comments-child-content">
          <!-- 用户信息 -->
          <div class="comments-child-username-contain">
            <h3 class="comments-child-username">{{ item.user.name }}</h3>
            <div v-if="item.toUser.name" class="comments-child-replay">
              <span class="reply-text">回复</span>
              <h4 class="comments-child-at-username">
                @{{ item.toUser.name }}
              </h4>
            </div>
          </div>
          <!-- 评论内容 -->
          <p class="comments-comments-child">
            {{ item.content }}
          </p>
          <div class="comments-child-bottom-contain">
            <!-- 发布时间 -->
            <span class="comments-child-time"> {{ item.createTime }} </span>
            <!--删除和回复-->
            <div class="comments-child-right">
              <span v-if="user.id == item.userId" class="fa fa-trash-o delete" @click="commentDelete(item, $event)">
                删除
              </span>
              <span  v-if="user.id && layerCount" class="fa fa-comment-o comments" @click="goReply(item, $event)">回复</span>
            </div>
          </div>
          <div class="reply-comment">
            <input :class="inputStatusClass" type="text" v-model="replyComment" @keyup.enter="replySubmit(item, $event)"/>
            <button @click="replySubmit(item, $event)">回复</button>
          </div>
        </div>
      </div>
      <!--递归调用-->
      <div v-if="item.children">
        <comments-child :comments="item.children" :count="layerCount"></comments-child>
      </div>
    </div>
  </div>
</template>

<script>
import {Notification} from "element-ui";
export default {
  name: "CommentsChild",
  data() {
    return {
      // 回复评论
      replyComment: "",
      // 非空验证
      hasNoConent: false,
      inputStatusClass: "",
      layerCount: 0,
      // 点赞数
      like: 0,
      user: {}
    };
  },
  created() {
    var _this = this;
    _this.layerCount = _this.count;
    _this.layerCount++;
    this.$axios({
      url: '/api/loginer',
      method: 'get',
    }).then(res => {
      this.user = res.data.data || {}
    })
  },
  props: {
    // 卡片内容
    comments: {
      type: Array,
      required: true,
    },
    // 子评论计数
    count: {
      type: Number,
      default: 0,
    },
    movieId: {
      type: String,
      required: true
    },
  },

  methods: {
    commentDelete(obj) {
      this.$confirm('确定删除该讨论吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios({
          url: '/api/movie-remark/delete?id=' + obj.id,
          method: 'delete'
        }).then(res => {
          if (res.data.code === 0) {
            this.$message.success('删除成功');
            location.reload()
          } else {
            this.$message.error(res.data.msg);
          }
        })
      })
    },
    // 点赞
    commentLike(obj) {
    },
    // 显示回复输入框
    goReply(obj, event) {
      var _this = this;
      _this.inputStatusClass = "";
      _this.replyComment = "";
      var _thisDom = event.currentTarget;
      // 注意 nextElementSibling
      var replyDom = _thisDom.parentNode.parentNode.nextElementSibling;
      // 显示回复输入
      if (replyDom.style.display === "" || replyDom.style.display === "none") {
        replyDom.style.display = "flex";
        var replyInput = replyDom.getElementsByTagName("input")[0];
        // 添加回复人信息
        var placeContent = "回复" + " @ " + obj.user.name;
        replyInput.setAttribute("placeholder", placeContent);
      } else {
        replyDom.style.display = "none";
      }
    },
    // 回复信息提交
    replySubmit(obj, event) {
      if (!this.replyComment) {
        this.$message.error('请输入回复内容')
        return
      }
      let reply = {
        parentId: obj.id,
        content: this.replyComment,
        toUserId: obj.user.id,
        movieId: this.movieId,
        children: []
      }
      this.$axios({
        url: '/api/movie-remark/add',
        method: 'post',
        data: reply
      }).then(res => {
        if (res.data.code === 0) {
          Notification.success("回复成功")
          location.reload()
          // 清空评论内容
        } else {
          this.$message.error(res.data.msg);
        }
      })
    },
  },
};
</script>

<style scoped>
/* 评论内容区域 */
.msg-content .comments-child {
  display: flex;
  position: relative;
  padding: 18px 10px 18px 10px;
}
.comments-child-contain {
  border-bottom: 1px solid #d3d9e1;
  padding: 0 25px;
}

/* 子评论头像 */
.comments-child .comments-child-img {
  /*flex: 1;*/
  text-align: center;
  padding: 0 20px 0 0;
}
/* 子评论头像 */
.comments-child-img > img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

/* 子评论小头像 */
.comments-child .comments-child-img-sm {
  /*flex: 1;*/
  text-align: center;
  padding: 0 20px 0 0;
}
/* 子评论小头像 */
.comments-child-img-sm > img {
  width: 35px;
  height: 35px;
  border-radius: 50%;
}

/* 子评论用户名 */
.comments-child-username {
  color: #504f4f;
  margin: 0;
  font-size: 15px;
  width: auto;
  text-align: left;
}

/* 子评论回复用户名 */
.comments-child-at-username {
  margin: 0;
  color: #00a1d6;
}

.comments-child-username-contain {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  flex-wrap: nowrap;
  /*margin-bottom: 15px;*/
}

/* 回复内容 */
.reply-text {
  margin: 0 10px;
  font-size: 16px;
  font-weight: 400;
  color: #000 !important;
  font-family: "Lato", Verdana, sans-serif !important;
}

.comments-child-replay {
  display: flex;
  align-items: center;
  font-size: 15px;
  margin: 0;
}

.comments-child-content {
  flex: 9;
}
/* 回复时间 */
.comments-child-time {
  color: #767575;
  font-size: 12px;
  white-space: nowrap;
}
.comments-comments-child {
  font-size: 16px;
  margin-top: 10px;
  margin-bottom: 10px;
  font-weight: 400;
  color: #000 !important;
  font-family: "Lato", Verdana, sans-serif !important;
  text-align: left;
}

.comments-child-bottom-contain {
  display: flex;
  align-items: center;
}
/* 右边点赞和评论 */
.comments-child-right {
  position: absolute;
  right: 1.5%;
  top: 10px;
  white-space: nowrap;
}
.comments-child-right span {
  font-weight: 400;
  font-size: 15px;
  margin: 0 20px;
  cursor: pointer;
  color: #333 !important;
}
/* 删除评论 */
.delete:hover {
  color: red;
}
.delete::before {
  /* 想使用的icon的十六制编码，去掉&#x之后的 */
  margin-right: 4px;
  font-size: 16px;
}
/* 评论字体图标 */
.comments::before {
  /* 想使用的icon的十六制编码，去掉&#x之后的 */
  margin-right: 4px;
  font-size: 16px;
}
/* 点赞字体图标 */
.praise::before {
  /* 想使用的icon的十六制编码，去掉&#x之后的 */
  content: "\ec7f";
  /* 必须加 */
  font-family: "iconfont";
  margin-right: 4px;
  font-size: 19px;
}

.to_reply {
  color: rgb(106, 106, 106);
}

/* 评论 */
.reply-comment {
  margin: 10px 0 0 0;
  align-items: center;
  justify-content: space-around;
  display: none;
}
/* 评论输入框头像 */
.reply-comment > img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}
/* 评论输入框 */
.reply-comment input {
  height: 40px;
  border-radius: 5px;
  outline: none;
  width: 70%;
  font-size: 16px;
  padding: 0 10px;
  /* border: 2px solid #f8f8f8; */
  border: 2px solid skyblue;
}
/* 发布评论按钮 */
.reply-comment button {
  width: 100px;
  height: 43px;
  border: 0;
  border-radius: 5px;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 2px;
  color: #fff !important;
  background-color: #00a1d6;
  cursor: pointer;
}
/* 鼠标经过字体加粗 */
.reply-comment button:hover {
}
/* 评论点赞颜色 */
.comment-like {
  color: red;
}

.no-content-warn {
  border: 1px solid red !important;
}

@media (max-width: 900px) {
  .comments-child-right {
    position: inherit;
    margin-left: 10px;
  }

  .comments-child > img {
    width: 40px;
    height: 40px;
    border-radius: 50%;
  }

  .reply-comment button {
    width: 50px;
    height: 43px;
    border: 0;
    border-radius: 5px;
    font-size: 14px;
    font-weight: 500;
    color: #fff !important;
    background-color: #00a1d6;
    cursor: pointer;
  }

  .reply-comment input {
    height: 40px;
    border-radius: 5px;
    outline: none;
    width: 50%;
    font-size: 16px;
    padding: 0 10px;
    margin: 0 10px;
    /* border: 2px solid #f8f8f8; */
    border: 2px solid skyblue;
  }

  .comments-child-right span {
    font-weight: 400;
    font-size: 12px;
    margin: 0 5px;
    cursor: pointer;
    color: #333 !important;
  }

  .reply-comment {
    justify-content: flex-start;
  }
  .container-fluid {
    position: relative;
  }

  .comments-child-username-contain {
    flex-wrap: wrap;
  }
  .comments-child-username {
    width: 100%;
  }
  .comments-child-replay {
    margin-top: 10px;
  }
  .reply-text {
    margin: 0 10px 0 0;
  }
  .msg-class {
    font-size: 25px;
    line-height: 26px;
  }
}
</style>


