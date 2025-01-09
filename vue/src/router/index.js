
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('../views/Login'),
    meta: {
      title: '欢迎登陆'
    }
  },
  {
    path: '/main',
    name: 'Main',
    component: () => import('../views/Main'),
    meta: {
      title: '主页面'
    },
    children: [
      {
        path: '/user',
        name: 'User',
        component: () => import('../views/User'),
        meta: {
          title: '用户管理'
        }
      },
      {
        path: '/movie',
        name: 'Movie',
        component: () => import('../views/Movie'),
        meta: {
          title: '电影管理'
        }
      },
      {
        path: '/movieRemark',
        name: 'MovieRemark',
        component: () => import('../views/MovieRemark'),
        meta: {
          title: '电影评论'
        }
      },
    ]
  },
  {
    path: '/front',
    name: 'FrontMain',
    component: () => import('../views/front/Main'),
    meta: {
      title: '电影商城'
    },
    children: [
      {
        path: '/movieList',
        name: 'movieList',
        component: () => import('../views/front/movieList'),
        meta: {
          title: '列表'
        }
      },
      {
        path: '/movieDetail',
        name: 'movieDetail',
        component: () => import('../views/front/movieDetail'),
        meta: {
          title: '电影详情'
        }
      },
      {
        path: '/UserInfo',
        name: 'UserInfo',
        component: () => import('../views/front/UserInfo'),
        meta: {
          title: '个人中心'
        }
      },
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
