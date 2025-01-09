import Vue from 'vue'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import App from './App.vue'
import router from './router'
import axios from 'axios';
Vue.prototype.$axios = axios.create({
  baseURL: '/',
  timeout: 30000
});

import * as echarts from 'echarts';
Vue.prototype.$echarts = echarts

Vue.prototype.$axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token")
    if (token) {
      config.headers['token'] = token
    }
    return config
  }
)

Vue.config.productionTip = false
Vue.use(ElementUI)
new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
